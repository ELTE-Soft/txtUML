package hu.elte.txtuml.layout.visualizer.algorithms.links;

import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Boundary;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Cost;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Node;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Painted;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Painted.Colors;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Parent;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class implements an A^* graph search algorithm.
 * 
 * @author Balázs Gregorics
 */
class GraphSearch
{
	
	// Constants
	
	private final Double _weightLength = 1.0; // 2.9
	private final Double _weightTurns = 3.0; // 2.7
	private final Double _weightCrossing = 3.0; // 2.0
	private final Integer _penalizeTurns = 2;
	
	// end Constants
	
	// Variables
	
	private Node _end;
	private Set<Node> _endSet;
	private Set<Painted<Point>> _objects;
	private Boundary _boundary;
	private Integer _extends;
	
	private Graph<Node> G;
	private PriorityQueue<Node> AvailableNodes;
	private Cost<Node> g;
	private Parent<Node> PI;
	
	private HashMap<Node, Double> _heuristic;
	
	// end Variables
	
	// Ctors
	
	/**
	 * Creates a graph to find a route in it from start to end. Route length is
	 * minimal. Route cornering is minimal.
	 * 
	 * @param ss
	 *            Set of start Nodes.
	 * @param es
	 *            Set of end Nodes to reach.
	 * @param os
	 *            Nodes we have to skip (already occupied).
	 * @param bounds
	 *            Bounds of the maximum width of the graph to search in.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if there is no route from start->end.
	 * @throws CannotStartAssociationRouteException
	 *             Throws if the alogirthm cannot even start the route from
	 *             start.
	 * @throws ConversionException
	 *             Throws if the algorithm cannot convert a {@link Point} to a
	 *             {@link Direction} or vica versa.
	 * @throws InternalException
	 *             Throws if the algorithm encounters something which it should
	 *             not have.
	 */
	public GraphSearch(Set<Node> ss, Set<Node> es, Set<Painted<Point>> os,
			Boundary bounds) throws CannotFindAssociationRouteException,
			CannotStartAssociationRouteException, ConversionException,
			InternalException
	{
		_end = new Node(new Point(), new Point());
		_endSet = new HashSet<Node>(es);
		_objects = os;
		_boundary = bounds;
		_extends = 0;
		_heuristic = new HashMap<Node, Double>();
		
		G = new Graph<Node>();
		
		AvailableNodes = new PriorityQueue<Node>((x, y) -> nodeComparator(x, y));
		
		g = new Cost<Node>();
		
		PI = new Parent<Node>();
		
		// Extend StartSet
		for (Node p : ss/* _startSet */)
		{
			PI.set(p, null);
			g.set(p, 2 * _weightLength);
			AvailableNodes.add(p);
		}
		
		if (!search())
		{
			throw new CannotFindAssociationRouteException(
					"No Route from START to END!");
		}
	}
	
	// end Ctors
	
	// Methods
	
	private boolean search()
	{
		while (true)
		{
			if (AvailableNodes.isEmpty())
				return false;
			
			Node n = AvailableNodes.poll();
			
			if (_endSet.stream().anyMatch(node -> node.getFrom()
					.equals(n.getTo())))
			{
				Node e = _endSet.stream()
						.filter(node -> node.getFrom().equals(n.getTo()))
						.findFirst().get();
				PI.set(e, n);
				PI.set(_end, e);
				return true;
			}
			
			++_extends;
			for (Pair<Node, Double> pair : Gamma(n))
			{
				Node m = pair.First;
				Double newCost = (g.get(n) + pair.Second);
				if (!G.contains(m) || newCost < g.get(m))
				{
					PI.set(m, n);
					g.set(m, newCost);
					AvailableNodes.add(m);
				}
				G.addNode(m);
				G.addLink(new Link<Node>(n, m));
			}
		}
	}
	
	private Integer nodeComparator(Node a, Node b)
	{
		return Double.compare(f(a), f(b));
	}
	
	@SuppressWarnings("unused")
	private Node minf()
	{
		Node min = null;
		Double minval = Double.MAX_VALUE;
		
		for (Node p : AvailableNodes)
		{
			Double f = f(p);
			if (f < minval)
			{
				minval = f;
				min = p;
			}
		}
		
		return min;
	}
	
	// Kiértékelõ függvény
	private Double f(Node p)
	{
		Double cost = g.get(p);
		Double heuristic = h(p);
		
		return cost + heuristic;
	}
	
	private Double h(Node p)
	{
		if (_heuristic.containsKey(p))
			return _heuristic.get(p);
		
		Pair<Double, Node> distance = manhattanDistance(p);
		Double remainingTurns = manhattanLeastTurns(p);
		// manhattanLeastTurnsCheckingOccupied(p, distance.Second);
		Double result = (_weightTurns * remainingTurns + _weightLength
				* distance.First);
		
		_heuristic.put(p, result);
		
		return result;
	}
	
	// Metrics
	
	private Pair<Double, Node> manhattanDistance(Node a)
	{
		Pair<Integer, Node> result = _endSet.stream().map(p ->
		{
			Integer dx = Math.abs(a.getTo().getX() - p.getTo().getX());
			Integer dy = Math.abs(a.getTo().getY() - p.getTo().getY());
			Integer tempResult = dx + dy;
			
			return new Pair<Integer, Node>(tempResult, p);
		}).min((p1, p2) ->
		{
			return Integer.compare(p1.First, p2.First);
		}).get();
		
		return new Pair<Double, Node>((double) result.First, result.Second);
	}
	
