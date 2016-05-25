package hu.elte.txtuml.layout.visualizer.algorithms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementsConflictException;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementLevel;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Pair;

/**
 * This static class handles a variety of {@link Statement} related support
 * features.
 */
//TODO: Refactor this
public class StatementHelper {
	/**
	 * Returns the {@link Statement}s defined on {@link LineAssociation}s.
	 * 
	 * @param stats
	 *            the {@link Statement}s to check.
	 * @param assocs
	 *            the {@link LineAssociation}s to check.
	 * @return the {@link Statement}s defined on {@link LineAssociation}s.
	 */
	public static List<Statement> splitAssocs(List<Statement> stats, Set<LineAssociation> assocs) {
		return stats.stream().filter(s -> s.getType().isAssocType() && isAssocParams(s.getParameters(), assocs))
				.collect(Collectors.toList());
	}
	
	private static boolean isAssocParams(List<String> p, Set<LineAssociation> as) {
		return as.stream().anyMatch(a -> a.getId().equals(p.get(0)));
	}

	/**
	 * Method that removes duplicate {@link Statement}s and checks for direct
	 * conflicts.
	 * 
	 * @param stats
	 *            list of {@link Statement}s to check.
	 * @return list of duplicate-free {@link Statement}s.
	 * @throws ConversionException
	 *             Throws if some value cannot be converted to another value.
	 * @throws StatementsConflictException
	 *             Throws if a direct conflict is found in the user statements.
	 */
	public static List<Statement> reduceAssocs(List<Statement> stats)
			throws ConversionException, StatementsConflictException {
		List<Statement> result = new ArrayList<Statement>();

		// Link name, End name, Direction value
		HashMap<Pair<String, String>, Direction> tempObj = new HashMap<Pair<String, String>, Direction>();
		// Link name, Priority value
		HashMap<String, Integer> tempPrior = new HashMap<String, Integer>();

		for (Statement s : stats) {
			// Priority conflict check
			if (s.getType().equals(StatementType.priority)) {
				if (tempPrior.containsKey(s.getParameter(0))) {
					if (tempPrior.get(s.getParameter(0)).equals(Integer.parseInt(s.getParameter(1))))
						continue; // WARNING: Duplicate
					else {
						if (!s.isUserDefined())
							continue;
						else
							throw new StatementsConflictException("Priorities not match: " + s.toString()
									+ " with older data: " + tempPrior.get(s.getParameter(0)).toString());
					}
				} else {
					tempPrior.put(s.getParameter(0), Integer.parseInt(s.getParameter(1)));
					result.add(s);
				}
			} else {
				// Direction conflict check
				Pair<String, String> pair = new Pair<String, String>(s.getParameter(0), s.getParameter(1));
				if (s.getParameters().size() == 3) {
					pair = new Pair<String, String>(pair.getFirst(), pair.getSecond() + "_" + s.getParameter(2));
				}

				if (tempObj.containsKey(pair)) {
					if (tempObj.get(pair).equals(s.getType().asDirection()))
						continue; // WARNING: Duplicate
					else {
						if (!s.isUserDefined())
							continue;
						else
							throw new StatementsConflictException(
									"Too many statements on " + s.getParameter(0) + " link!");
					}
				} else {
					tempObj.put(pair, s.getType().asDirection());
					result.add(s);
				}
			}
		}

		return result;
	}

	/**
	 * Returns a {@link Set} of the names of phantom objects/boxes.
	 * 
	 * @param stats
	 *            list of {@link Statement}s to search in.
	 * @return a {@link Set} of the names of phantom objects/boxes.
	 */
	public static Set<String> extractPhantoms(List<Statement> stats) {
		return stats.stream().filter(s -> s.getType().equals(StatementType.phantom))
				.map(s -> s.getParameter(0))
				.collect(Collectors.toSet());
	}

	/**
	 * Returns a {@link Pair} of generated {@link Statement}s based on
	 * {@link LineAssociation}s, {@link RectangleObject}s and the latest Group
	 * Id used.
	 * 
	 * @param type
	 *            type of the diagrams.
	 * 
	 * @param objs
	 *            set of {@link RectangleObject}s to check.
	 * @param assocs
	 *            set of {@link LineAssociation}s to check.
	 * @param par_gid
	 *            latest Group Id number used.
	 * @return a {@link Pair} of generated {@link Statement}s and the latest
	 *         Group Id used.
	 * @throws InternalException
	 *             Throws if something bad happens, which is not allowed to
	 *             happen.
	 */
	public static Pair<List<Statement>, Integer> transformAssocs(DiagramType type, Set<RectangleObject> objs,
			Set<LineAssociation> assocs, Integer par_gid) throws InternalException {
		switch (type) {
		case Class:
			return transformAssocs_ClassDiagram(assocs, par_gid);
		case Activity:
		case State:
		default:
			throw new InternalException("This diagram type is not supported");
		}
	}

	private static Pair<List<Statement>, Integer> transformAssocs_ClassDiagram(Set<LineAssociation> assocs,
			Integer par_gid) throws InternalException {
		Integer gid = par_gid;
		List<Statement> result = new ArrayList<Statement>();

		HashMap<String, List<String>> generalizationMap = new HashMap<String, List<String>>();

		for (LineAssociation a : assocs) {
			switch (a.getType()) {
			case generalization:
				if (generalizationMap.containsKey(a.getFrom())) {
					List<String> temp = generalizationMap.get(a.getFrom());
					temp.add(a.getTo());
					generalizationMap.put(a.getFrom(), temp);
				} else {
					List<String> temp = new ArrayList<String>();
					temp.add(a.getTo());
					generalizationMap.put(a.getFrom(), temp);
				}
				++gid;
				result.add(new Statement(StatementType.north, StatementLevel.Low, gid, a.getFrom(), a.getTo()));
				break;
			case aggregation:
			case composition:
			case normal:
			default:
				break;
			}
		}

		// Arrange generalization children
		for (Entry<String, List<String>> entry : generalizationMap.entrySet()) {
			if (entry.getValue().size() > 1) {
				++gid;
				for (int i = 0; i < entry.getValue().size() - 1; ++i) {
					String s1 = entry.getValue().get(i);
					for (int j = i + 1; j < entry.getValue().size(); ++j) {
						String s2 = entry.getValue().get(j);

						result.add(new Statement(StatementType.horizontal, StatementLevel.Low, gid, s1, s2));
					}
				}
			}

			++gid;
			Integer index;
			if (entry.getValue().size() > 1) {
				Random r = new Random();
				index = r.nextInt(entry.getValue().size() - 1);
			} else {
				index = 0;
			}
			result.add(new Statement(StatementType.above, StatementLevel.Medium, gid, entry.getKey(),
					entry.getValue().get(index)));
		}

		return new Pair<List<Statement>, Integer>(result, gid);
	}
	
	public static List<Statement> filterBoxStatements(final List<Statement> stats, final List<RectangleObject> objs)
	{
		List<Statement> result = new ArrayList<Statement>();
		
		for(Statement statement : stats)
		{
			if(objs.stream().anyMatch(box -> box.getName().equals(statement.getParameter(0)) || box.getName().equals(statement.getParameter(1)) ))
			{
				result.add(statement);
			}
		}
		
		return result;
	}
}
