package hu.elte.txtuml.layout.visualizer.algorithms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Link;
import hu.elte.txtuml.layout.visualizer.algorithms.utils.BreathFirstSearch.LabelType;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;
import hu.elte.txtuml.layout.visualizer.model.utils.RectangleObjectTreeEnumerator;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementLevel;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * This class generates the necessary default statements to auto layout a
 * diagram.
 */
public class DefaultStatements {

	/**
	 * List of generated {@link Statement}s.
	 */
	private List<Statement> _statements;
	/**
	 * The previously used Group Id of a {@link Statement}.
	 */
	private Integer _gId;

	/**
	 * Constructor and run command for the generation of default statements.
	 * 
	 * @param diagram
	 *            Diagram to work with.
	 * 
	 * @param ss
	 *            List of statements.
	 * @param gid
	 *            The last used group id.
	 * @throws InternalException
	 *             Throws if some algorithm related error occurs. Contact with
	 *             your programmer in the nearest zoo for more details.
	 */
	public DefaultStatements(final Diagram diagram, List<Statement> ss, Integer gid) throws InternalException {
		_statements = new ArrayList<Statement>();
		_gId = gid;

		switch (diagram.Type) {
		case Class:
			defaultClass(diagram, ss);
			break;
		case State:
			defaultState(diagram);
			break;
		case Activity:
		case Composite:
		case unknown:
		default:
			break;
		}

	}

	/**
	 * Returns the maximum group Id.
	 * 
	 * @return the maximum group Id.
	 */
	public Integer getGroupId() {
		return _gId;
	}

	/**
	 * Returns the list of default statements.
	 * 
	 * @return List of default statements.
	 */
	public List<Statement> value() {
		return _statements;
	}

	/**
	 * Run command for the generation of default statements.
	 * 
	 * @param objects
	 *            Set of objects in the diagram.
	 * @param links
	 *            Set of links in the diagram.
	 * @param statements
	 *            List of statements.
	 * @throws InternalException
	 *             Throws if some algorithm related error occurs. Contact with
	 *             your programmer in the nearest zoo for more details.
	 */
	private void defaultClass(final Diagram diagram, List<Statement> statements) throws InternalException {

		// Assamble access table
		HashMap<String, HashSet<String>> accesses = new HashMap<String, HashSet<String>>();
		HashMap<String, String> parent = new HashMap<String, String>();
		for (RectangleObject box : diagram.Objects) {
			accesses.put(box.getName(), new HashSet<String>());
			if (box.hasInner()) {
				for (RectangleObject innerBox : new RectangleObjectTreeEnumerator(box.getInner().Objects)) {
					parent.put(innerBox.getName(), box.getName());
				}
			}
		}

		for (LineAssociation link : diagram.Assocs) {
			// if (a.isReflexive())
			// continue;

			accesses = putAccess(link.getFrom(), link.getTo(), accesses, parent);

			accesses = putAccess(link.getTo(), link.getFrom(), accesses, parent);
		}

		for (Statement statement : statements) {
			if (!statement.getType().isOnObjects())
				continue;

			accesses = putAccess(statement.getParameter(0), statement.getParameter(1), accesses, parent);

			accesses = putAccess(statement.getParameter(1), statement.getParameter(0), accesses, parent);
		}

		// Detect groups, arrange groups, arrange in groups
		_statements.addAll(detectGroups(accesses));
	}

	private HashMap<String, HashSet<String>> putAccess(final String target1, final String target2,
			final HashMap<String, HashSet<String>> accesses, final HashMap<String, String> parent) {
		HashMap<String, HashSet<String>> result = new HashMap<String, HashSet<String>>(accesses);

		HashSet<String> temp = accesses.get(target1);
		if (temp == null) {
			temp = accesses.get(parent.get(target1));
		}
		if (!temp.contains(target2)) {
			temp.add(target2);
			result.put(target1, temp);
		}

		return result;
	}

	/**
	 * Method to construct the {@link Graph} we want to search in.
	 * 
	 * @param edges
	 *            the accesses from one Box.
	 * @return the {@link Graph} to search in.
	 */
	private Graph<String> buildGraph(HashMap<String, HashSet<String>> edges) {
		Graph<String> result = new Graph<String>();

		for (Entry<String, HashSet<String>> entry : edges.entrySet()) {
			// add Node
			result.addNode(entry.getKey());

			// add Link
			for (String to : entry.getValue()) {
				result.addLink(new Link<String>(entry.getKey(), to));
				result.addLink(new Link<String>(to, entry.getKey()));
			}
		}

		return result;
	}

