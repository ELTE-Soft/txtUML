package hu.elte.txtuml.layout.visualizer.algorithms.boxes;

import hu.elte.txtuml.layout.visualizer.algorithms.boxes.bellmanfordhelpers.DirectedEdge;
import hu.elte.txtuml.layout.visualizer.algorithms.boxes.bellmanfordhelpers.EdgeWeightedDigraph;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementLevel;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.events.ProgressManager;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxOverlapConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.helpers.BiMap;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Quadraple;
import hu.elte.txtuml.layout.visualizer.helpers.StatementHelper;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.OverlapArrangeMode;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class arranges the boxes in the diagram using mainly a Bellman-Ford
 * shortest path algorithm.
 * 
 * @author Balázs Gregorics
 *
 */
public class ArrangeObjects
{
	private Set<RectangleObject> _objects;
	private List<Statement> _statements;
	private BiMap<String, Integer> _indices;
	private Integer _gid;
	private Boolean _logging;
	private OverlapArrangeMode _arrangeOverlaps;
	
	private Integer _transformAmount;
	
	/**
	 * Getter for the transform amount.
	 * 
	 * @return the transform amount.
	 */
	public Integer getTransformAmount()
	{
		return _transformAmount;
	}
	
	/**
	 * Setter for the transform amount.
	 * 
	 * @param value
	 *            the transform amount.
	 */
	public void setTransformAmount(Integer value)
	{
		_transformAmount = value;
	}
	
	/**
	 * Class that arranges the boxes on the diagram.
	 * 
	 * @param obj
	 *            Set of objects/boxes to arrange.
	 * @param par_stats
	 *            List of statements on the boxes.
	 * @param gid
	 *            The previously used group id.
	 * @param log
	 *            Whether to print out logging msgs.
	 * @param arrangeoverlaps
	 *            Whether to arrange overlapping elements.
	 * @throws BoxArrangeConflictException
	 *             Throws when a conflict appears during the arrangement of the
	 *             boxes.
	 * @throws InternalException
	 *             Throws if some algorithm related error happens. Contact
	 *             programmer for more details.
	 * @throws ConversionException
	 *             Throws if the algorithm could not convert a specific value.
	 * @throws BoxOverlapConflictException
	 *             Throws if the algorithm encounters an unsolvable overlap
	 *             during the arrangement of the boxes.
	 */
	public ArrangeObjects(Set<RectangleObject> obj, List<Statement> par_stats,
			Integer gid, Boolean log, OverlapArrangeMode arrangeoverlaps)
			throws BoxArrangeConflictException, InternalException, ConversionException,
			BoxOverlapConflictException
	
	{
		// Nothing to arrange
		if (obj == null || obj.size() == 0)
			return;
		
		_logging = log;
		_arrangeOverlaps = arrangeoverlaps;
		_gid = gid;
		
		_statements = Helper.cloneStatementList(par_stats);
		_objects = new HashSet<RectangleObject>(obj);
		_transformAmount = 1;
		
		// Emit Event
		ProgressManager.getEmitter().OnBoxArrangeStart();
		
		// Arrange, arrange overlaps
		setIndices();
		arrangeUntilNotConflicted();
		
		if (isThereOverlapping())
			arrangeOverlaps();
	}
	
	private void arrangeUntilNotConflicted() throws InternalException,
			BoxArrangeConflictException
	{
		Boolean isConflicted = false;
		do
		{
			isConflicted = false;
			try
			{
				arrange(_statements);
			}
			catch (BoxArrangeConflictException ex)
			{
				isConflicted = true;
				// Remove a weak statement if possible
				if (ex.ConflictStatements.stream().anyMatch(s -> !s.isUserDefined()))
				{
					existsAddedConflictingStatement(ex.ConflictStatements);
				}
				else if (_statements.stream().anyMatch(s -> !s.isUserDefined()))
				{
					existsAddedStatement();
				}
				else
					throw ex;
				
				if (_logging)
					System.err.println("> > ReTrying box arrange!");
			}
		} while (isConflicted);
	}
	
