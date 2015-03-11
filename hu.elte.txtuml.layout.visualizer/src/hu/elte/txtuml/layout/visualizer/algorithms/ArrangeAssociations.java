package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation.RouteConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

class ArrangeAssociations
{
	private Integer _transformAmount;
	private ArrayList<LineAssociation> _assocs;
	private HashMap<Pair<String, RouteConfig>, Point> _modifies;
	
	/**
	 * Gets the maximum amount which the diagram was enlarged during the arrange
	 * of the associations.
	 * 
	 * @return Integer of the amount.
	 */
	public Integer getTransformAmount()
	{
		return _transformAmount;
	}
	
	/**
	 * Arranges associations between objects, on the grid.
	 * 
	 * @param diagramObjects
	 *            Objects on the grid.
	 * @param diagramAssocs
	 *            Associations to arrange on the grid.
	 * @param stats
	 *            Statements on associations.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if the algorithm cannot find the route for a
	 *             association.
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain StatementType into
	 *             Direction.
	 * @throws InternalException
	 *             Throws if any error occurs which should not happen. Contact
	 *             developer!
	 */
	public ArrangeAssociations(HashSet<RectangleObject> diagramObjects,
			HashSet<LineAssociation> diagramAssocs, ArrayList<Statement> stats)
			throws ConversionException, CannotFindAssociationRouteException,
			InternalException
	{
		if (diagramAssocs == null || diagramAssocs.size() == 0 || diagramAssocs == null
				|| diagramAssocs.size() == 0)
			return;
		
		_transformAmount = 2;
		arrange(diagramObjects, diagramAssocs, stats);
	}
	
	private void arrange(HashSet<RectangleObject> diagramObjects,
			HashSet<LineAssociation> diagramAssocs, ArrayList<Statement> stats)
			throws ConversionException, CannotFindAssociationRouteException,
			InternalException
	{
		HashSet<Pair<Point, Integer>> occupied = new HashSet<Pair<Point, Integer>>();
		_assocs = (ArrayList<LineAssociation>) diagramAssocs.stream().collect(
				Collectors.toList());
		_modifies = new HashMap<Pair<String, RouteConfig>, Point>();
		
		// Count maxlinks on side of obejct
		// Integer maxLinks = maxLinksOnSide();
		
		// TODO
		
		// Transform everything to new dimensions (*2)
		// Add objects transformed place to occupied list
		for (RectangleObject obj : diagramObjects)
		{
			occupied.add(new Pair<Point, Integer>(transformDimension(obj.getPosition()),
					2));
		}
		// Transform Links' start and end route point
		for (int i = 0; i < _assocs.size(); ++i)
		{
			LineAssociation mod = _assocs.get(i);
			ArrayList<Point> temp = mod.getRoute();
			ArrayList<Point> route = new ArrayList<Point>();
			for (Point p : temp)
			{
				route.add(transformDimension(p));
			}
			mod.setRoute(route);
			_assocs.set(i, mod);
		}
		
		// Maximum distance between objects
		Integer top = calcMaxDistance(occupied);
		
		// Transform everything so the object with most(n) links have n entry
		// points
		// TODO
		
		// Process statements, priority and direction
		if (stats != null && stats.size() != 0)
		{
			// Set priority
			HashMap<String, Integer> priorityMap = setPriorityMap(stats);
			
			// Order based on priority
			_assocs.sort((a1, a2) ->
			{
				if (priorityMap.containsKey(a1.getId()))
				{
					if (priorityMap.containsKey(a2.getId()))
					{
						Integer v = priorityMap.get(a1.getId());
						Integer w = priorityMap.get(a2.getId());
						return (v - w > 0) ? 1 : -1;
					}
					else
						return -1;
				}
				else
				{
					if (priorityMap.containsKey(a2.getId()))
						return 1;
					else
						return 0;
				}
			});
			
			// Set starts/ends for statemented assocs
			setModified(stats, diagramObjects);
		}
		
		// Search for the route of every Link
		for (int i = 0; i < _assocs.size(); ++i)
		{
			LineAssociation a = _assocs.get(i);
			Point START = a.getRoute(LineAssociation.RouteConfig.START);
			Point END = a.getRoute(LineAssociation.RouteConfig.END);
			Point BEFORE = _modifies.get(new Pair<String, RouteConfig>(a.getId(),
					RouteConfig.START));
			Point AFTER = _modifies.get(new Pair<String, RouteConfig>(a.getId(),
					RouteConfig.END));
			
			GraphSearch gs = new GraphSearch(START, END, occupied, BEFORE, AFTER, top);
			a.setRoute(gs.value());
			a.setTurns(gs.turns());
			a.setExtends(gs.extendsNum());
			_assocs.set(i, a);
			
			// Update occupied places with the route of this link
			if (a.getRoute().size() < 3)
				throw new InternalException("Route is shorter then 3!");
			
			for (int ri = 2; ri < a.getRoute().size() - 2; ++ri)
			{
				if (a.getRoute().size() > ri + 1
						&& !Point.Substract(a.getRoute().get(ri - 1),
								a.getRoute().get(ri)).equals(
								Point.Substract(a.getRoute().get(ri),
										a.getRoute().get(ri + 1))))
				{
					occupied.add(new Pair<Point, Integer>(a.getRoute().get(ri), 2));
				}
				else
				{
					Point temp = a.getRoute().get(ri);
					if (!occupied.stream().anyMatch(pr -> pr.First.equals(temp)))
						occupied.add(new Pair<Point, Integer>(a.getRoute().get(ri), 1));
				}
			}
		}
	}
	
