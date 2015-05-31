package hu.elte.txtuml.layout.visualizer.algorithms.links;

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
import java.util.Set;

/**
 * This class implements an A^* graph search algorithm.
 * 
 * @author Balázs Gregorics
 */
class GraphSearch
{
	
	// Constants
	
	private final Double _weightLength = 1.0;
	private final Double _weightTurns = 2.0;
	private final Double _weightCrossing = 2.0;
	private final Integer _penalizeTurns = 2;
	
	// end Constants
	
	// Variables
	
	private Set<Node> _startSet;
	private Node _end;
	private Set<Node> _endSet;
	private Set<Painted<Point>> _objects;
	private Integer _boundary;
	private Integer _extends;
	private Set<Integer> _batches;
	
	private Graph<Node> G;
	private Set<Node> Nyilt;
	private Cost<Node> g;
	private Parent<Node> PI;
	
	private HashMap<Node, Integer> _manhattanDistance;
	private HashMap<Node, Integer> _leastTurns;
	
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
	 * @param top
	 *            Upper bound of the maximum width of the graph to search in.
	 * @param bs
	 *            Set of ids of the batches.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if there is no route from start->end.
	 * @throws CannotStartAssociationRouteException
	 *             Throws if the alogirthm cannot even start the route from
	 *             start.
	 * @throws ConversionException
	 * @throws InternalException
	 */
	public GraphSearch(Set<Node> ss, Set<Node> es, Set<Painted<Point>> os, Integer top,
			Set<Integer> bs) throws CannotFindAssociationRouteException,
			CannotStartAssociationRouteException, ConversionException, InternalException
	{
		_end = new Node(new Point(), new Point());
		_startSet = new HashSet<Node>(ss);
		_endSet = new HashSet<Node>(es);
		_objects = os;
		_boundary = (top > 0) ? top : -1;
		_extends = 0;
		_batches = bs;
		_manhattanDistance = new HashMap<Node, Integer>();
		_leastTurns = new HashMap<Node, Integer>();
		
		G = new Graph<Node>();
		
		Nyilt = new HashSet<Node>();
		
		g = new Cost<Node>();
		
		PI = new Parent<Node>();
		
		// Extend StartSet
		for (Node p : _startSet)
		{
			Nyilt.add(p);
			g.set(p, 2 * _weightLength);
			PI.set(p, null);
		}
		
		if (!search())
		{
			throw new CannotFindAssociationRouteException("No Route from START to END!");
		}
	}
	
	// end Ctors
	
	// Methods
	
	private boolean search() throws ConversionException, InternalException
	{
		while (true)
		{
			if (Nyilt.isEmpty())
				return false;
			
			Node n = minf();
			
			if (_endSet.stream().anyMatch(node -> node.getFrom().equals(n.getTo())))
			{
				Node e = _endSet.stream()
						.filter(node -> node.getFrom().equals(n.getTo())).findFirst()
						.get();
				PI.set(e, n);
				PI.set(_end, e);
				return true;
			}
			
			Nyilt.remove(n);
			
			++_extends;
			for (Pair<Node, Double> pair : Gamma(n))
			{
				Node m = pair.First;
				Double newCost = (g.get(n) + pair.Second);
				if (!G.contains(m) || newCost < g.get(m))
				{
					PI.set(m, n);
					g.set(m, newCost);
					Nyilt.add(m);
				}
				G.addNode(m);
				G.addLink(new Link<Node>(n, m));
			}
		}
	}
	
	private Node minf()
	{
		Node min = null;
		Double minval = Double.MAX_VALUE;
		
		for (Node p : Nyilt)
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
		// Quickened with memory
		Double distance = (double) manhattanDistance(p);
		// Quickened with memory
		Double remainingTurns = (double) manhattanLeastTurnsCheckingOccupied(p);
		
		return (_weightTurns * remainingTurns + _weightLength * distance);
	}
	
	// Metrics
	