	private void existsAddedConflictingStatement(List<Statement> conflicts)
	{
		ArrayList<Statement> possibleDeletes = (ArrayList<Statement>) conflicts.stream()
				.filter(s -> !s.isUserDefined()).collect(Collectors.toList());
		Statement max = possibleDeletes
				.stream()
				.max((s1, s2) ->
				{
					return -1
							* Integer.compare(StatementHelper.getComplexity(s1),
									StatementHelper.getComplexity(s2));
				}).get();
		
		ArrayList<Statement> toDeletes = new ArrayList<Statement>();
		toDeletes.addAll(_statements
				.stream()
				.filter(s -> s.getGroupId() != null
						&& s.getGroupId().equals(max.getGroupId()))
				.collect(Collectors.toList()));
		
		_statements.removeAll(toDeletes);
		if (_logging)
		{
			for (Statement stat : toDeletes)
			{
				System.err
						.println("> > Weak(" + stat.toString() + ") statement deleted!");
			}
		}
	}
	
	private void existsAddedStatement()
	{
		ArrayList<Statement> possibleDeletes = (ArrayList<Statement>) _statements
				.stream().filter(s -> !s.isUserDefined()).collect(Collectors.toList());
		Statement max = possibleDeletes
				.stream()
				.max((s1, s2) ->
				{
					return -1
							* Integer.compare(StatementHelper.getComplexity(s1),
									StatementHelper.getComplexity(s2));
				}).get();
		
		ArrayList<Statement> toDeletes = new ArrayList<Statement>();
		toDeletes.addAll(_statements
				.stream()
				.filter(s -> s.getGroupId() != null
						&& s.getGroupId().equals(max.getGroupId()))
				.collect(Collectors.toList()));
		
		_statements.removeAll(toDeletes);
		if (_logging.equals(true))
		{
			for (Statement stat : toDeletes)
			{
				System.err
						.println("> > Weak(" + stat.toString() + ") statement deleted!");
			}
		}
	}
	
	private void arrangeOverlaps() throws ConversionException, InternalException,
			BoxOverlapConflictException
	{
		if (_logging)
			System.err.println("Starting overlap arrange...");
		
		// Emit Event
		ProgressManager.getEmitter().OnBoxOverlapArrangeStart();
		
		switch (_arrangeOverlaps)
		{
			case none:
				// Nothing to do
				break;
			case one:
				removeOverlapsWithOne();
				break;
			case few:
				removeOverlapsWithFew();
				break;
			case limited:
				// removeOverlaps(100);
				throw new InternalException("Not Implemented Exception!");
				// break;
			case full:
				// removeOverlaps(-1);
				throw new InternalException("Not Implemented Exception!");
				// break;
		}
		
		if (_logging)
			System.err.println("Ending overlap arrange...");
		
		// Emit Event
		ProgressManager.getEmitter().OnBoxOverlapArrangeEnd();
	}
	
	private void removeOverlapsWithOne() throws ConversionException, InternalException,
			BoxOverlapConflictException
	{
		// Fixes the layout of the diagram by giving statements preserving the
		// current state
		fixCurrentState();
		
		for (Entry<Point, HashSet<String>> entry : overlaps().entrySet())
		{
			if (entry.getValue().size() > 1)
			{
				Integer matrixSize = (int) Math.ceil(Math.sqrt(entry.getValue().size()));
				String[][] matrix = new String[matrixSize][matrixSize];
				
				Integer nextI = 0;
				Integer nextJ = 0;
				
				for (String name : entry.getValue())
				{
					matrix[nextI][nextJ] = name;
					++nextJ;
					
					if (nextJ >= matrixSize)
					{
						++nextI;
						nextJ = 0;
					}
				}
				
				makeStatementsForMatrix(matrix, matrixSize);
			}
		}
		
		try
		{
			arrange(_statements);
		}
		catch (BoxArrangeConflictException e)
		{
			throw new BoxOverlapConflictException(overlaps().entrySet().stream()
					.findFirst().get().getValue().stream().collect(Collectors.toList()));
		}
		
	}
	
