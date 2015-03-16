package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation.RouteConfig;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain StatementType into
	 *             Direction.
	 * @throws InternalException
	 *             Throws if any error occurs which should not happen. Contact
	 *             developer!
	 * @throws CannotFindAssociationRouteException
	 */
	public ArrangeAssociations(Set<RectangleObject> diagramObjects,
			Set<LineAssociation> diagramAssocs, ArrayList<Statement> stats)
			throws ConversionException, InternalException,
			CannotFindAssociationRouteException
	{
		if (diagramAssocs == null || diagramAssocs.size() == 0 || diagramAssocs == null
				|| diagramAssocs.size() == 0)
			return;
		
		_transformAmount = 2;
		OLDarrange(diagramObjects, diagramAssocs, stats);
	}
	
	@SuppressWarnings("unused")
	private void arrange(Set<RectangleObject> diagramObjects,
			Set<LineAssociation> diagramAssocs, ArrayList<Statement> stats)
			throws ConversionException, InternalException
	{
		Set<Point> occupied = new HashSet<Point>();
		_assocs = (ArrayList<LineAssociation>) diagramAssocs.stream().collect(
				Collectors.toList());
		_modifies = new HashMap<Pair<String, RouteConfig>, Point>();
		
		// Count maxlinks on obejcts
		Integer maxLinks = maxLinks();
		
		// TODO
		// transformAmount páros
		// assocs= (object-2)*4 + 8
		// 2:> object=1, assocs=4;
		// 4:> object=3, assocs=12;
		// 6:> object=5, assocs=20;
		// 8:> object=7, assocs=;
		_transformAmount = getTransformAmount(maxLinks);
		if (_transformAmount < 2)
			_transformAmount = 2;
		
		Boolean repeat = true;
		while (repeat)
		{
			repeat = false;
			
			// Transform everything according to transform amount.
			// set object width rectangle
			for (RectangleObject obj : diagramObjects)
			{
				Point origin = transformDimension(obj.getPosition());
				
				// generate row
				ArrayList<Point> row = new ArrayList<Point>();
				for (int i = 1; i < _transformAmount - 1; ++i)
				{
					Point tempP = Point.Add(origin, Point.Multiply(Direction.east, i));
					Point tempM = Point.Add(origin, Point.Multiply(Direction.west, i));
					row.add(tempP);
					row.add(tempM);
				}
				occupied.addAll(row);
				
				// generate coloumns
				for (int i = 1; i < _transformAmount - 1; ++i)
				{
					Point tempN = Point.Multiply(Direction.north, i);
					Point tempS = Point.Multiply(Direction.south, i);
					
					ArrayList<Point> temp = new ArrayList<Point>(row);
					temp.forEach(p -> Point.Add(p, tempN));
					occupied.addAll(temp);
					temp = new ArrayList<Point>(row);
					temp.forEach(p -> Point.Add(p, tempS));
					occupied.addAll(temp);
				}
			}
			
			// Calculate maximum distance between objects
			Integer top = calcMaxDistance(occupied);
			
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
			try
			{
				// Graph Search
				for (int i = 0; i < _assocs.size(); ++i)
				{
					LineAssociation a = _assocs.get(i);
					Point START = a.getRoute(LineAssociation.RouteConfig.START);
					Point END = a.getRoute(LineAssociation.RouteConfig.END);
					Point BEFORE = _modifies.get(new Pair<String, RouteConfig>(a.getId(),
							RouteConfig.START));
					Point AFTER = _modifies.get(new Pair<String, RouteConfig>(a.getId(),
							RouteConfig.END));
					
					GraphSearch gs = new GraphSearch(START, END, occupied, BEFORE, AFTER,
							top);
					a.setRoute(gs.value());
					a.setTurns(gs.turns());
					a.setExtends(gs.extendsNum());
					_assocs.set(i, a);
					
					// Update occupied places with the route of this link
					if (a.getRoute().size() < 3)
						throw new InternalException("Route is shorter then 3!");
					
					for (int ri = 1; ri < a.getRoute().size() - 1; ++ri)
					{
						occupied.add(new Point(a.getRoute().get(ri)));
					}
				}
			}
			catch (CannotFindAssociationRouteException e)
			{
				repeat = true;
				_transformAmount = _transformAmount + 2;
			}
		}
		
	}
	
	private void OLDarrange(Set<RectangleObject> diagramObjects,
			Set<LineAssociation> diagramAssocs, ArrayList<Statement> stats)
			throws ConversionException, InternalException,
			CannotFindAssociationRouteException
	{
		Set<Point> occupied = new HashSet<Point>();
		_assocs = (ArrayList<LineAssociation>) diagramAssocs.stream().collect(
				Collectors.toList());
		_modifies = new HashMap<Pair<String, RouteConfig>, Point>();
		// Transform everything to new dimensions (*2)
		// Add objects transformed place to occupied list
		for (RectangleObject obj : diagramObjects)
		{
			occupied.add(transformDimension(obj.getPosition()));
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
			
			for (int ri = 1; ri < a.getRoute().size() - 1; ++ri)
			{
				occupied.add(new Point(a.getRoute().get(ri)));
			}
		}
	}
	
	private Integer getTransformAmount(Integer max)
	{
		Integer result = (int) Math.ceil(((max - 8) / 4) + 2);
		if (result % 2 == 0)
			return result;
		else
			return result + 1;
	}
	
	private Integer calcMaxDistance(Set<Point> grid)
	{
		Integer maxval = 0;
		
		for (Point p1 : grid)
		{
			for (Point p2 : grid)
			{
				int dx = Math.abs(p1.getX() - p2.getX());
				int dy = Math.abs(p1.getY() - p2.getY());
				if (dx > maxval)
					maxval = dx;
				if (dy > maxval)
					maxval = dy;
			}
		}
		
		return maxval;
	}
	
	private Integer maxLinks()
	{
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		Integer maxvalue = 0;
		
		for (LineAssociation a : _assocs)
		{
			if (temp.containsKey(a.getFrom()))
				temp.put(a.getFrom(), temp.get(a.getFrom()) + 1);
			else
				temp.put(a.getFrom(), 1);
			
			if (maxvalue < temp.get(a.getFrom()))
				maxvalue = temp.get(a.getFrom());
			
			if (temp.containsKey(a.getTo()))
				temp.put(a.getTo(), temp.get(a.getTo()) + 1);
			else
				temp.put(a.getTo(), 1);
			
			if (maxvalue < temp.get(a.getTo()))
				maxvalue = temp.get(a.getTo());
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
	
	private void setModified(ArrayList<Statement> stats, Set<RectangleObject> objs)
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
	
	private void modifyIfInList(Statement stat, Direction dir, Set<RectangleObject> objs)
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
	
	public Set<LineAssociation> value()
	{
		return (Set<LineAssociation>) _assocs.stream().collect(Collectors.toSet());
	}
	
}
