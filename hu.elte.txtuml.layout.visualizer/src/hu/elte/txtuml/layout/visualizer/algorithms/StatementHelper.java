package hu.elte.txtuml.layout.visualizer.algorithms;

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
		
		// Delete duplicates
		for (Statement s : stats)
		{
			if (result.contains(s))
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
	
	public static StatementType Opposite(StatementType st)
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
	
}