	private Integer calcMaxDistance(HashSet<Pair<Point, Integer>> grid)
	{
		Integer maxval = 0;
		
		for (Pair<Point, Integer> p1 : grid)
		{
			for (Pair<Point, Integer> p2 : grid)
			{
				int dx = Math.abs(p1.First.getX() - p2.First.getX());
				int dy = Math.abs(p1.First.getY() - p2.First.getY());
				if (dx > maxval)
					maxval = dx;
				if (dy > maxval)
					maxval = dy;
			}
		}
		
		return maxval;
	}
	
	@SuppressWarnings("unused")
	private Integer maxLinksOnSide()
	{
		HashMap<Pair<String, Direction>, Integer> temp = new HashMap<Pair<String, Direction>, Integer>();
		Integer maxvalue = 0;
		
		for (LineAssociation a : _assocs)
		{
			// which direction
			int dx = a.getRoute(RouteConfig.START).getX()
					- a.getRoute(RouteConfig.END).getX();
			int dy = a.getRoute(RouteConfig.START).getY()
					- a.getRoute(RouteConfig.END).getY();
			
			String objname = a.getFrom();
			
			if (Math.abs(dx) >= Math.abs(dy) && dx >= 0)
			{
				// jobbra
				Pair<String, Direction> key = new Pair<String, Direction>(objname,
						Direction.east);
				if (temp.containsKey(key))
					temp.put(key, temp.get(key) + 1);
				else
					temp.put(key, 1);
				
				if (temp.get(key) > maxvalue)
					maxvalue = temp.get(key);
			}
			if (Math.abs(dx) >= Math.abs(dy) && dx <= 0)
			{
				// balra
				Pair<String, Direction> key = new Pair<String, Direction>(objname,
						Direction.west);
				if (temp.containsKey(key))
					temp.put(key, temp.get(key) + 1);
				else
					temp.put(key, 1);
				
				if (temp.get(key) > maxvalue)
					maxvalue = temp.get(key);
			}
			if (Math.abs(dx) <= Math.abs(dy) && dy >= 0)
			{
				// fel
				Pair<String, Direction> key = new Pair<String, Direction>(objname,
						Direction.north);
				if (temp.containsKey(key))
					temp.put(key, temp.get(key) + 1);
				else
					temp.put(key, 1);
				
				if (temp.get(key) > maxvalue)
					maxvalue = temp.get(key);
			}
			if (Math.abs(dx) <= Math.abs(dy) && dy <= 0)
			{
				// le
				Pair<String, Direction> key = new Pair<String, Direction>(objname,
						Direction.south);
				if (temp.containsKey(key))
					temp.put(key, temp.get(key) + 1);
				else
					temp.put(key, 1);
				
				if (temp.get(key) > maxvalue)
					maxvalue = temp.get(key);
			}
			
		}
		
		return maxvalue;
	}
	
	private HashMap<String, Integer> setPriorityMap(ArrayList<Statement> stats)
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		for (Statement s : stats)
		{
			if (s.getType().equals(StatementType.priority))
			{
				result.put(s.getParameter(0), Integer.parseInt(s.getParameter(1)));
			}
		}
		
