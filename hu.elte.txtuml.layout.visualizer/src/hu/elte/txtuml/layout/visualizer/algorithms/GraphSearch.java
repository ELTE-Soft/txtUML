package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Cost;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Link;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Parent;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class GraphSearch
{
	
	private Point _start;
	private Point _end;
	private Point _before;
	private Point _after;
	// private Point _trueEnd;
	private Set<Point> _objects;
	private Integer _korlat;
	private Integer _kiterjesztesek;
	
	private Graph G;
	private Set<Point> Nyilt;
	private Cost g;
	private Parent PI;
	
	/**
	 * Creates a graph to find a route in it from start to end. Route length is
	 * minimal. Route cornering is minimal.
	 * 
	 * @param s
	 *            Start Point of the route.
	 * @param e
	 *            End Point of the route.
	 * @param os
	 *            Points we have to skip (already occupied).
	 * @throws CannotFindAssociationRouteException
	 * @throws Exception
	 * @throws MyException
	 */
	public GraphSearch(Point s, Point e, Set<Point> os)
			throws CannotFindAssociationRouteException
	{
		_start = s;
		_end = e;
		_objects = os;
		_korlat = -1;
		_kiterjesztesek = 0;
		
		G = new Graph();
		G.add(_start);
		
		Nyilt = new HashSet<Point>();
		Nyilt.add(_start);
		
		g = new Cost();
		g.set(_start, 0);
		
		PI = new Parent();
		PI.set(_start, null);
		
		if (!search())
			throw new CannotFindAssociationRouteException("No Route from " + s.toString()
					+ " to " + e.toString() + "!");
	}
	
	public GraphSearch(Point s, Point e, Set<Point> os, Point beforeS, Point afterE)
			throws CannotFindAssociationRouteException
	{
		_start = s;
		_end = e;
		_objects = os;
		_korlat = -1;
		_kiterjesztesek = 0;
		
		G = new Graph();
		G.add(_start);
		
		Nyilt = new HashSet<Point>();
		Nyilt.add(_start);
		
		g = new Cost();
		g.set(_start, 0);
		
		_before = beforeS;
		_after = afterE;
		
		PI = new Parent();
		PI.set(_before, null);
		PI.set(_start, _before);
		PI.set(_after, _end);
		
		if (!search())
			throw new CannotFindAssociationRouteException("No Route from " + s.toString()
					+ " to " + e.toString() + "!");
	}
	
	public GraphSearch(Point s, Point e, Set<Point> os, Integer k)
			throws CannotFindAssociationRouteException
	{
		_start = s;
		_end = e;
		_objects = os;
		_korlat = (k > 0) ? k : -1;
		_kiterjesztesek = 0;
		
		G = new Graph();
		G.add(_start);
		
		Nyilt = new HashSet<Point>();
		Nyilt.add(_start);
		
		g = new Cost();
		g.set(_start, 0);
		
		PI = new Parent();
		PI.set(_start, null);
		
		if (!search())
			throw new CannotFindAssociationRouteException("No Route from " + s.toString()
					+ " to " + e.toString() + "!");
	}
	
	public GraphSearch(Point s, Point e, Set<Point> os, Point beforeS, Point afterE,
			Integer k) throws CannotFindAssociationRouteException
	{
		_start = s;
		_end = e;
		_objects = os;
		_korlat = (k > 0) ? k : -1;
		_kiterjesztesek = 0;
		
		G = new Graph();
		G.add(_start);
		
		Nyilt = new HashSet<Point>();
		Nyilt.add(_start);
		
		g = new Cost();
		g.set(_start, 0);
		
		_before = beforeS;
		_after = afterE;
		
		PI = new Parent();
		if (_before != null)
		{
			PI.set(_before, null);
			PI.set(_start, _before);
		}
		else
			PI.set(_start, null);
		if (_after != null)
			PI.set(_after, _end);
		
		if (!search())
			throw new CannotFindAssociationRouteException("No Route from " + s.toString()
					+ " to " + e.toString() + "!");
	}
	
	private boolean search()
	{
		while (true)
		{
			if (Nyilt.isEmpty())
				return false;
			
			Point n = minf();
			
			if (_end.equals(n))
			{
				return true;
			}
			
			Nyilt.remove(n);
			
			++_kiterjesztesek;
			for (Point m : Gamma(n))
			{
				if (!G.contains(m) || g.get(n) + 1 < g.get(m))
				{
					PI.set(m, n);
					g.set(m, g.get(n) + 1);
					Nyilt.add(m);
				}
				G.addLink(new Link(n, m));
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
	
	private Integer f(Point p)
	{
		// TODO
		Double weightCost = 1.0;
		Double weightAway = 1.0;
		Double weightTurns = 2.0;
		Double weightDistance = 1.1;
		Double weightRemainingTurns = 1.0;
		
		Integer cost = g.get(p);
		Integer away = awayFromStart(p);
		Integer turns = countOfTurns(p);
		Integer distance = manhattanDistance(p, _end);
		// Integer remainingTurns1 = manhattanLeastTurns(p, _end);
		Integer remainingTurns = manhattanLeastTurnsCheckingOccupied(p, _end);
		
		Integer result = (int) (weightCost * cost + weightAway * away + weightTurns
				* turns + weightRemainingTurns * remainingTurns + weightDistance
				* distance);
		return result;
	}
	
	private ArrayList<Point> getCurrentRoute(Point current)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		Point p = current;
		
		Point pointToReach = (_before != null) ? _before : _start;
		do
		{
			result.add(p);
			p = PI.get(p);
		} while (!pointToReach.equals(p) && p != null);
		result.add(pointToReach);
		
		return result;
	}
	
	private Integer awayFromStart(Point current)
	{
		Integer cap = 3;
		
		Integer dist = manhattanDistance(_start, current);
		if (dist > cap)
			return 0;
		else
			return cap - dist;
	}
	
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
	
	private Integer manhattanDistance(Point a, Point b)
	{
		double dx = Math.abs(a.getX() - b.getX());
		double dy = Math.abs(a.getY() - b.getY());
		double result = dx + dy;
		return (int) result;
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
	
	private Integer manhattanLeastTurnsCheckingOccupied(Point a, Point b)
	{
		Integer penalizeTurns = 3;
		
		double dx = Math.abs(a.getX() - b.getX());
		double sx = b.getX() - a.getX();
		double dy = Math.abs(a.getY() - b.getY());
		double sy = b.getY() - a.getY();
		Integer result = 0;
		Integer result2 = 0;
		
		if (dx > 0 && dy > 0)
		{
			++result;
			++result2;
		}
		
		Point tempP = a;
		Point tempP2 = b;
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
			
			if (!tempP.equals(_end)
					&& (_objects.contains(tempP) || _objects.contains(tempP2)))
				result = result + penalizeTurns;
			if (!tempP2.equals(_end)
					&& (_objects.contains(tempP) || _objects.contains(tempP2)))
				result2 = result2 + penalizeTurns;
		}
		
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
			
			if (!tempP.equals(_end)
					&& (_objects.contains(tempP) || _objects.contains(tempP2)))
				result = result + penalizeTurns;
			if (!tempP2.equals(_end)
					&& (_objects.contains(tempP) || _objects.contains(tempP2)))
				result2 = result2 + penalizeTurns;
		}
		
		return Math.min(result, result2);
	}
	
	@SuppressWarnings("unused")
	private Integer euklideanDistance(Point a, Point b)
	{
		double dx = Math.pow(a.getX() - b.getX(), 2);
		double dy = Math.pow(a.getY() - b.getY(), 2);
		double result = Math.sqrt(dx + dy);
		return (int) Math.round(result);
	}
	
	private Set<Point> Gamma(Point parent)
	{
		Set<Point> result = new HashSet<Point>();
		ArrayList<Point> toberemoved = new ArrayList<Point>();
		
		// Add possible neighbors
		result.add(Point.Add(parent, Direction.north));
		result.add(Point.Add(parent, Direction.east));
		result.add(Point.Add(parent, Direction.south));
		result.add(Point.Add(parent, Direction.west));
		
		// Remove nodes already in graph, or occupied by objects.
		for (Point n : result)
		{
			if (G.contains(n))
				toberemoved.add(n);
			else if (_objects.contains(n) && !n.equals(_start) && !n.equals(_end))
				toberemoved.add(n);
			else if (_korlat != -1
					&& (Math.abs(n.getX()) > _korlat || Math.abs(n.getY()) > _korlat))
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
		Point pointToEnd = (_after != null) ? _after : _end;
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
		return _kiterjesztesek;
	}
	
}
