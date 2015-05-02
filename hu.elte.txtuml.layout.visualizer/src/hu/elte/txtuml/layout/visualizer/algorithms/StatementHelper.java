package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementLevel;
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
			case above:
			case below:
			case horizontal:
			case left:
			case phantom:
			case right:
			case unknown:
			case vertical:
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
					{
						if (!s.isUserDefined())
							continue;
						else
							throw new ConflictException("Priorities not match: "
									+ s.toString() + " with older data: "
									+ tempPrior.get(s.getParameter(0)).toString());
					}
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
				if (s.getParameters().size() == 3)
				{
					pair.Second += "_" + s.getParameter(2);
				}
				
				if (tempObj.containsKey(pair))
				{
					if (tempObj.get(pair).equals(Helper.asDirection(s.getType())))
						continue; // WARNING: Duplicate
					else
					{
						if (!s.isUserDefined())
							continue;
						else
							throw new ConflictException("Too many statements on "
									+ s.getParameter(0) + " link!");
					}
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
		
		stats.sort((s1, s2) ->
		{
			if (s1.isUserDefined() && !s2.isUserDefined())
				return -1;
			else if (!s1.isUserDefined() && s2.isUserDefined())
				return 1;
			else
				return 0;
		});
		
		// Delete duplicates and reverse duplicates
		for (Statement s : stats)
		{
			// Check normal
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
			case horizontal:
			case vertical:
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
			case unknown:
			default:
				throw new InternalException("This statement should not reach this code: "
						+ st.toString() + "!");
		}
		return false;
	}
	
	public static ArrayList<Statement> transformAssocs(ArrayList<Statement> stats,
			Set<LineAssociation> assocs)
	{
		// Object parent -> [Object childs]
		HashMap<String, HashSet<String>> parents = new HashMap<String, HashSet<String>>();
		
		// TODO
		
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		for (LineAssociation a : assocs)
		{
			switch (a.getType())
			{
				case generalization:
					result.add(new Statement(StatementType.north, StatementLevel.Low, a
							.getFrom(), a.getTo()));
					result.add(new Statement(StatementType.vertical, StatementLevel.High,
							a.getFrom(), a.getTo()));
					break;
				case aggregation:
				case composition:
				case normal:
				default:
					break;
			}
		}
		
		return result;
	}
	
	public static Statement opposite(Statement s)
	{
		return new Statement(opposite(s.getType()), s.getLevel(), s.getParameter(1),
				s.getParameter(0));
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
	
	public static Integer getComplexity(Statement s)
	{
		Integer result = 0;
		
		switch (s.getLevel())
		{
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
		
		switch (s.getType())
		{
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
			default:
				return result + 100;
		}
	}
}