		return result;
	}
	
	private void setModified(ArrayList<Statement> stats, HashSet<RectangleObject> objs)
			throws ConversionException
	{
		for (Statement s : stats)
		{
			if (s.getType().equals(StatementType.north))
			{
				modifyIfInList(s, Helper.asDirection(StatementType.north), objs);
			}
			else if (s.getType().equals(StatementType.south))
			{
				modifyIfInList(s, Helper.asDirection(StatementType.south), objs);
			}
			else if (s.getType().equals(StatementType.east))
			{
				modifyIfInList(s, Helper.asDirection(StatementType.east), objs);
			}
			else if (s.getType().equals(StatementType.west))
			{
				modifyIfInList(s, Helper.asDirection(StatementType.west), objs);
			}
		}
	}
	
	private void modifyIfInList(Statement stat, Direction dir,
			HashSet<RectangleObject> objs)
	{
		
		String nameOfAssoc = stat.getParameter(0);
		// Search 'nameOfAssoc' in '_assocs'
		LineAssociation toModify = _assocs.stream()
				.filter(a -> a.getId().equals(nameOfAssoc)).collect(Collectors.toList())
				.get(0);
		Integer indexOfAssoc = _assocs.indexOf(toModify);
		
		String nameOfObject = stat.getParameter(1);
		// Search 'nameOfObject' in 'objs'
		RectangleObject ob = objs.stream().filter(o -> o.getName().equals(nameOfObject))
				.collect(Collectors.toList()).get(0);
		
		ArrayList<Point> route = toModify.getRoute();
		if (toModify.getRoute(RouteConfig.START).equals(
				transformDimension(ob.getPosition())))
		{
			route.set(0, Point.Add(transformDimension(ob.getPosition()), dir));
			_modifies.put(new Pair<String, LineAssociation.RouteConfig>(toModify.getId(),
					RouteConfig.START), transformDimension(ob.getPosition()));
		}
		if (toModify.getRoute(RouteConfig.END).equals(
				transformDimension(ob.getPosition())))
		{
			route.set(1, Point.Add(transformDimension(ob.getPosition()), dir));
			_modifies.put(new Pair<String, LineAssociation.RouteConfig>(toModify.getId(),
					RouteConfig.END), transformDimension(ob.getPosition()));
		}
		toModify.setRoute(route);
		
		// Set in List
		_assocs.set(indexOfAssoc, toModify);
	}
	
	private Point transformDimension(Point p)
	{
		return Point.Multiply(p, _transformAmount);
	}
	
	public HashSet<LineAssociation> value()
	{
		return (HashSet<LineAssociation>) _assocs.stream().collect(Collectors.toSet());
	}
	
	public static void test()
	{
		try
		{
			HashSet<RectangleObject> o = new HashSet<RectangleObject>();
			// o.add(new RectangleObject("A", new Point(0, 1)));
			// o.add(new RectangleObject("B", new Point(-1, 0)));
			o.add(new RectangleObject("C", new Point(0, 0)));
			o.add(new RectangleObject("D", new Point(0, -1)));
			
			HashSet<LineAssociation> a = new HashSet<LineAssociation>();
			// a.add(new LineAssociation("AtoB", "A", "B", new Point(0, 1),
			// new
			// Point(-1, 0)));
			// a.add(new LineAssociation("BtoC", "B", "C", new Point(-1, 0),
			// new
			// Point(0, 0)));
			// a.add(new LineAssociation("BtoD", "B", "D", new Point(-1, 0),
			// new
			// Point(0, -1)));
			a.add(new LineAssociation("CtoD1", "C", "D", new Point(0, 0),
					new Point(0, -1)));
			a.add(new LineAssociation("CtoD2", "C", "D", new Point(0, 0),
					new Point(0, -1)));
			a.add(new LineAssociation("CtoD3", "C", "D", new Point(0, 0),
					new Point(0, -1)));
			a.add(new LineAssociation("CtoD4", "C", "D", new Point(0, 0),
					new Point(0, -1)));
			// a.add(new LineAssociation("CtoD5", "C", "D", new Point(0, 0), new
			// Point(0, -1)));
			
			ArrayList<Statement> s = new ArrayList<Statement>();
			// s.add(Statement.Parse("priority(BtoC, 50)"));
			// s.add(Statement.Parse("priority(BtoC, 20)"));
			// s.add(Statement.Parse("north(BtoD, D)"));
			s.add(Statement.Parse("north(CtoD2, C)"));
			s.add(Statement.Parse("priority(CtoD2, 10)"));
			
			ArrangeAssociations aa = new ArrangeAssociations(o, a, s);
			for (LineAssociation as : aa.value())
			{
				System.out.println(as.getId() + ": " + as.getFrom() + " - " + as.getTo()
						+ ":");
				boolean first = true;
				for (Point p : as.getRoute())
				{
					if (first)
						first = false;
					else
						System.out.print(" -> ");
					System.out.print(p.toString());
				}
				System.out.println();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		catch (MyException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
