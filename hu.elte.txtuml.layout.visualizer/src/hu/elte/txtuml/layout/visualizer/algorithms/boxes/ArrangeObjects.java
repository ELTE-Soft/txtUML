package hu.elte.txtuml.layout.visualizer.algorithms.boxes;

import hu.elte.txtuml.layout.visualizer.algorithms.boxes.bellmanfordhelpers.DirectedEdge;
import hu.elte.txtuml.layout.visualizer.algorithms.boxes.bellmanfordhelpers.EdgeWeightedDigraph;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotPositionObjectException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.BiMap;
import hu.elte.txtuml.layout.visualizer.helpers.Quadraple;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
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
	private BiMap<String, Integer> _indices;
	private Boolean _logging;
	
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
	 * @param stats
	 *            List of statements on the boxes.
	 * @param log
	 *            Whether to print out logging msgs.
	 * @throws BoxArrangeConflictException
	 *             Throws when a conflict appears during the arrangement of the
	 *             boxes.
	 * @throws CannotPositionObjectException
	 *             Throws if the algorithm cannot position a box for some
	 *             reason.
	 * @throws InternalException
	 *             Throws if some algorithm related error happens. Contact
	 *             programmer for more details.
	 */
	public ArrangeObjects(Set<RectangleObject> obj, ArrayList<Statement> stats,
			Boolean log) throws BoxArrangeConflictException,
			CannotPositionObjectException, InternalException
	
	{
		_logging = log;
		
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
		if (_logging)
			System.err.print("Arranging overlaps...");
		
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
		_transformAmount = (int) Math.floor((Math.ceil(Math.sqrt(max))) + 1);
		
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
		
		if (_logging)
			System.err.println("DONE!");
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
	
	private ArrayList<Quadraple<Integer, Integer, Integer, Statement>> buildEdges(
			Integer n, ArrayList<Statement> stats) throws InternalException
	{
		ArrayList<Quadraple<Integer, Integer, Integer, Statement>> result = new ArrayList<Quadraple<Integer, Integer, Integer, Statement>>();
		
		for (int i = 0; i < (2 * n) + 1; ++i)
		{
			result.add(new Quadraple<Integer, Integer, Integer, Statement>(0, i, 0, null));
		}
		
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
	
	/**
	 * Returns the value of the arrangement.
	 * 
	 * @return the value of the arrangement.
	 */
	public Set<RectangleObject> value()
	{
		return _objects;
	}
	
}
