package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.bellmanfordhelpers.EdgeWeightedDigraph;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.BiMap;
import hu.elte.txtuml.layout.visualizer.helpers.Triple;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

class ArrangeObjects
{
	private Set<RectangleObject> _objects;
	private BiMap<String, Integer> _indices;
	
	private Integer _transformAmount;
	
	public Integer getTransformAmount()
	{
		return _transformAmount;
	}
	
	public void setTransformAmount(Integer value)
	{
		_transformAmount = value;
	}
	
	public ArrangeObjects(Set<RectangleObject> obj, ArrayList<Statement> stats)
			throws ConflictException, CannotPositionObjectException, InternalException
	
	{
		// Nothing to arrange
		if (obj.size() == 0)
			return;
		
		_objects = obj;
		_transformAmount = 1;
		
		// Default, auto-layout
		if (stats.size() == 0)
		{
			defaultLayout();
			return;
		}
		
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
		
		int n = _objects.size();
		int startNode = 0;
		ArrayList<Triple<Integer, Integer, Integer>> l = buildEdges(n, stats);
		
		EdgeWeightedDigraph G = new EdgeWeightedDigraph((2 * n) + 1, l);
		
		BellmanFordSP sp = new BellmanFordSP(G, startNode);
		
		if (sp.hasNegativeCycle())
		{
			// Nincs megoldás, ellentmondás
			throw new ConflictException(
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
								.collect(Collectors.toList()).get(0);
						
						_objects.remove(mod);
						
						mod.setPosition(new Point(mod.getPosition().getX(), (int) sp
								.distTo(v)));
						
						_objects.add(mod);
					}
					else
					{
						// set X coordinate
						nameOfTheObject = _indices.getKey(v);
						
						RectangleObject mod = _objects.stream()
								.filter(o -> o.getName().equals(nameOfTheObject))
								.collect(Collectors.toList()).get(0);
						
						_objects.remove(mod);
						
						mod.setPosition(new Point((int) sp.distTo(v), mod.getPosition()
								.getY()));
						
						_objects.add(mod);
					}
				}
				else
				{
					throw new CannotPositionObjectException(
							"Certain elements are not connected to the others!");
				}
			}
		}
		
		arrangeOverlapping();
	}
	
	private void defaultLayout()
	{
		for (RectangleObject o : _objects)
		{
			o.setPosition(new Point(0, 0));
		}
		
		arrangeOverlapping();
	}
	
	private void arrangeOverlapping()
	{
		HashMap<Point, Integer> overlaps = new HashMap<Point, Integer>();
		Integer max = new Integer(0);
		
		for (RectangleObject o : _objects)
		{
			if (overlaps.containsKey(o.getPosition()))
			{
				overlaps.put(o.getPosition(), overlaps.get(o.getPosition()) + 1);
				if (overlaps.get(o.getPosition()) > max)
					max = overlaps.get(o.getPosition());
			}
			else
				overlaps.put(o.getPosition(), 1);
		}
		
		if (max <= 1)
		{
			// Nothing to do!
			return;
		}
		_transformAmount = (int) Math.floor((Math.ceil(Math.sqrt(max)) / 2) + 1);
		
		// Multiply coordinates by _transformAmount
		for (RectangleObject o : _objects)
		{
			o.setPosition(Point.Multiply(o.getPosition(), _transformAmount));
		}
		
		// maxvalue-tõl van valamerre gyökn * gyökn terület
		for (Entry<Point, Integer> entry : overlaps.entrySet())
		{
			if (entry.getValue() > 1)
			{
				overlapRemove(Point.Multiply(entry.getKey(), _transformAmount),
						entry.getValue());
			}
		}
	}
	
	private void overlapRemove(Point p, Integer c)
	{
		ArrayList<RectangleObject> objectsToModify = (ArrayList<RectangleObject>) _objects
				.stream().filter(o -> o.getPosition().equals(p))
				.collect(Collectors.toList());
		
		// First remains at p, (n-1) arranges around it
		Integer radius = 1;
		Integer num = 1;
		for (int i = 1; i < c; ++i)
		{
			RectangleObject tempObj = objectsToModify.get(i);
			// _objects.remove(tempObj);
			tempObj.setPosition(newPointAtRadius(p, radius, num));
			// _objects.add(tempObj);
			
			if (num.equals((radius * 4)))
			{
				++radius;
				num = 1;
			}
			else
				++num;
		}
	}
	
	private ArrayList<Triple<Integer, Integer, Integer>> buildEdges(Integer n,
			ArrayList<Statement> stats) throws InternalException
	{
		ArrayList<Triple<Integer, Integer, Integer>> result = new ArrayList<Triple<Integer, Integer, Integer>>();
		
		for (int i = 0; i < (2 * n) + 1; ++i)
		{
			result.add(new Triple<Integer, Integer, Integer>(0, i, 0));
		}
		
		for (Statement s : stats)
		{
			switch (s.getType())
			{
				case north:
				{
					int i = _indices.get(s.getParameter(0)) + n;
					int j = _indices.get(s.getParameter(1)) + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, -1));
				}
					break;
				case south:
				{
					int i = _indices.get(s.getParameter(1)) + n;
					int j = _indices.get(s.getParameter(0)) + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, -1));
				}
					break;
				case east:
				{
					int i = _indices.get(s.getParameter(0));
					int j = _indices.get(s.getParameter(1));
					result.add(new Triple<Integer, Integer, Integer>(i, j, -1));
				}
					break;
				case west:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Triple<Integer, Integer, Integer>(i, j, -1));
				}
					break;
				case vertical:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Triple<Integer, Integer, Integer>(i, j, 0));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 0));
				}
					break;
				case horizontal:
				{
					int i = _indices.get(s.getParameter(1)) + n;
					int j = _indices.get(s.getParameter(0)) + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, 0));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 0));
				}
					break;
				case above:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Triple<Integer, Integer, Integer>(i, j, 0));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 0));
					i = i + n;
					j = j + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, 1));
					result.add(new Triple<Integer, Integer, Integer>(j, i, -1));
				}
					break;
				case below:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Triple<Integer, Integer, Integer>(i, j, 0));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 0));
					i = i + n;
					j = j + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, -1));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 1));
				}
					break;
				case right:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Triple<Integer, Integer, Integer>(i, j, 1));
					result.add(new Triple<Integer, Integer, Integer>(j, i, -1));
					i = i + n;
					j = j + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, 0));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 0));
				}
					break;
				case left:
				{
					int i = _indices.get(s.getParameter(1));
					int j = _indices.get(s.getParameter(0));
					result.add(new Triple<Integer, Integer, Integer>(i, j, -1));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 1));
					i = i + n;
					j = j + n;
					result.add(new Triple<Integer, Integer, Integer>(i, j, 0));
					result.add(new Triple<Integer, Integer, Integer>(j, i, 0));
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
	
	private Point newPointAtRadius(Point p, Integer radius, Integer num)
	{
		Point result = p;
		
		for (int i = 0; i < radius; ++i)
		{
			result = Point.Add(result, Direction.north);
		}
		
		// Down right
		for (int dr = 0; dr < radius; ++dr)
		{
			if (num.equals(0))
				return result;
			result = Point.Add(result, new Point(1, -1));
			--num;
		}
		// Down left
		for (int dl = 0; dl < radius; ++dl)
		{
			if (num.equals(0))
				return result;
			result = Point.Add(result, new Point(-1, -1));
			--num;
		}
		// Up left
		for (int ul = 0; ul < radius; ++ul)
		{
			if (num.equals(0))
				return result;
			result = Point.Add(result, new Point(-1, 1));
			--num;
		}
		// Up right
		for (int ur = 0; ur < radius; ++ur)
		{
			if (num.equals(0))
				return result;
			result = Point.Add(result, new Point(1, 1));
			--num;
		}
		
		return result;
	}
	
	public Set<RectangleObject> value()
	{
		return _objects;
	}
	
}