	private void makeStatementsForMatrix(String[][] matrix, Integer size)
			throws InternalException
	{
		++_gid;
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				if (matrix[i][j] == null)
					continue;
				
				if (i > 0)
				{
					_statements.add(new Statement(StatementType.south,
							StatementLevel.Medium, _gid, matrix[i][j], matrix[i - 1][j]));
				}
				if (j > 0)
				{
					_statements.add(new Statement(StatementType.east,
							StatementLevel.Medium, _gid, matrix[i][j], matrix[i][j - 1]));
				}
			}
		}
	}
	
	private void fixCurrentState() throws ConversionException, InternalException
	{
		for (RectangleObject o1 : _objects)
		{
			for (RectangleObject o2 : _objects)
			{
				if (o1.equals(o2) || o1.getPosition().equals(o2.getPosition()))
					continue;
				
				for (Direction dir : Point.directionOfAll(o1.getPosition(),
						o2.getPosition()))
				{
					Statement s = new Statement(Helper.asStatementType(dir),
							StatementLevel.Low, _gid, o1.getName(), o2.getName());
					if (!_statements.contains(s))
						_statements.add(s);
				}
			}
		}
	}
	
	private void removeOverlapsWithFew() throws InternalException, ConversionException,
			BoxOverlapConflictException
	{
		List<RectangleObject> SortedObjects = new ArrayList<RectangleObject>(_objects);
		SortedObjects.sort((box1, box2) ->
		{
			return box1.getName().compareTo(box2.getName());
		});
		
		// Fixes the layout of the diagram by giving statements preserving the
		// current state
		fixCurrentState();
		
		Boolean wasExtension = false;
		do
		{
			wasExtension = false;
			if (_logging)
				System.err.println(" > [FEW] Round of overlap arrange...");
			
			// for every overlapping box pairs
			for (RectangleObject boxA : SortedObjects)
			{
				if (!isThereOverlapping())
					break;
				
				for (RectangleObject boxB : SortedObjects)
				{
					if (!isThereOverlapping())
						break;
					
					if (boxA.getName().equals(boxB.getName()))
						continue;
					
					if (boxA.getPosition().equals(boxB.getPosition()))
					{
						// try add a statement to resolve overlap
						wasExtension = wasExtension || tryAddStatement(boxA, boxB);
					}
				}
			}
		} while (wasExtension);
		
		// If the cycle stopped but there remained overlapped elements
		if (isThereOverlapping())
		{
			throw new BoxOverlapConflictException(overlaps().entrySet().stream()
					.map(e -> e.getValue()).max((x, y) ->
					{
						return Integer.compare(x.size(), y.size());
					}).get().stream().collect(Collectors.toList()));
		}
		
	}
	
	private Boolean tryAddStatement(RectangleObject boxA, RectangleObject boxB)
			throws InternalException, ConversionException
	{
		Boolean wasExtension = false;
		
		for (Direction dir : Direction.values())
		{
			Integer prevOverlapCount = overlappingCount();
			if (!isThereOverlapping(prevOverlapCount))
				break;
			
			List<Statement> newStats = Helper.cloneStatementList(_statements);
			++_gid;
			newStats.add(new Statement(Helper.asStatementType(dir),
					StatementLevel.Medium, _gid, boxA.getName(), boxB.getName()));
			
			try
			{
				arrange(newStats);
				Integer currOverlapCount = overlappingCount();
				if (currOverlapCount < prevOverlapCount)
				{
					_statements = Helper.cloneStatementList(newStats);
					wasExtension = true;
					
					if (_logging)
						System.err.println("[FEW] Found a possible solution "
								+ currOverlapCount + "<" + prevOverlapCount + "!");
				}
				else
					--_gid;
			}
			catch (MyException ex)
			{
				--_gid;
				if (_logging)
					System.err.println("[FEW] Retrying to resolve overlaps...");
			}
		}
		
		return wasExtension;
	}
	
	private void arrange(List<Statement> stats) throws InternalException,
			BoxArrangeConflictException
	{
		int n = _objects.size();
		int startNode = 0;
		ArrayList<Quadraple<Integer, Integer, Integer, Statement>> l = buildEdges(n,
				stats);
		
		EdgeWeightedDigraph G = new EdgeWeightedDigraph((2 * n) + 1, l);
		
		BellmanFordSP sp = new BellmanFordSP(G, startNode);
		
		if (sp.hasNegativeCycle())
		{
			// Nincs megoldás, ellentmondás
			ArrayList<Statement> excparam = new ArrayList<Statement>();
			for (DirectedEdge e : sp.negativeCycle())
			{
				excparam.add(e.stat());
			}
			throw new BoxArrangeConflictException(excparam,
					"There were conflicts in the statements (cyclic, unsolvable)!");
		}
		else
		{
			for (int v = 1; v < G.V(); v++)
			{
				if (sp.hasPathTo(v))
				{
					String nameOfTheObject;
					if (v > n)
					{
						// set Y coordinate
						nameOfTheObject = _indices.getKey(v - n);
						
						RectangleObject mod = _objects.stream()
								.filter(o -> o.getName().equals(nameOfTheObject))
								.findFirst().get();
						mod.setPosition(new Point(mod.getPosition().getX(), (int) sp
								.distTo(v)));
					}
					else
					{
						// set X coordinate
						nameOfTheObject = _indices.getKey(v);
						
						RectangleObject mod = _objects.stream()
								.filter(o -> o.getName().equals(nameOfTheObject))
								.findFirst().get();
						mod.setPosition(new Point((int) sp.distTo(v), mod.getPosition()
								.getY()));
					}
				}
				else
				{
					throw new InternalException(
							"Certain elements are not connected to the others!");
				}
			}
		}
	}
	
	private void setIndices()
	{
		_indices = new BiMap<String, Integer>();
		// Set indices
		{
			Integer index = 1;
			for (RectangleObject o : _objects)
			{
				_indices.put(o.getName(), index);
				++index;
			}
		}
	}
	
	private boolean isThereOverlapping()
	{
		return isThereOverlapping(overlappingCount());
	}
	
	private boolean isThereOverlapping(Integer oc)
	{
		return oc > 1;
	}
	
	private HashMap<Point, HashSet<String>> overlaps()
	{
		HashMap<Point, HashSet<String>> overlaps = new HashMap<Point, HashSet<String>>();
		
		for (RectangleObject o : _objects)
		{
			if (overlaps.containsKey(o.getPosition()))
			{
				HashSet<String> temp = overlaps.get(o.getPosition());
				temp.add(o.getName());
				
				overlaps.put(o.getPosition(), temp);
			}
			else
			{
				HashSet<String> temp = new HashSet<String>();
				temp.add(o.getName());
				overlaps.put(o.getPosition(), temp);
			}
		}
		
		return overlaps;
	}
	
	private Integer overlappingCount()
	{
		Integer count = 0;
		for (Entry<Point, HashSet<String>> entry : overlaps().entrySet())
		{
			if (entry.getValue().size() > 1)
			{
				count += entry.getValue().size();
			}
		}
		
		return count;
	}
	
	private ArrayList<Quadraple<Integer, Integer, Integer, Statement>> buildEdges(
			Integer n, List<Statement> stats) throws InternalException
	{
		ArrayList<Quadraple<Integer, Integer, Integer, Statement>> result = new ArrayList<Quadraple<Integer, Integer, Integer, Statement>>();
		
		// Set the edges from node 0 to ALL
		for (int i = 0; i < (2 * n) + 1; ++i)
		{
			result.add(new Quadraple<Integer, Integer, Integer, Statement>(0, i, 0, null));
		}
		
		// Set edges based on statement constraints
		for (Statement s : stats)
		{
			switch (s.getType())
			{
				case north:
				{
					int i = _indices.get(s.getParameter(0)) + n;
					int j = _indices.get(s.getParameter(1)) + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							-1, s));
				}
					break;
				case south:
				{
					int i = _indices.get(s.getParameter(1)) + n;
					int j = _indices.get(s.getParameter(0)) + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							-1, s));
				}
					break;
				case east:
				{
					int i = _indices.get(s.getParameter(0));
					int j = _indices.get(s.getParameter(1));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							-1, s));
				}
					break;
				case west:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							-1, s));
				}
					break;
				case vertical:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							0, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							0, s));
				}
					break;
				case horizontal:
				{
					int i = _indices.get(s.getParameter(1)) + n;
					int j = _indices.get(s.getParameter(0)) + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							0, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							0, s));
				}
					break;
				case above:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							0, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							0, s));
					i = i + n;
					j = j + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							1, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							-1, s));
				}
					break;
				case below:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							0, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							0, s));
					i = i + n;
					j = j + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							-1, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							1, s));
				}
					break;
				case right:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							1, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							-1, s));
					i = i + n;
					j = j + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							0, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							0, s));
				}
					break;
				case left:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							-1, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							1, s));
					i = i + n;
					j = j + n;
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(i, j,
							0, s));
					result.add(new Quadraple<Integer, Integer, Integer, Statement>(j, i,
							0, s));
				}
					break;
				case phantom:
				case priority:
				case unknown:
				default:
					throw new InternalException(
							"This statement should not reach this point: " + s.toString());
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the value of the arrangement.
	 * 
	 * @return the value of the arrangement.
	 */
	public Set<RectangleObject> value()
	{
		return _objects;
	}
	
	/**
	 * Returns the modified {@link Statement} list.
	 * 
	 * @return the modified {@link Statement} list.
	 */
	public List<Statement> statements()
	{
		return _statements;
	}
	
	/**
	 * Returns the latest used Group Id number.
	 * 
	 * @return the latest used Group Id number.
	 */
	public Integer getGId()
	{
		return _gid;
	}
	
}
