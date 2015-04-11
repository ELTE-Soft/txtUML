package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.BreathFirstSearch.LabelType;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

class StatementHelper
{
	public static ArrayList<Statement> splitAssocs(ArrayList<Statement> stats,
			Set<LineAssociation> assocs)
	{
		return (ArrayList<Statement>) stats
				.stream()
				.filter(s -> isAssocType(s.getType())
						&& isAssocParams(s.getParameters(), assocs))
				.collect(Collectors.toList());
	}
	
	private static boolean isAssocType(StatementType t)
	{
		switch (t)
		{
			case north:
			case south:
			case east:
			case west:
			case priority:
				return true;
			default:
				break;
		}
		
		return false;
	}
	
	private static boolean isAssocParams(ArrayList<String> p, Set<LineAssociation> as)
	{
		return as.stream().anyMatch(a -> a.getId().equals(p.get(0)));
	}
	
	public static ArrayList<Statement> reduceAssocs(ArrayList<Statement> stats)
			throws ConflictException, ConversionException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		// Link name, End name, Direction value
		HashMap<Pair<String, String>, Direction> tempObj = new HashMap<Pair<String, String>, Direction>();
		// Link name, Priority value
		HashMap<String, Integer> tempPrior = new HashMap<String, Integer>();
		
		for (Statement s : stats)
		{
			// Priority conflict check
			if (s.getType().equals(StatementType.priority))
			{
				if (tempPrior.containsKey(s.getParameter(0)))
				{
					if (tempPrior.get(s.getParameter(0)).equals(
							Integer.parseInt(s.getParameter(1))))
						continue; // WARNING: Duplicate
					else
						throw new ConflictException("Priorities not match: "
								+ s.toString() + " with older data: "
								+ tempPrior.get(s.getParameter(0)).toString());
				}
				else
				{
					tempPrior.put(s.getParameter(0), Integer.parseInt(s.getParameter(1)));
					result.add(s);
				}
			}
			else
			{
				// Direction conflict check
				Pair<String, String> pair = new Pair<String, String>(s.getParameter(0),
						s.getParameter(1));
				if (tempObj.containsKey(pair))
				{
					if (tempObj.get(pair).equals(Helper.asDirection(s.getType())))
						continue; // WARNING: Duplicate
					else
						throw new ConflictException("Túl sok irány statement a "
								+ s.getParameter(0) + " linkre!");
				}
				else
				{
					tempObj.put(pair, Helper.asDirection(s.getType()));
					result.add(s);
				}
			}
		}
		
