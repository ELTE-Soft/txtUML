package hu.elte.txtuml.layout.visualizer.annotations;

import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Statement
{
	// Variables
	
	private StatementType _type;
	private ArrayList<String> _parameters;
	
	// end Variables
	
	// Getters, setters
	
	public StatementType getType()
	{
		return _type;
	}
	
	public ArrayList<String> getParameters()
	{
		return _parameters;
	}
	
	public String getParameter(Integer i)
	{
		return _parameters.get(i);
	}
	
	// end Getters, setters
	
	// Ctors
	
	public Statement()
	{
		_type = StatementType.unknown;
		_parameters = new ArrayList<String>();
	}
	
	public Statement(StatementType t, String... params)
	{
		_type = t;
		_parameters = new ArrayList<String>();
		for (String s : params)
		{
			_parameters.add(s);
		}
	}
	
	public Statement(StatementType t, ArrayList<String> params)
	{
		_type = t;
		_parameters = new ArrayList<String>();
		for (String s : params)
		{
			_parameters.add(s);
		}
	}
	
	// end Ctors
	
	// Statics
	
	public static Statement Parse(String input) throws MyException
	{
		StatementType type = StatementType.unknown;
		String[] params;
		
		input = input.replaceAll(" ", "");
		
		Integer parOpen = input.indexOf("(");
		Integer parClose = input.lastIndexOf(")");
		
		if (parOpen == -1 || parClose == -1)
			throw new MyException("No (not enough) parentheisis found!");
		
		try
		{
			type = Enum.valueOf(StatementType.class, input.substring(0, parOpen)
					.toLowerCase());
		}
		catch (IllegalArgumentException e)
		{
			throw new UnknownStatementException("No known statement such as: "
					+ input.substring(0, parOpen) + "!");
		}
		
		params = input.substring(parOpen + 1, parClose).split(",");
		
		if (!enoughParametersForType(type, params))
			throw new MyException("Not enough / Too many parameters for type!");
		
		return new Statement(type, params);
	}
	
	private static boolean enoughParametersForType(StatementType t, String[] p)
	{
		switch (t)
		{
			case north:
			case south:
			case east:
			case west:
				if (p.length == 2 || p.length == 3)
					return true;
				break;
			case above:
			case below:
			case right:
			case left:
			case group:
			case layout:
			case priority:
				if (p.length == 2)
					return true;
				break;
			case topmost:
			case bottommost:
			case rightmost:
			case leftmost:
				if (p.length == 1)
					return true;
				break;
			default:
				break;
		}
		return false;
	}
	
	public static boolean Equals(Statement s1, Statement s2)
	{
		return s1.equals(s2);
	}
	
	public static ArrayList<Statement> UnfoldGroups(ArrayList<Statement> stats)
			throws UnknownStatementException
	{
		// Minden groupnévhez List of object/más név
		HashMap<String, ArrayList<String>> groups = new HashMap<String, ArrayList<String>>();
		
		for (Statement s : stats)
		{
			if (s.getType().equals(StatementType.group))
			{
				if (groups.containsKey(s.getParameter(1)))
				{
					if (!groups.get(s.getParameter(1)).contains(s.getParameter(0)))
					{
						ArrayList<String> templist = groups.get(s.getParameter(1));
						templist.add(s.getParameter(0));
						groups.put(s.getParameter(1), templist);
					}
				}
				else
				{
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(s.getParameter(0));
					groups.put(s.getParameter(1), templist);
				}
			}
		}
		
		// Recursive group definition unfold
		boolean wasmodification = true;
		while (wasmodification)
		{
			wasmodification = false;
			HashMap<String, ArrayList<String>> groupsM = new HashMap<String, ArrayList<String>>();
			groupsM = Helper.cloneMap(groups);
			for (String group : groupsM.keySet())
			{
				ArrayList<String> listToIterate = groupsM.get(group);
				for (String name : listToIterate)
				{
					if (groupsM.keySet().contains(name))
					{
						groups.get(group).remove(name);
						groups.get(group).addAll(groups.get(name));
						wasmodification = true;
					}
				}
			}
		}
		
		// Remove groups
		stats.removeIf(s -> s.getType().equals(StatementType.group));
		
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		// Remove layouts
		for (Statement s : stats.stream()
				.filter(st -> st.getType().equals(StatementType.layout))
				.collect(Collectors.toList()))
		{
			LayoutType lt = Enum.valueOf(LayoutType.class, s.getParameter(1)
					.toLowerCase());
			result.addAll(getStatementsFromLayout(lt, groups.get(s.getParameter(0))));
		}
		
		stats.removeIf(s -> s.getType().equals(StatementType.layout));
		
		// Remove group names
		for (Statement s : stats)
		{
			// First parameter
			if (groups.containsKey(s.getParameter(0)))
			{
				for (String name : groups.get(s.getParameter(0)))
				{
					result.add(new Statement(s.getType(), name, s.getParameter(1)));
				}
				continue;
			}
			// Second parameter
			if (s.getParameters().size() >= 2 && groups.containsKey(s.getParameter(1)))
			{
				for (String name : groups.get(s.getParameter(1)))
				{
					result.add(new Statement(s.getType(), s.getParameter(0), name));
				}
				continue;
			}
			result.add(s);
		}
		
		return result;
	}
	
	private static ArrayList<Statement> getStatementsFromLayout(LayoutType lt,
			ArrayList<String> groupMembers) throws UnknownStatementException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		StatementType typeToAdd;
		if (lt.equals(LayoutType.toptobottom))
			typeToAdd = StatementType.north;
		else if (lt.equals(LayoutType.bottomtotop))
			typeToAdd = StatementType.south;
		else if (lt.equals(LayoutType.lefttoright))
			typeToAdd = StatementType.west;
		else if (lt.equals(LayoutType.righttoleft))
			typeToAdd = StatementType.east;
		else
			throw new UnknownStatementException("Unkown layout pattern (" + lt.toString()
					+ ")!");
		
		for (int i = 1; i < groupMembers.size(); ++i)
		{
			Statement s = new Statement(typeToAdd, groupMembers.get(i - 1),
					groupMembers.get(i));
			result.add(s);
		}
		
		return result;
	}
	
	public static ArrayList<Statement> SplitAssocs(ArrayList<Statement> stats,
			HashSet<LineAssociation> assocs)
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
	
	private static boolean isAssocParams(ArrayList<String> p, HashSet<LineAssociation> as)
	{
		// !
		return as.stream().anyMatch(a -> a.getId().equals(p.get(0)));
	}
	
	public static ArrayList<Statement> ReduceAssocs(ArrayList<Statement> stats)
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
	
	public static ArrayList<Statement> ReduceObjects(ArrayList<Statement> stats,
			HashSet<RectangleObject> objs)
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		ArrayList<Statement> tops = new ArrayList<Statement>();
		ArrayList<Statement> bottoms = new ArrayList<Statement>();
		ArrayList<Statement> rights = new ArrayList<Statement>();
		ArrayList<Statement> lefts = new ArrayList<Statement>();
		
		for (Statement s : stats)
		{
			if (s.getType().equals(StatementType.topmost))
				tops.add(s);
			else if (s.getType().equals(StatementType.bottommost))
				bottoms.add(s);
			else if (s.getType().equals(StatementType.rightmost))
				rights.add(s);
			else if (s.getType().equals(StatementType.leftmost))
				lefts.add(s);
			else
			{
				result.add(s);
			}
		}
		
		// Replace Mosts
		result.addAll(GenerateMostStatements(tops, StatementType.north, objs));
		result.addAll(GenerateMostStatements(bottoms, StatementType.south, objs));
		result.addAll(GenerateMostStatements(rights, StatementType.east, objs));
		result.addAll(GenerateMostStatements(lefts, StatementType.west, objs));
		
		// Delete duplicates
		ArrayList<Statement> temp = result;
		result = new ArrayList<Statement>();
		for (Statement s : temp)
		{
			if (result.contains(s))
				continue;
			else
				result.add(s);
		}
		
		return result;
	}
	
	private static ArrayList<Statement> GenerateMostStatements(
			ArrayList<Statement> mosts, StatementType replacement,
			HashSet<RectangleObject> objs)
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		HashSet<String> objectNames = (HashSet<String>) objs.stream()
				.map(ro -> ro.getName()).collect(Collectors.toSet());
		objectNames.removeAll((HashSet<String>) mosts.stream()
				.map(s -> s.getParameter(0)).collect(Collectors.toSet()));
		
		for (Statement s : mosts)
		{
			for (String name : objectNames)
			{
				
				Statement temp = new Statement(replacement, s.getParameter(0), name);
				result.add(temp);
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unused")
	private static boolean Opposites(Statement s1, Statement s2)
	{
		if (Helper.Opposite(s1.getType()).equals(s2.getType())
				&& s1.getParameters().size() == s2.getParameters().size())
		{
			boolean all = true;
			
			int size = s1.getParameters().size();
			for (int i = 0; i < size; ++i)
			{
				all = all && (s1.getParameter(i).equals(s2.getParameter(size - i - 1)));
			}
			
			return all;
		}
		
		return false;
	}
	
	public static ArrayList<Statement> TransformAssocs(ArrayList<Statement> stats,
			HashSet<LineAssociation> assocs)
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
	
	public static boolean IsTypeChecked(Statement st, HashSet<RectangleObject> ob,
			HashSet<LineAssociation> as) throws InternalException
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
				if (as.stream().anyMatch(a -> a.getId().equals(st.getParameter(1)))
						&& Helper.tryParseInt(st.getParameter(1)))
					return true;
				break;
			default:
				throw new InternalException("This statement should not reach this code: "
						+ st.toString() + "!");
		}
		return false;
	}
	
	// end Statics
	
	// Methods
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
		if (this instanceof Statement && obj instanceof Statement)
		{
			Statement s1 = (Statement) this;
			Statement s2 = (Statement) obj;
			return s1._type.equals(s2._type) && s1._parameters.equals(s2._parameters);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _type.hashCode();
		result = prime * result + _parameters.hashCode();
		return result;
	}
	
	public String toString()
	{
		String result = _type.toString() + "(";
		for (String p : _parameters)
		{
			if (!result.substring(result.length() - 1, result.length()).equals("("))
				result += ", ";
			result += p;
		}
		result += ")";
		return result;
	}
	
	// end Methods
	
}