	private ArrayList<Statement> detectGroups(HashMap<String, HashSet<String>> accesses) throws InternalException {
		ArrayList<Statement> result = new ArrayList<Statement>();

		ArrayList<HashSet<String>> groups = new ArrayList<HashSet<String>>();

		HashSet<String> openToCheck = (HashSet<String>) Helper.cloneStringSet(accesses.keySet());
		Graph<String> G = buildGraph(accesses);

		if (openToCheck.size() != 0) {
			String start = openToCheck.stream().findFirst().get();
			do {
				BreathFirstSearch<String> bfs = new BreathFirstSearch<String>(G, start);

				HashSet<String> oneGroup = (HashSet<String>) bfs.value().entrySet().stream()
						.filter(entry -> entry.getValue().equals(LabelType.discovered)).map(entry -> entry.getKey())
						.collect(Collectors.toSet());
				groups.add(oneGroup);

				openToCheck.removeAll(oneGroup);

				if (openToCheck.size() == 0)
					break;

				start = openToCheck.stream().findFirst().get();

			} while (true);
		}

		// Arrange groups
		groups.sort((a, b) -> {
			return -1 * Integer.compare(a.size(), b.size());
		});

		for (int i = 0; i < groups.size() - 1; ++i) {
			for (int j = 1; (i + j) < groups.size(); ++j) {
				++_gId;
				result.addAll(statementsBetweenGroups(groups.get(i), groups.get(i + j), _gId));
			}
		}

		return result;
	}

	private ArrayList<Statement> statementsBetweenGroups(HashSet<String> g1, HashSet<String> g2, Integer gid)
			throws InternalException {
		ArrayList<Statement> result = new ArrayList<Statement>();

		for (String w : g1) {
			for (String e : g2) {
				result.add(new Statement(StatementType.west, StatementLevel.Medium, gid, w, e));
			}
		}

		return result;
	}

	private void defaultState(final Diagram diagram) throws InternalException {
		// Initial node on top left
		Optional<RectangleObject> initial_element = diagram.Objects.stream()
				.filter(o -> o.getSpecial().equals(SpecialBox.Initial)).findFirst();
		if (initial_element.isPresent()) {
			++_gId;
			for (RectangleObject otherBox : diagram.Objects) {
				if (otherBox.getName().equals(initial_element.get().getName()))
					continue;

				_statements.add(new Statement(StatementType.north, StatementLevel.Medium, _gId,
						initial_element.get().getName(), otherBox.getName()));
			}
		}

		// States in one row
		Optional<RectangleObject> noInTransition = diagram.Objects.stream()
				.filter(o -> (diagram.Assocs.stream().filter(a -> a.getTo().equals(o.getName())).count()) == 0)
				.findFirst();

		String first;
		String before;
		if (noInTransition.isPresent()) {
			first = noInTransition.get().getName();
		} else {
			first = diagram.Objects.stream().findFirst().get().getName();
		}

		before = first;
		++_gId;
		for (RectangleObject box : diagram.Objects) {
			if (box.getName().equals(first))
				continue;

			_statements.add(new Statement(StatementType.left, StatementLevel.High, _gId, before, box.getName()));

			before = box.getName();
		}

		// Forks
		for (RectangleObject box : diagram.Objects) {
			if (box.getSpecial().equals(SpecialBox.Choice)) {
				for (LineAssociation linkToChoice : diagram.Assocs.stream().filter(a -> a.getTo().equals(box.getName()))
						.collect(Collectors.toList())) {
					++_gId;
					_statements.add(new Statement(StatementType.north, StatementLevel.Medium, _gId,
							linkToChoice.getId(), box.getName()));
				}

				for (LineAssociation linkToChoice : diagram.Assocs.stream().filter(a -> a.getFrom().equals(box.getName()))
						.collect(Collectors.toList())) {
					++_gId;
					_statements.add(new Statement(StatementType.south, StatementLevel.Medium, _gId,
							linkToChoice.getId(), box.getName()));
				}
			}
		}

	}

}