		return result;
	}
	
	public static Set<String> extractPhantoms(ArrayList<Statement> stats)
	{
		return (Set<String>) stats.stream()
				.filter(s -> s.getType().equals(StatementType.phantom))
				.map(s -> s.getParameter(0)).collect(Collectors.toSet());
	}
	
	public static ArrayList<Statement> reduceObjects(ArrayList<Statement> stats,
			Set<RectangleObject> objs)
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		// Delete duplicates and reverse duplicates
		for (Statement s : stats)
		{
			if (result.contains(s))
				continue;
			else if (result.contains(opposite(s)))
				continue;
			else
				result.add(s);
		}
		
		return result;
	}
	
	public static boolean isTypeChecked(Statement st, Set<RectangleObject> ob,
			Set<LineAssociation> as) throws InternalException
	{
		switch (st.getType())
		{
			case north:
			case south:
			case east:
			case west:
				// 2 féle is lehet
				if (ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(0)))
						&& ob.stream().anyMatch(
								o -> o.getName().equals(st.getParameter(1))))
					return true;
				else if (as.stream().anyMatch(a -> a.getId().equals(st.getParameter(0)))
						&& ob.stream().anyMatch(
								o -> o.getName().equals(st.getParameter(1))))
					return true;
				break;
			case above:
			case below:
			case right:
			case left:
				// csak obejct lehet
				if (ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(0)))
						&& ob.stream().anyMatch(
								o -> o.getName().equals(st.getParameter(1))))
					return true;
				break;
			case priority:
				// csak assoc lehet
				if (as.stream().anyMatch(a -> a.getId().equals(st.getParameter(0)))
						&& Helper.tryParseInt(st.getParameter(1)))
					return true;
				break;
			case phantom:
				if (ob.stream().anyMatch(o -> o.getName().equals(st.getParameter(0))))
					return true;
				break;
			default:
				throw new InternalException("This statement should not reach this code: "
						+ st.toString() + "!");
		}
		return false;
	}
	
	public static ArrayList<Statement> transformAssocs(ArrayList<Statement> stats,
			Set<LineAssociation> assocs)
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		for (LineAssociation a : assocs)
		{
			switch (a.getType())
			{
				case generalization:
					// If it would mean a conflict, we don't add it
					if (!stats.contains(new Statement(StatementType.south, a.getTo(), a
							.getFrom())))
						result.add(new Statement(StatementType.north, a.getFrom(), a
								.getTo()));
					break;
				case aggregation:
					
					break;
				case composition:
					
					break;
				default:
					
					break;
			}
		}
		
		return result;
	}
	
	public static Statement opposite(Statement s)
	{
		return new Statement(opposite(s.getType()), s.getParameter(1), s.getParameter(0));
	}
	
	public static StatementType opposite(StatementType st)
	{
		if (st.equals(StatementType.north))
			return StatementType.south;
		if (st.equals(StatementType.south))
			return StatementType.north;
		
		if (st.equals(StatementType.east))
			return StatementType.west;
		if (st.equals(StatementType.west))
			return StatementType.east;
		
		if (st.equals(StatementType.above))
			return StatementType.below;
		if (st.equals(StatementType.below))
			return StatementType.above;
		
		if (st.equals(StatementType.right))
			return StatementType.left;
		if (st.equals(StatementType.left))
			return StatementType.right;
		
		return StatementType.unknown;
	}
	
	public static ArrayList<Statement> defaultStatements(Set<RectangleObject> os,
			Set<LineAssociation> as) throws InternalException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		HashMap<String, Integer> degrees = new HashMap<String, Integer>();
		HashMap<String, HashSet<String>> accesses = new HashMap<String, HashSet<String>>();
		
		for (LineAssociation a : as)
		{
			if (degrees.containsKey(a.getFrom()))
				degrees.put(a.getFrom(), degrees.get(a.getFrom()) + 1);
			else
				degrees.put(a.getFrom(), 1);
			
			if (degrees.containsKey(a.getTo()))
				degrees.put(a.getTo(), degrees.get(a.getTo()) + 1);
			else
				degrees.put(a.getTo(), 1);
			
			if (accesses.containsKey(a.getFrom()))
			{
				HashSet<String> temp = accesses.get(a.getFrom());
				if (!temp.contains(a.getTo()))
					temp.add(a.getTo());
				accesses.put(a.getFrom(), temp);
			}
			else
			{
				HashSet<String> temp = new HashSet<String>();
				temp.add(a.getTo());
				accesses.put(a.getFrom(), temp);
			}
			
			if (accesses.containsKey(a.getTo()))
			{
				HashSet<String> temp = accesses.get(a.getTo());
				if (!temp.contains(a.getFrom()))
					temp.add(a.getFrom());
				accesses.put(a.getTo(), temp);
			}
			else
			{
				HashSet<String> temp = new HashSet<String>();
				temp.add(a.getFrom());
				accesses.put(a.getTo(), temp);
			}
		}
		
		// Detect groups
		ArrayList<HashSet<String>> groups = new ArrayList<HashSet<String>>();
		
		HashSet<String> openToCheck = (HashSet<String>) Helper.cloneStringSet(accesses
				.keySet());
		Graph<String> G = buildGraph(accesses);
		String start = openToCheck.stream().findFirst().get();
		do
		{
			BreathFirstSearch bfs = new BreathFirstSearch(G, start);
			
			HashSet<String> oneGroup = (HashSet<String>) bfs.value().entrySet().stream()
					.filter(entry -> entry.getValue().equals(LabelType.discovered))
					.map(entry -> entry.getKey()).collect(Collectors.toSet());
			groups.add(oneGroup);
			
			openToCheck.removeAll(oneGroup);
			
			if (openToCheck.size() == 0)
				break;
			
			start = openToCheck.stream().findFirst().get();
			
		} while (true);
		
		// Arrange groups
		Integer gridSize = (int) Math.pow(Math.ceil(Math.sqrt(groups.size())), 2);
		
		for (int i = 0; i < groups.size(); ++i)
		{
			if (existsItem(groups, i, gridSize, Direction.west))
			{
				result.addAll(generateStatements(groups, i, gridSize, StatementType.west));
			}
			if (existsItem(groups, i, gridSize, Direction.east))
			{
				result.addAll(generateStatements(groups, i, gridSize, StatementType.east));
			}
			if (existsItem(groups, i, gridSize, Direction.north))
			{
				result.addAll(generateStatements(groups, i, gridSize, StatementType.north));
			}
			if (existsItem(groups, i, gridSize, Direction.south))
			{
				result.addAll(generateStatements(groups, i, gridSize, StatementType.south));
			}
		}
		
		// Arrange in groups
		// TODO
		
		// TODO
		// groupokra szedni az objecteket, linkek mentén
		// sok linkel rendelkezõ objectek középre
		
		return result;
	}
	
	private static Graph<String> buildGraph(HashMap<String, HashSet<String>> edges)
	{
		Graph<String> result = new Graph<String>();
		
		for (Entry<String, HashSet<String>> entry : edges.entrySet())
		{
			// add Node
			result.addNode(entry.getKey());
			
			// add Link
			for (String to : entry.getValue())
			{
				result.addLink(new Link<String>(entry.getKey(), to));
				result.addLink(new Link<String>(to, entry.getKey()));
			}
		}
		
		return result;
	}
	
	private static boolean existsItem(ArrayList<HashSet<String>> groups, Integer i,
			Integer gridSize, Direction dir)
	{
		switch (dir)
		{
			case north:
				if (i >= gridSize)
					return true;
				break;
			case east:
				if (i < (groups.size() - 1) && (i % gridSize) != 2)
					return true;
				break;
			case south:
				if (i < (groups.size() - gridSize))
					return true;
				break;
			case west:
				if ((i % gridSize) != 0)
					return true;
				break;
		}
		
		return false;
	}
	
	private static Integer getIndex(Integer i, Integer gridSize, StatementType type)
			throws InternalException
	{
		switch (type)
		{
			case north:
				return (i - gridSize);
			case east:
				return (i + 1);
			case south:
				return (i + gridSize);
			case west:
				return (i - 1);
			default:
				throw new InternalException("The type \'" + type.toString()
						+ "\' should not get here!");
		}
	}
	
	private static ArrayList<Statement> generateStatements(
			ArrayList<HashSet<String>> groups, Integer i, Integer gridSize,
			StatementType type) throws InternalException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		Integer index = getIndex(i, gridSize, type);
		for (String move : groups.get(index))
		{
			for (String fix : groups.get(i))
			{
				result.add(new Statement(type, move, fix));
			}
		}
		
		return result;
	}
}
