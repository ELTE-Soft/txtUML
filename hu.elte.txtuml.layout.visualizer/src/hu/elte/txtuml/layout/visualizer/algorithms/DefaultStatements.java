package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.BreathFirstSearch.LabelType;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementLevel;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class generates the necessary default statements to auto layout a
 * diagram.
 * 
 * @author Balázs Gregorics
 *
 */
public class DefaultStatements
{
	
	private ArrayList<Statement> _statements;
	
	/**
	 * Constructor and run command for the generation of default statements.
	 * 
	 * @param os
	 *            Set of objects in the diagram.
	 * @param as
	 *            Set of links in the diagram.
	 * @throws InternalException
	 *             Throws if some internal error is thrown. This indicates
	 *             programming errors.
	 * @throws ConversionException
	 *             Throws if some conversions could not be completed.
	 */
	public DefaultStatements(Set<RectangleObject> os, Set<LineAssociation> as)
			throws InternalException, ConversionException
	{
		_statements = new ArrayList<Statement>();
		
		defaults(os, as);
	}
	
	/**
	 * Returns the list of default statements.
	 * 
	 * @return List of default statements.
	 */
	public ArrayList<Statement> value()
	{
		return _statements;
	}
	
	private void defaults(Set<RectangleObject> os, Set<LineAssociation> as)
			throws InternalException, ConversionException
	{
		// Count degrees
		HashMap<String, Integer> degrees = new HashMap<String, Integer>();
		HashMap<String, HashSet<String>> accesses = new HashMap<String, HashSet<String>>();
		
		for (LineAssociation a : as)
		{
			if (a.isReflexive())
				continue;
			
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
		
		// Detect groups, arrange groups, arrange in groups
		_statements.addAll(detectGroups(degrees, accesses, as));
	}
	
	private Graph<String> buildGraph(HashMap<String, HashSet<String>> edges)
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
	
	private boolean existsItem(ArrayList<HashSet<String>> groups, Integer i,
			Integer gridSize, Direction dir)
	{
		switch (dir)
		{
			case north:
				if (i >= gridSize)
					return true;
				break;
			case east:
				if (i < (groups.size() - 1) && (i % gridSize) < (gridSize - 1))
					return true;
				break;
			case south:
				if (i < (groups.size() - gridSize))
					return true;
				break;
			case west:
				if ((i % gridSize) > 0)
					return true;
				break;
		}
		
		return false;
	}
	
	private Integer getIndex(Integer i, Integer gridSize, StatementType type)
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
			case above:
			case below:
			case horizontal:
			case left:
			case phantom:
			case priority:
			case right:
			case unknown:
			case vertical:
			default:
				throw new InternalException("The type \'" + type.toString()
						+ "\' should not get here!");
		}
	}
	
	private ArrayList<Statement> generateGroupsStatements(
			ArrayList<HashSet<String>> groups, Integer i, Integer gridSize,
			StatementType type) throws InternalException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		Integer index = getIndex(i, gridSize, type);
		for (String move : groups.get(index))
		{
			for (String fix : groups.get(i))
			{
				result.add(new Statement(type, StatementLevel.Medium, move, fix));
			}
		}
		
		return result;
	}
	
	private ArrayList<Statement> detectGroups(HashMap<String, Integer> degrees,
			HashMap<String, HashSet<String>> accesses, Set<LineAssociation> as)
			throws InternalException, ConversionException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		ArrayList<HashSet<String>> groups = new ArrayList<HashSet<String>>();
		
		HashSet<String> openToCheck = (HashSet<String>) Helper.cloneStringSet(accesses
				.keySet());
		Graph<String> G = buildGraph(accesses);
		
		if (openToCheck.size() != 0)
		{
			String start = openToCheck.stream().findFirst().get();
			do
			{
				BreathFirstSearch bfs = new BreathFirstSearch(G, start);
				
				HashSet<String> oneGroup = (HashSet<String>) bfs.value().entrySet()
						.stream()
						.filter(entry -> entry.getValue().equals(LabelType.discovered))
						.map(entry -> entry.getKey()).collect(Collectors.toSet());
				groups.add(oneGroup);
				
				openToCheck.removeAll(oneGroup);
				
				if (openToCheck.size() == 0)
					break;
				
				start = openToCheck.stream().findFirst().get();
				
			} while (true);
		}
		
		// Arrange groups
		Integer gridSize = (int) Math.ceil(Math.sqrt(groups.size()));
		
		for (int i = 0; i < groups.size(); ++i)
		{
			if (existsItem(groups, i, gridSize, Direction.west))
			{
				result.addAll(generateGroupsStatements(groups, i, gridSize,
						StatementType.west));
			}
			if (existsItem(groups, i, gridSize, Direction.east))
			{
				result.addAll(generateGroupsStatements(groups, i, gridSize,
						StatementType.east));
			}
			if (existsItem(groups, i, gridSize, Direction.north))
			{
				result.addAll(generateGroupsStatements(groups, i, gridSize,
						StatementType.north));
			}
			if (existsItem(groups, i, gridSize, Direction.south))
			{
				result.addAll(generateGroupsStatements(groups, i, gridSize,
						StatementType.south));
			}
			
			// Arrange in groups
			// result.addAll(arrangeInGroup(groups.get(i), degrees, accesses,
			// as));
		}
		
		return result;
	}
	
	private ArrayList<Statement> arrangeInGroup(HashSet<String> group,
			HashMap<String, Integer> degrees, HashMap<String, HashSet<String>> accesses,
			Set<LineAssociation> as) throws ConversionException, InternalException
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		// Decide the dimensions of the matrix
		Integer countOfManyDegrees = degrees.entrySet().stream()
				.filter(e -> e.getValue() > 6).collect(Collectors.toSet()).size();
		Integer minGridSizeByDegrees = (int) Math.ceil(Math.sqrt(countOfManyDegrees)) + 2;
		Integer minGridSizeByCount = (int) Math.ceil(Math.sqrt(group.size()));
		
		Integer n = Math.max(minGridSizeByCount, minGridSizeByDegrees);
		String[][] groupPos = new String[n][n];
		
		// Build groupPos matrix
		buildGroupPosMatrix(groupPos, group, degrees, accesses);
		
		// Convert groupPos matrix into weak statements
		result.addAll(convertMatrixIntoStatements(groupPos, as));
		
		// TODO
		// most degree to middle
		// it's association get priority(100)
		// arrange others around, those have access to each other get closer
		
		return result;
	}
	
	private void buildGroupPosMatrix(String[][] groupPos, HashSet<String> group,
			HashMap<String, Integer> degrees, HashMap<String, HashSet<String>> accesses)
			throws InternalException
	{
		HashSet<String> placed = new HashSet<String>();
		String objectToPlace = degrees.entrySet().stream()
				.filter(e -> group.contains(e.getKey())).max((e1, e2) ->
				{
					return Integer.compare(e1.getValue(), e2.getValue());
				}).get().getKey();
		Pair<Integer, Integer> index = indexOfDegree(groupPos, degrees.get(objectToPlace));
		for (int placedCount = 0; placedCount < group.size();)
		{
			groupPos[index.First][index.Second] = objectToPlace;
			placed.add(objectToPlace);
			++placedCount;
			
			HashSet<String> adjacents = (HashSet<String>) accesses.get(objectToPlace)
					.stream().filter(o -> group.contains(o) && !placed.contains(o))
					.collect(Collectors.toSet());
			if (adjacents.size() > 0)
			{
				objectToPlace = degrees
						.entrySet()
						.stream()
						.filter(e -> group.contains(e.getKey())
								&& adjacents.contains(e.getKey())).max((e1, e2) ->
						{
							return Integer.compare(e1.getValue(), e2.getValue());
						}).get().getKey();
				index = indexSomewhereNear(index, groupPos, degrees.get(objectToPlace));
			}
			else
			{
				Optional<Entry<String, Integer>> opt = degrees
						.entrySet()
						.stream()
						.filter(e -> group.contains(e.getKey())
								&& !placed.contains(e.getKey())).max((e1, e2) ->
						{
							return Integer.compare(e1.getValue(), e2.getValue());
						});
				if (opt.isPresent())
				{
					objectToPlace = opt.get().getKey();
					index = indexOfDegree(groupPos, degrees.get(objectToPlace));
				}
			}
		}
	}
	
	private Pair<Integer, Integer> indexSomewhereNear(Pair<Integer, Integer> index,
			String[][] g, Integer d) throws InternalException
	{
		ArrayList<Pair<Integer, Integer>> possibles = possiblePointsInMatrix(g, d);
		
		Pair<Integer, Integer> minval = null;
		Integer mindist = null;
		for (Pair<Integer, Integer> p : possibles)
		{
			if (minval == null)
			{
				minval = p;
				mindist = manhattanDistance(index, p);
			}
			else
			{
				Integer tempdist = manhattanDistance(index, p);
				if (tempdist < mindist)
				{
					minval = p;
					mindist = tempdist;
				}
			}
		}
		
		return minval;
	}
	
	private Integer manhattanDistance(Pair<Integer, Integer> a, Pair<Integer, Integer> b)
	{
		double dx = Math.abs(a.First - b.First);
		double dy = Math.abs(a.Second - b.Second);
		double result = dx + dy;
		return (int) result;
	}
	
	private Pair<Integer, Integer> indexOfDegree(String[][] g, Integer d)
			throws InternalException
	{
		return possiblePointsInMatrix(g, d).stream().findFirst().get();
	}
	
	private ArrayList<Pair<Integer, Integer>> possiblePointsInMatrix(String[][] g,
			Integer d) throws InternalException
	{
		ArrayList<Pair<Integer, Integer>> possibles = new ArrayList<Pair<Integer, Integer>>();
		Integer n = g.length;
		
		if (d <= 3)
		{
			if (g[0][0] == null)
				possibles.add(new Pair<Integer, Integer>(0, 0));
			if (g[0][n - 1] == null)
				possibles.add(new Pair<Integer, Integer>(0, n - 1));
			if (g[n - 1][0] == null)
				possibles.add(new Pair<Integer, Integer>(n - 1, 0));
			if (g[n - 1][n - 1] == null)
				possibles.add(new Pair<Integer, Integer>(n - 1, n - 1));
		}
		
		if (possibles.size() == 0 || d > 3)
		{
			for (int i = 0; i < n; ++i)
			{
				for (int j = 0; j < n; ++j)
				{
					if ((i == 0 && j == 0) || (i == 0 && j == n - 1)
							|| (i == n - 1 && j == 0) || (i == n - 1 && j == n - 1)
							|| (i > 0 && j > 0 && i < n - 1 && j < n - 1))
						continue;
					
					if (g[i][j] == null)
						possibles.add(new Pair<Integer, Integer>(i, j));
				}
			}
		}
		
		if (possibles.size() == 0 || d > 5)
		{
			for (int i = 1; i < n - 1; ++i)
			{
				for (int j = 1; j < n - 1; ++j)
				{
					if (g[i][j] == null)
						possibles.add(new Pair<Integer, Integer>(i, j));
				}
			}
		}
		
		if (possibles.size() == 0)
			throw new InternalException("Too few possible locations!");
		
		return possibles;
	}
	
	private ArrayList<Statement> convertMatrixIntoStatements(String[][] g,
			Set<LineAssociation> as)
	{
		ArrayList<Statement> result = new ArrayList<Statement>();
		
		for (int i = 0; i < g.length; ++i)
		{
			for (int j = 0; j < g.length; ++j)
			{
				if (g[i][j] == null)
					continue;
				
				// Above
				if (i > 0 && g[i - 1][j] != null)
				{
					result.add(new Statement(StatementType.below, StatementLevel.High,
							g[i][j], g[i - 1][j]));
					result.add(new Statement(StatementType.south, StatementLevel.High,
							g[i][j], g[i - 1][j]));
				}
				
				// Right
				if (j < (g.length - 1) && g[i][j + 1] != null)
				{
					result.add(new Statement(StatementType.left, StatementLevel.High,
							g[i][j], g[i][j + 1]));
					result.add(new Statement(StatementType.west, StatementLevel.High,
							g[i][j], g[i][j + 1]));
				}
				
				// Below
				if (i < (g.length - 1) && g[i + 1][j] != null)
				{
					result.add(new Statement(StatementType.above, StatementLevel.High,
							g[i][j], g[i + 1][j]));
					result.add(new Statement(StatementType.north, StatementLevel.High,
							g[i][j], g[i + 1][j]));
				}
				
				// Left
				if (j > 0 && g[i][j - 1] != null)
				{
					result.add(new Statement(StatementType.right, StatementLevel.High,
							g[i][j], g[i][j - 1]));
					result.add(new Statement(StatementType.east, StatementLevel.High,
							g[i][j], g[i][j - 1]));
				}
			}
		}
		
		return result;
	}
}
