package hu.elte.txtuml.layout.visualizer.algorithms.links;

import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Cost;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Painted;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Painted.Colors;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Parent;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements an A^c graph search algorithm.
 * 
 * @author Balázs Gregorics
 */
class GraphSearch
{
	
	// Variables
	
	private Point _start;
	private Set<Point> _startSet;
	private Point _end;
	private Set<Point> _endSet;
	private Set<Painted<Point>> _objects;
	private Integer _boundary;
	private Integer _extends;
	
	private Graph<Point> G;
	private Set<Point> Nyilt;
	private Cost g;
	private Parent PI;
	
	private HashMap<Point, Integer> _manhattanDistance;
	private HashMap<Point, Integer> _leastTurns;
	
	// end Variables
	
	// Ctors
	
	/**
	 * Creates a graph to find a route in it from start to end. Route length is
	 * minimal. Route cornering is minimal.
	 * 
	 * @param s
	 *            Start Point of the route.
	 * @param ss
	 *            Set of start Points.
	 * @param e
	 *            End Point of the route.
	 * @param es
	 *            Set of end Points to reach.
	 * @param os
	 *            Points we have to skip (already occupied).
	 * @param top
	 *            Upper bound of the maximum width of the graph to search in.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if there is no route from start->end.
	 * @throws CannotStartAssociationRouteException
	 *             Throws if the alogirthm cannot even start the route from
	 *             start.
	 */
	public GraphSearch(Point s, Set<Point> ss, Point e, Set<Point> es,
			Set<Painted<Point>> os, Integer top)
			throws CannotFindAssociationRouteException,
			CannotStartAssociationRouteException
	{
		_start = s;
		_startSet = ss;
		_end = e;
		_endSet = new HashSet<Point>(es);
		_objects = os;
		_boundary = (top > 0) ? top : -1;
		_extends = 0;
		_manhattanDistance = new HashMap<Point, Integer>();
		_leastTurns = new HashMap<Point, Integer>();
		
		G = new Graph<Point>();
		G.add(_start);
		
		Nyilt = new HashSet<Point>();
		Nyilt.add(_start);
		
		g = new Cost();
		g.set(_start, 0);
		
		PI = new Parent();
		PI.set(_start, null);
		
		if (!search())
		{
			if (!PI.hasChild(_start))
				throw new CannotStartAssociationRouteException("Cannot start from"
						+ s.toString() + "!");
			else
				throw new CannotFindAssociationRouteException("No Route from "
						+ s.toString() + " to " + e.toString() + "!");
		}
	}
	
	// end Ctors
	
	// Methods
	
	private boolean search()
	{
		while (true)
		{
			if (Nyilt.isEmpty())
				return false;
			
			Point n = minf();
			
			if (_endSet.contains(n))
			{
				PI.set(_end, n);
				g.set(_end, g.get(n) + 1);
				G.addLink(new Link<Point>(n, _end));
				G.addNode(_end);
				return true;
			}
			
			Nyilt.remove(n);
			
			++_extends;
			for (Point m : Gamma(n))
			{
				if (!G.contains(m) || g.get(n) + 1 < g.get(m))
				{
					PI.set(m, n);
					g.set(m, g.get(n) + 1);
					Nyilt.add(m);
				}
				G.addLink(new Link<Point>(n, m));
				G.addNode(m);
			}
		}
	}
	
	private Point minf()
	{
		Point min = null;
		Integer minval = -1;
		
		for (Point p : Nyilt)
		{
			if (minval.equals(-1))
			{
				minval = f(p);
				min = p;
			}
			else if (f(p) < minval)
			{
				minval = f(p);
				min = p;
			}
		}
		
		return min;
	}
	
	// Kiértékelõ függvény
	private Integer f(Point p)
	{
		// TODO
		Double weightCost = 1.0;
		Double weightTurns = 1.5;
		Double weightDistance = 1.0;
		Double weightRemainingTurns = 1.0;
		Double weightYellowPoints = 1.5;
		
		Integer cost = g.get(p);
		Integer turns = countOfTurns(p);
		// Quickened with memory
		Integer distance = manhattanDistance(p);
		// Quickened with memory
		Integer remainingTurns = manhattanLeastTurnsCheckingOccupied(p);
		Integer yellowPoints = countOfYellows(p);
		
		Integer result = (int) (weightCost * cost + weightTurns * turns
				+ weightRemainingTurns * remainingTurns + weightDistance * distance + weightYellowPoints
				* yellowPoints);
		
		return result;
	}
	
