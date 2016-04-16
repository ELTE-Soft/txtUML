package hu.elte.txtuml.layout.visualizer.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.algorithms.BreathFirstSearch.LabelType;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.SpecialBox;
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

	private DiagramType _diagramType;

	/**
	 * Constructor and run command for the generation of default statements.
	 * 
	 * @param type
	 *            type of the Diagram.
	 * 
	 * @param os
	 *            Set of objects in the diagram.
	 * @param as
	 *            Set of links in the diagram.
	 * @param ss
	 *            List of statements.
	 * @param gid
	 *            The last used group id.
	 * @throws InternalException
	 *             Throws if some algorithm related error occurs. Contact with
	 *             your programmer in the nearest zoo for more details.
	 */
	public DefaultStatements(DiagramType type, Set<RectangleObject> os, Set<LineAssociation> as, List<Statement> ss,
			Integer gid) throws InternalException {
		_diagramType = type;
		_statements = new ArrayList<Statement>();
		_gId = gid;

		switch (_diagramType) {
		case Class:
			defaultClass(os, as, ss);
			break;
		case State:
			defaultState(os, as, ss);
			break;
		case Activity:
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
	 * @param os
	 *            Set of objects in the diagram.
	 * @param as
	 *            Set of links in the diagram.
	 * @param ss
	 *            List of statements.
	 * @throws InternalException
	 *             Throws if some algorithm related error occurs. Contact with
	 *             your programmer in the nearest zoo for more details.
	 */
	private void defaultClass(Set<RectangleObject> os, Set<LineAssociation> as, List<Statement> ss)
			throws InternalException {
		// Assamble access table
		HashMap<String, HashSet<String>> accesses = new HashMap<String, HashSet<String>>();
		for (RectangleObject o : os) {
			accesses.put(o.getName(), new HashSet<String>());
		}

		for (LineAssociation a : as) {
			// if (a.isReflexive())
			// continue;

			HashSet<String> tempF = accesses.get(a.getFrom());
			if (!tempF.contains(a.getTo())) {
				tempF.add(a.getTo());
				accesses.put(a.getFrom(), tempF);
			}

			HashSet<String> tempT = accesses.get(a.getTo());
			if (!tempT.contains(a.getFrom())) {
				tempT.add(a.getFrom());
				accesses.put(a.getTo(), tempT);
			}
		}

		for (Statement s : ss) {
			if (!StatementType.isOnObjects(s.getType()))
				continue;

			HashSet<String> tempF = accesses.get(s.getParameter(0));
			if (!tempF.contains(s.getParameter(1))) {
				tempF.add(s.getParameter(1));
				accesses.put(s.getParameter(0), tempF);
			}

			HashSet<String> tempT = accesses.get(s.getParameter(1));
			if (!tempT.contains(s.getParameter(0))) {
				tempT.add(s.getParameter(0));
				accesses.put(s.getParameter(1), tempT);
			}
		}

		// Detect groups, arrange groups, arrange in groups
		_statements.addAll(detectGroups(accesses));
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

	private void defaultState(Set<RectangleObject> os, Set<LineAssociation> as, List<Statement> ss) throws InternalException {
		// Initial node on top left
		Optional<RectangleObject> initial_element = os.stream().filter(o -> o.getSpecial().equals(SpecialBox.Initial))
				.findFirst();
		if (initial_element.isPresent()) {
			++_gId;
			for (RectangleObject otherBox : os) {
				if(otherBox.getName().equals(initial_element.get().getName()))
					continue;
				
				_statements.add(new Statement(StatementType.north, StatementLevel.Medium, _gId,
						initial_element.get().getName(), otherBox.getName()));
			}
		}
		
		// States in one row
		Optional<RectangleObject> noInTransition = 
				os.stream().filter(o -> (as.stream()
						.filter(a -> a.getTo().equals(o.getName())).count()) == 0)
				.findFirst();
		
		String first;
		String before;
		if(noInTransition.isPresent())
		{
			first = noInTransition.get().getName();
		}
		else
		{
			first = os.stream().findFirst().get().getName();
		}
		
		before = first;
		++_gId;
		for(RectangleObject box : os)
		{
			if(box.getName().equals(first))
				continue;
			
			_statements.add(new Statement(StatementType.left, StatementLevel.High,
					_gId, before, box.getName()));
			
			before = box.getName();
		}
		
		// Forks
		for(RectangleObject box : os)
		{
			if(box.getSpecial().equals(SpecialBox.Choice))
			{
				for(LineAssociation linkToChoice : as.stream().filter(a -> a.getTo().equals(box.getName())).collect(Collectors.toList()))
				{
					++_gId;
					_statements.add(new Statement(StatementType.north, StatementLevel.Medium, 
							_gId, linkToChoice.getId(), box.getName()));
				}
				
				for(LineAssociation linkToChoice : as.stream().filter(a -> a.getFrom().equals(box.getName())).collect(Collectors.toList()))
				{
					++_gId;
					_statements.add(new Statement(StatementType.south, StatementLevel.Medium, 
							_gId, linkToChoice.getId(), box.getName()));
				}
			}
		}
		
	}

}
