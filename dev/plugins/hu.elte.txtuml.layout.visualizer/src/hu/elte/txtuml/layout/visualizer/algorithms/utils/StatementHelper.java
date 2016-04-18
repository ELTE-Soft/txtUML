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
import hu.elte.txtuml.layout.visualizer.exceptions.StatementTypeMatchException;
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
		return stats.stream().filter(s -> isAssocType(s.getType()) && isAssocParams(s.getParameters(), assocs))
				.collect(Collectors.toList());
	}

	/**
	 * Checks the parameter types of the {@link Statement}s provided.
	 * 
	 * @param stats
	 *            list of {@link Statement}s on objects/boxes to check.
	 * @param astats
	 *            list of {@link Statement}s on links to check.
	 * @param objs
	 *            set of objects/boxes in the diagram.
	 * @param assocs
	 *            set of links in the diagram.
	 * @return true if all of the {@link Statement}s have correctly typed
	 *         parameters according to the elements in the diagram.
	 * @throws StatementTypeMatchException
	 *             Throws if one of the {@link Statement}s have incorrect
	 *             parameters.
	 * @throws InternalException
	 *             Throws if something bad happens, but it should not be allowed
	 *             to happen.
	 */
	public static boolean checkTypes(List<Statement> stats, List<Statement> astats, Set<RectangleObject> objs,
			Set<LineAssociation> assocs) throws StatementTypeMatchException, InternalException {
		// Check Object Statement Types
		for (Statement s : stats) {
			if (!StatementHelper.isTypeChecked(s, objs, assocs))
				throw new StatementTypeMatchException("Types not match at statement: " + s.toString() + "!");
		}
		// Check Association Statement Types
		for (Statement s : astats) {
			if (!StatementHelper.isTypeChecked(s, objs, assocs))
				throw new StatementTypeMatchException("Types not match at statement: " + s.toString() + "!");
		}

		return true;
	}

	private static boolean isAssocType(StatementType t) {
		switch (t) {
		case north:
		case south:
		case east:
		case west:
		case priority:
			return true;
		case above:
		case below:
		case horizontal:
		case left:
		case phantom:
		case right:
		case unknown:
		case vertical:
		case corridorsize:
		case overlaparrange:
		default:
			break;
		}

		return false;
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
					if (tempObj.get(pair).equals(Helper.asDirection(s.getType())))
						continue; // WARNING: Duplicate
					else {
						if (!s.isUserDefined())
							continue;
						else
							throw new StatementsConflictException(
									"Too many statements on " + s.getParameter(0) + " link!");
					}
				} else {
					tempObj.put(pair, Helper.asDirection(s.getType()));
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
	 * Returns whether a specific {@link Statement} has correct parameters
	 * according to the diagram or not.
	 * 
	 * @param st
	 *            {@link Statement} to check.
	 * @param ob
	 *            set of objects/boxes in the diagram.
	 * @param as
	 *            set of links in the diagram.
	 * @return true if the {@link Statement} st has correct parameters.
	 * @throws InternalException
	 *             Throws if something bad happens, which should not be allowed
	 *             to happen.
	 */
	public static boolean isTypeChecked(Statement st, Set<RectangleObject> ob, Set<LineAssociation> as)
			throws InternalException {
		switch (st.getType()) {
		case north:
		case south:
		case east:
		case west:
			// both type
			if (ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(0)))
					&& ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(1))))
				return true;
			else if (as.stream().anyMatch(a -> a.getId().equals(st.getParameter(0)))
					&& ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(1))))
				return true;
			break;
		case above:
		case below:
		case right:
		case left:
		case horizontal:
		case vertical:
			// only object/box
			if (ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(0)))
					&& ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(1))))
				return true;
			break;
		case priority:
			// only assoc/link
			if (as.stream().anyMatch(a -> a.getId().equals(st.getParameter(0)))
					&& Helper.tryParseInt(st.getParameter(1)))
				return true;
			break;
		case phantom:
			if (ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(0))))
				return true;
			break;
		case unknown:
		case corridorsize:
		case overlaparrange:
		default:
			throw new InternalException("This statement should not reach this code: " + st.toString() + "!");
		}
		return false;
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

	/**
	 * Returns the abstract complexity value of a {@link Statement}.
	 * 
	 * @param s
	 *            {@link Statement} to check.
	 * @return the abstract complexity value of a {@link Statement}.
	 */
	public static Integer getComplexity(Statement s) {
		Integer result = 0;

		switch (s.getLevel()) {
		case User:
			result = -100;
			break;
		case High:
			result = 100;
			break;
		case Medium:
			result = 50;
			break;
		case Low:
			result = 10;
			break;
		}

		switch (s.getType()) {
		case north:
		case south:
		case east:
		case west:
			return result + 5;
		case above:
		case below:
		case right:
		case left:
			return result + 10;
		case horizontal:
		case vertical:
			return result + 15;
		case phantom:
		case priority:
		case unknown:
		case corridorsize:
		case overlaparrange:
		default:
			return result + 100;
		}
	}
}