	private ArrayList<Point> getCurrentRoute(Point current)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		Point p = current;
		
		Point pointToReach = _start;
		do
		{
			result.add(p);
			p = PI.get(p);
		} while (!pointToReach.equals(p) && p != null);
		result.add(pointToReach);
		
		return result;
	}
	
	// Metrics
	
	private Integer countOfTurns(Point current)
	{
		ArrayList<Point> result = getCurrentRoute(current);
		
		int count = 0;
		
		for (int i = 2; i < result.size(); ++i)
		{
			Point a = result.get(i - 2);
			Point b = result.get(i - 1);
			Point c = result.get(i);
			
			if (!Point.Substract(a, b).equals(Point.Substract(b, c)))
			{
				++count;
			}
		}
		
		return count;
	}
	
	private Integer countOfYellows(Point current)
	{
		ArrayList<Point> route = getCurrentRoute(current);
		
		int count = 0;
		
		for (Point point : route)
		{
			if (_objects.stream().anyMatch(p -> p.Inner.equals(point))
					&& _objects.stream().filter(p -> p.Inner.equals(point)).findFirst()
							.get().Color.equals(Colors.Yellow))
			{
				++count;
			}
		}
		
		return count;
	}
	
	private Integer manhattanDistance(Point a)
	{
		if (_manhattanDistance.containsKey(a))
			return _manhattanDistance.get(a);
		
		Integer temp = (int) Math.floor(_endSet.stream().map(p ->
		{
			double dx = Math.abs(a.getX() - p.getX());
			double dy = Math.abs(a.getY() - p.getY());
			double tempResult = dx + dy;
			
			return tempResult;
		}).min((d1, d2) ->
		{
			return Double.compare(d1, d2);
		}).get());
		_manhattanDistance.put(a, temp);
		return temp;
		
		/*
		 * double dx = Math.abs(a.getX() - _end.getX());
		 * double dy = Math.abs(a.getY() - _end.getY());
		 * double tempResult = dx + dy - (_widthOfObject / 2);
		 * 
		 * return (int) tempResult;
		 */
	}
	
	@SuppressWarnings("unused")
	private Integer manhattanLeastTurns(Point a, Point b)
	{
		double dx = Math.abs(a.getX() - b.getX());
		double dy = Math.abs(a.getY() - b.getY());
		
		if (dx > 0 && dy > 0)
			return 1;
		
		return 0;
	}
	
	private Integer manhattanLeastTurnsCheckingOccupied(Point a)
	{
		if (_leastTurns.containsKey(a))
			return _leastTurns.get(a);
		
		Integer penalizeTurns = 2;
		Integer min = Integer.MAX_VALUE;
		
		for (Point ending : _endSet)
		{
			double dx = Math.abs(a.getX() - ending.getX());
			double sx = ending.getX() - a.getX();
			double dy = Math.abs(a.getY() - ending.getY());
			double sy = ending.getY() - a.getY();
			Integer result = 0;
			Integer result2 = 0;
			
			if (dx > 0 && dy > 0)
			{
				++result;
				++result2;
			}
			
			Point tempP = a;
			Point tempP2 = ending;
			for (int i = 0; i < dx; ++i)
			{
				if (sx > 0)
				{
					tempP = Point.Add(tempP, Direction.east);
					tempP2 = Point.Add(tempP2, Direction.west);
				}
				else if (sx < 0)
				{
					tempP = Point.Add(tempP, Direction.west);
					tempP2 = Point.Add(tempP2, Direction.east);
				}
				
				Painted<Point> temp1 = new Painted<Point>(Colors.Red, tempP);
				Painted<Point> temp2 = new Painted<Point>(Colors.Red, tempP2);
				if (!tempP.equals(ending)
						&& (_objects.contains(temp1) || _objects.contains(temp2)))
				{
					result = result + penalizeTurns;
					break;
				}
				if (!tempP2.equals(ending)
						&& (_objects.contains(temp1) || _objects.contains(temp2)))
				{
					result2 = result2 + penalizeTurns;
					break;
				}
			}
			
			tempP = a;
			tempP2 = ending;
			for (int i = 0; i < dy; ++i)
			{
				if (sy > 0)
				{
					tempP = Point.Add(tempP, Direction.north);
					tempP2 = Point.Add(tempP2, Direction.south);
				}
				else if (sy < 0)
				{
					tempP = Point.Add(tempP, Direction.south);
					tempP2 = Point.Add(tempP2, Direction.north);
				}
				
				Painted<Point> temp1 = new Painted<Point>(Colors.Red, tempP);
				Painted<Point> temp2 = new Painted<Point>(Colors.Red, tempP2);
				if (!tempP.equals(ending)
						&& (_objects.contains(temp1) || _objects.contains(temp2)))
				{
					result = result + penalizeTurns;
					break;
				}
				if (!tempP2.equals(ending)
						&& (_objects.contains(temp1) || _objects.contains(temp2)))
				{
					result2 = result2 + penalizeTurns;
					break;
				}
			}
			
			min = Math.min(min, result);
			min = Math.min(min, result2);
		}
		
		_leastTurns.put(a, min);
		return min;
	}
	
	@SuppressWarnings("unused")
	private Integer euklideanDistance(Point a, Point b)
	{
		double dx = Math.pow(a.getX() - b.getX(), 2);
		double dy = Math.pow(a.getY() - b.getY(), 2);
		double result = Math.sqrt(dx + dy);
		return (int) Math.round(result);
	}
	
	@SuppressWarnings("unused")
	private Integer maxDifferenceDistance(Point a, Point b)
	{
		double dx = Math.abs(a.getX() - b.getX());
		double dy = Math.abs(a.getY() - b.getY());
		return (int) ((dx > dy) ? dx : dy);
	}
	
	// end Metrics
	
	// Methods
	
	private Set<Point> Gamma(Point parent)
	{
		Set<Point> result = new HashSet<Point>();
		ArrayList<Point> toberemoved = new ArrayList<Point>();
		
		// Add possible neighbors
		if (parent == _start)
		{
			// Starting edges
			result.addAll(_startSet);
			return result;
		}
		else if (_endSet.contains(parent))
		{
			// Ending edges
			result.add(_end);
			return result;
		}
		else
		{
			// Default
			result.add(Point.Add(parent, Direction.north));
			result.add(Point.Add(parent, Direction.east));
			result.add(Point.Add(parent, Direction.south));
			result.add(Point.Add(parent, Direction.west));
		}
		
		// Remove nodes already in graph, or occupied by objects.
		for (Point n : result)
		{
			if (G.contains(n))
				toberemoved.add(n);
			else if (_objects.stream().anyMatch(p -> p.Inner.equals(n)))
			{
				// -> Red
				if (_objects.stream().filter(p -> p.Inner.equals(n)).findFirst().get().Color
						.equals(Colors.Red))
				{
					toberemoved.add(n);
				}
				// Yellow -> Yellow
				if (_objects.stream().anyMatch(p -> p.Inner.equals(parent)))
				{
					if (_objects.stream().filter(p -> p.Inner.equals(parent)).findFirst()
							.get().Color.equals(Colors.Yellow)
							&& _objects.stream().filter(p -> p.Inner.equals(n))
									.findFirst().get().Color.equals(Colors.Yellow))
					{
						toberemoved.add(n);
					}
				}
				// -> Blue
				if (_objects.stream().filter(p -> p.Inner.equals(n)).findFirst().get().Color
						.equals(Colors.Blue))
				{
					toberemoved.add(n);
				}
			}
			else if (_boundary != -1
					&& (Math.abs(n.getX()) > _boundary || Math.abs(n.getY()) > _boundary))
				toberemoved.add(n);
		}
		
		for (Point n : toberemoved)
		{
			result.remove(n);
		}
		
		return result;
	}
	
	public ArrayList<Point> value()
	{
		ArrayList<Point> result = new ArrayList<Point>();
		Point pointToEnd = _end;
		ArrayList<Point> temp = getCurrentRoute(pointToEnd);
		
		for (int i = temp.size() - 1; i >= 0; --i)
		{
			result.add(temp.get(i));
		}
		
		return result;
	}
	
	public Integer turns()
	{
		ArrayList<Point> route = value();
		
		int count = 0;
		
		for (int i = 2; i < route.size(); ++i)
		{
			Point a = route.get(i - 2);
			Point b = route.get(i - 1);
			Point c = route.get(i);
			
			if (!Point.Substract(a, b).equals(Point.Substract(b, c)))
			{
				++count;
			}
		}
		
		return count;
	}
	
	public Integer extendsNum()
	{
		return _extends;
	}
	
}