	private Integer manhattanDistance(Node a)
	{
		if (_manhattanDistance.containsKey(a))
			return _manhattanDistance.get(a);
		
		Integer temp = (int) Math.floor(_endSet.stream().map(p ->
		{
			Integer dx = Math.abs(a.getTo().getX() - p.getFrom().getX());
			Integer dy = Math.abs(a.getTo().getY() - p.getFrom().getY());
			Integer tempResult = dx + dy;
			
			return tempResult;
		}).min((d1, d2) ->
		{
			return Integer.compare(d1, d2);
		}).get());
		_manhattanDistance.put(a, temp);
		return temp;
	}
	
	private Integer manhattanLeastTurnsCheckingOccupied(Node a)
	{
		if (_leastTurns.containsKey(a))
			return _leastTurns.get(a);
		
		Integer min = Integer.MAX_VALUE;
		
		for (Node endnode : _endSet)
		{
			Point ending = endnode.getFrom();
			double dx = Math.abs(a.getTo().getX() - ending.getX());
			double sx = ending.getX() - a.getTo().getX();
			double dy = Math.abs(a.getTo().getY() - ending.getY());
			double sy = ending.getY() - a.getTo().getY();
			Integer resultF = 0;
			Integer resultB = 0;
			
			if (dx > 0 && dy > 0)
			{
				++resultF;
				++resultB;
			}
			
			// elejérõl és végérõl nézzük egyszerre a két külön utat
			// X tengely mentén
			Point tempF = new Point(a.getTo());
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
						&& (_objects.stream().anyMatch(pp -> pp.Color.equals(Colors.Red)
								&& pp.Inner.equals(tF))))
				{
					resultF = resultF + _penalizeTurns;
				}
				Point tB = new Point(tempB);
				if (resultB <= 1
						&& !tempB.equals(a)
						&& (_objects.stream().anyMatch(pp -> pp.Color.equals(Colors.Red)
								&& pp.Inner.equals(tB))))
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
						&& (_objects.stream().anyMatch(pp -> pp.Color.equals(Colors.Red)
								&& pp.Inner.equals(tF))))
				{
					resultF = resultF + _penalizeTurns;
				}
				Point tB = new Point(tempB);
				if (resultB <= 1
						&& !tempB.equals(a)
						&& (_objects.stream().anyMatch(pp -> pp.Color.equals(Colors.Red)
								&& pp.Inner.equals(tB))))
				{
					resultB = resultB + _penalizeTurns;
				}
			}
			
			Integer tempMin = Math.min(resultF, resultB);
			min = Math.min(min, tempMin);
		}
		
		_leastTurns.put(a, min);
		return min;
	}
	
	// end Metrics
	
	// Methods
	
	private Set<Pair<Node, Double>> Gamma(Node parent) throws ConversionException
	{
		if (_boundary != -1
				&& (Math.abs(parent.getTo().getX()) > _boundary || Math.abs(parent
						.getTo().getY()) > _boundary))
			return new HashSet<Pair<Node, Double>>();
		
		Set<Pair<Node, Double>> result = new HashSet<Pair<Node, Double>>();
		
		// Add possible neighbors
		if (!_objects.stream().anyMatch(
				pp -> pp.Inner.equals(parent.getTo())
						&& !pp.Batch.stream().anyMatch(b -> _batches.contains(b))
						&& pp.Color.equals(Colors.Red)))
		{
			Direction sub = Point.directionOf(parent.getTo(), parent.getFrom());
			if (_objects.stream().anyMatch(
					pp -> pp.Inner.equals(parent.getTo())
							&& !pp.Batch.stream().anyMatch(b -> _batches.contains(b))
							&& pp.Color.equals(Colors.Yellow)))
			{
				// straight, crossing
				result.add(new Pair<Node, Double>(new Node(parent.getTo(), Point.Add(
						parent.getTo(), sub)), _weightLength + _weightCrossing));
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
						w = _weightLength + _weightTurns;
					
					result.add(new Pair<Node, Double>(new Node(parent.getTo(), Point.Add(
							parent.getTo(), dir)), w));
				}
			}
		}
		
		return result;
	}
	
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
	
	public Integer extendsNum()
	{
		return _extends;
	}
	
}