	private Double manhattanLeastTurns(Node a)
	{
		Node closest = _endSet.stream().map(p ->
		{
			Integer dx = Math.abs(a.getTo().getX() - p.getTo().getX());
			Integer dy = Math.abs(a.getTo().getY() - p.getTo().getY());
			Integer tempResult = dx + dy;
			
			return new Pair<Node, Integer>(p, tempResult);
		}).min((d1, d2) ->
		{
			return Integer.compare(d1.Second, d2.Second);
		}).get().First;
		
		if (!closest.getTo().getX().equals(a.getTo().getX())
				&& !closest.getTo().getY().equals(a.getTo().getY()))
		{
			return 1.0;
		}
		
		return 0.0;
	}
	
	@SuppressWarnings("unused")
	private Double manhattanLeastTurnsCheckingOccupied(Node from, Node to)
	{
		Integer min = Integer.MAX_VALUE;
		
		Point ending = to.getTo();
		double dx = Math.abs(from.getTo().getX() - ending.getX());
		double sx = ending.getX() - from.getTo().getX();
		double dy = Math.abs(from.getTo().getY() - ending.getY());
		double sy = ending.getY() - from.getTo().getY();
		Integer resultF = 0;
		Integer resultB = 0;
		
		if (dx > 0 && dy > 0)
		{
			++resultF;
			++resultB;
		}
		
		// elejérõl és végérõl nézzük egyszerre a két külön utat
		// X tengely mentén
		Point tempF = new Point(from.getTo());
		Point tempB = new Point(ending);
		for (int i = 0; i < dx; ++i)
		{
			if (sx > 0)
			{
				tempF = Point.Add(tempF, Direction.east);
				tempB = Point.Add(tempF, Direction.west);
			}
			else if (sx < 0)
			{
				tempF = Point.Add(tempF, Direction.west);
				tempB = Point.Add(tempF, Direction.east);
			}
			
			// tempF nem a vége és nem piros pont
			// tempB nem az eleje és nem piros pont
			Point tF = new Point(tempF);
			if (resultF <= 1
					&& !tempF.equals(ending)
					&& (_objects.stream().anyMatch(pp -> pp.Color
							.equals(Colors.Red) && pp.Inner.equals(tF))))
			{
				resultF = resultF + _penalizeTurns;
			}
			Point tB = new Point(tempB);
			if (resultB <= 1
					&& !tempB.equals(from)
					&& (_objects.stream().anyMatch(pp -> pp.Color
							.equals(Colors.Red) && pp.Inner.equals(tB))))
			{
				resultB = resultB + _penalizeTurns;
			}
		}
		
		// Y tengely mentén
		for (int i = 0; i < dy; ++i)
		{
			if (sy > 0)
			{
				tempF = Point.Add(tempF, Direction.north);
				tempB = Point.Add(tempB, Direction.south);
			}
			else if (sy < 0)
			{
				tempF = Point.Add(tempF, Direction.south);
				tempB = Point.Add(tempB, Direction.north);
			}
			
			Point tF = new Point(tempF);
			if (resultF <= 1
					&& !tempF.equals(ending)
					&& (_objects.stream().anyMatch(pp -> pp.Color
							.equals(Colors.Red) && pp.Inner.equals(tF))))
			{
				resultF = resultF + _penalizeTurns;
			}
			Point tB = new Point(tempB);
			if (resultB <= 1
					&& !tempB.equals(from)
					&& (_objects.stream().anyMatch(pp -> pp.Color
							.equals(Colors.Red) && pp.Inner.equals(tB))))
			{
				resultB = resultB + _penalizeTurns;
			}
		}
		
		Integer tempMin = Math.min(resultF, resultB);
		min = Math.min(min, tempMin);
		
		return (double) min;
	}
	
	// end Metrics
	
	// Methods
	
	private Set<Pair<Node, Double>> Gamma(Node parent)
	{
		if (!_boundary.isIn(parent.getTo()))
			return new HashSet<Pair<Node, Double>>();
		
		Set<Pair<Node, Double>> result = new HashSet<Pair<Node, Double>>();
		
		// Add possible neighbors
		if (!_objects.stream().anyMatch(pp -> pp.Inner.equals(parent.getTo())
				&& pp.Color.equals(Colors.Red)))
		{
			Direction sub = Point.directionOf(parent.getTo(), parent.getFrom());
			if (_objects.stream()
					.anyMatch(pp -> pp.Inner.equals(parent.getTo())
							&& pp.Color.equals(Colors.Yellow)))
			{
				// straight, crossing
				result.add(new Pair<Node, Double>(new Node(parent.getTo(),
						Point.Add(parent.getTo(), sub)), _weightLength
						+ _weightCrossing));
			}
			else
			{
				for (Direction dir : Direction.values())
				{
					Double w = 0.0;
					
					if (dir.equals(sub))
						w = _weightLength;
					else if (dir.equals(Direction.opposite(sub)))
						continue;
					else
					{
						w = _weightLength + _weightTurns;
					}
					
					Point p = Point.Add(parent.getTo(), dir);
					result.add(new Pair<Node, Double>(new Node(parent.getTo(),
							p), w));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the route from start to end.
	 * 
	 * @return the route from start to end.
	 */
	public ArrayList<Node> value()
	{
		ArrayList<Node> result = new ArrayList<Node>();
		
		Node n = PI.get(_end);
		
		do
		{
			result.add(n);
			n = PI.get(n);
		} while (n != null);
		
		// Reverse order
		ArrayList<Node> result2 = new ArrayList<Node>();
		
		for (int i = result.size() - 1; i >= 0; --i)
		{
			result2.add(result.get(i));
		}
		
		return result2;
	}
	
	/**
	 * Returns the number of node extensions completed during the path search.
	 * 
	 * @return the number of node extensions completed during the path search.
	 */
	public Integer extendsNum()
	{
		return _extends;
	}
	
}
