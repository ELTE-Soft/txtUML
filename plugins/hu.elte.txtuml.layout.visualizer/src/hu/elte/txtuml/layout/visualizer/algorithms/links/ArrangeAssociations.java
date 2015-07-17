package hu.elte.txtuml.layout.visualizer.algorithms.links;

import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Node;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Painted;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Painted.Colors;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.events.ProgressManager;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation.RouteConfig;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class arranges the lines of links.
 * 
 * @author Balázs Gregorics
 *
 */
public class ArrangeAssociations
{
	private Integer _widthOfObjects;
	
	private Set<RectangleObject> _objects;
	private List<LineAssociation> _assocs;
	private HashMap<Pair<String, RouteConfig>, HashSet<Point>> _possibleStarts;
	private HashMap<String, HashSet<Integer>> _batches;
	
	private Integer _gId;
	private Boolean _batching;
	private Boolean _logging;
	private Integer _corridorPercent;
	
	/**
	 * Gets the final width of the boxes, that was computed during the running
	 * of the ArrangeAssociation algo.rithm
	 * 
	 * @return Integer number of the width of boxes.
	 */
	public Integer getWidthAmount()
	{
		return _widthOfObjects;
	}
	
	/**
	 * Returns the last use Group Id number.
	 * 
	 * @return the last use Group Id number.
	 */
	public Integer getGId()
	{
		return _gId;
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
	 * @param gid
	 *            The max group id yet existing.
	 * @param corridor
	 *            THe percent of corridor's with relative to boxes.
	 * @param batch
	 *            Whether to use batching or not.
	 * @param log
	 *            Whether to print out logging msgs.
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain StatementType into
	 *             Direction.
	 * @throws InternalException
	 *             Throws if any error occurs which should not happen. Contact
	 *             developer!
	 * @throws CannotFindAssociationRouteException
	 *             Throws if a certain link's path cannot be found.
	 * @throws UnknownStatementException
	 *             Throws if some unkown statements are found during processing.
	 */
	public ArrangeAssociations(Set<RectangleObject> diagramObjects,
			Set<LineAssociation> diagramAssocs, List<Statement> stats, Integer gid,
			Integer corridor, Boolean batch, Boolean log) throws ConversionException,
			InternalException, CannotFindAssociationRouteException,
			UnknownStatementException
	{
		// Nothing to arrange
		if (diagramAssocs == null || diagramAssocs.size() == 0)
			return;
		
		_gId = gid;
		_batching = batch;
		_logging = log;
		_corridorPercent = corridor;
		
		_widthOfObjects = 1;
		
		// Emit Event
		ProgressManager.getEmitter().OnLinkArrangeStart();
		
		arrange(diagramObjects, diagramAssocs, stats);
		
		// Emit Event
		ProgressManager.getEmitter().OnLinkArrangeEnd();
	}
	
	private void arrange(Set<RectangleObject> par_objects,
			Set<LineAssociation> diagramAssocs, List<Statement> par_statements)
			throws ConversionException, InternalException
	{
		Set<RectangleObject> diagramObjects = new HashSet<RectangleObject>(par_objects);
		List<Statement> statements = new ArrayList<Statement>(par_statements);
		
		_possibleStarts = new HashMap<Pair<String, RouteConfig>, HashSet<Point>>();
		_objects = diagramObjects;
		_assocs = Helper.cloneLinkList(diagramAssocs.stream()
				.collect(Collectors.toList()));
		_batches = setBatches();
		
		// Inflate diagram to start with a object width enough for the
		// maximum number of links
		Integer maxLinks = calculateMaxLinks(_assocs);
		diagramObjects = defaultGrid(maxLinks, diagramObjects);
		
		// Pre-define priorities
		
		DefaultAssocStatements das = new DefaultAssocStatements(_gId, statements, _assocs);
		statements = das.value();
		_gId = das.getGroupId();
		
		Boolean repeat = true;
		
		// Arrange until ok
		Integer maxTryCount = 100;
		while (repeat && maxTryCount >= 0)
		{
			--maxTryCount;
			
			// Process statements, priority and direction
			_assocs = processStatements(_assocs, statements, diagramObjects);
			repeat = false;
			
			try
			{
				// Search for the route of every Link
				arrangeLinks(diagramObjects);
			}
			catch (CannotStartAssociationRouteException
					| CannotFindAssociationRouteException e)
			{
				if (_logging)
					System.err.println("(Normal) Expanding grid!");
				
				repeat = true;
				// Grid * 2
				diagramObjects = expandGrid(diagramObjects);
				continue;
			}
		}
		
		_objects = diagramObjects;
	}
	
	/**
	 * Create the {@link HashMap} which determines which {@link LineAssociation}
	 * s can go
	 * batched together.
	 * 
	 * @return the {@link HashMap} which determines which
	 *         {@link LineAssociation}s can go
	 *         batched together.
	 */
	private HashMap<String, HashSet<Integer>> setBatches()
	{
		HashMap<String, HashSet<Integer>> result = new HashMap<String, HashSet<Integer>>();
		
		Integer id = 1;
		
		for (LineAssociation a : _assocs)
		{
			if (!result.containsKey(a.getId()))
			{
				HashSet<LineAssociation> sameKinds = new HashSet<LineAssociation>();
				sameKinds.add(a);
				if (_batching)
				{
					sameKinds.addAll(_assocs
							.stream()
							.filter(as -> !as.isReflexive()
									&& !as.getType().equals(AssociationType.normal)
									&& as.getType().equals(a.getType())
									&& as.getFrom().equals(a.getFrom()))
							.collect(Collectors.toSet()));
				}
				
				for (LineAssociation same : sameKinds)
				{
					if (result.containsKey(same.getId()))
					{
						HashSet<Integer> temp = result.get(same.getId());
						temp.add(id);
						result.put(same.getId(), temp);
					}
					else
					{
						HashSet<Integer> temp = new HashSet<Integer>();
						temp.add(id);
						result.put(same.getId(), temp);
					}
				}
				++id;
			}
		}
		
		return result;
	}
	
	private Set<RectangleObject> defaultGrid(Integer k, Set<RectangleObject> objs)
	{
		Set<RectangleObject> result = new HashSet<RectangleObject>();
		_widthOfObjects = k + 2;
		
		for (RectangleObject o : objs)
		{
			RectangleObject mod = new RectangleObject(o);
			mod.setPosition(Point.Multiply(
					o.getPosition(),
					2 + (int) Math.floor((double) k * ((double) _corridorPercent / 100.0)
							* 2.0)));
			mod.setWidth(_widthOfObjects);
			result.add(mod);
		}
		
		for (int i = 0; i < _assocs.size(); ++i)
		{
			LineAssociation mod = _assocs.get(i);
			ArrayList<Point> route = new ArrayList<Point>();
			route.add(result.stream().filter(o -> o.getName().equals(mod.getFrom()))
					.findFirst().get().getPosition());
			route.add(result.stream().filter(o -> o.getName().equals(mod.getTo()))
					.findFirst().get().getPosition());
			mod.setRoute(route);
			_assocs.set(i, mod);
		}
		
		if (_logging)
			System.err.println("(Default) Expanding Grid!");
		
		return result;
	}
	
	private Set<RectangleObject> expandGrid(Set<RectangleObject> objs)
			throws ConversionException
	{
		Set<RectangleObject> result = new HashSet<RectangleObject>();
		
		for (RectangleObject o : objs)
		{
			RectangleObject mod = new RectangleObject(o);
			mod.setPosition(Point.Multiply(mod.getPosition(), 2));
			mod.setWidth(mod.getWidth() * 2);
			result.add(mod);
		}
		
		for (LineAssociation mod : _assocs)
		{
			ArrayList<Point> route = new ArrayList<Point>();
			for (int j = 0; j < mod.getRoute().size(); ++j)
			{
				Point p = new Point(mod.getRoute().get(j));
				
				Point temp = Point.Multiply(p, 2);
				
				if (mod.isPlaced() && j > 1 && j < mod.getRoute().size() - 1)
				{
					Direction beforeDirection = Helper.asDirection(Point.Substract(mod
							.getRoute().get(j - 1), mod.getRoute().get(j)));
					Point before = Point.Add(temp, beforeDirection);
					route.add(before);
				}
				
				route.add(temp);
			}
			
			mod.setRoute(route);
		}
		
		return result;
	}
	
	private Integer calculateMaxLinks(List<LineAssociation> as)
	{
		if (as.size() == 0)
			return 0;
		
		// Gather data
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		
		Integer countMod = 1;
		for (LineAssociation a : as)
		{
			// From
			if (data.containsKey(a.getFrom()))
			{
				data.put(a.getFrom(), data.get(a.getFrom()) + countMod);
			}
			else
			{
				data.put(a.getFrom(), countMod);
			}
			
			if (a.isReflexive())
				continue;
			// To
			if (data.containsKey(a.getTo()))
			{
				data.put(a.getTo(), data.get(a.getTo()) + countMod);
			}
			else
			{
				data.put(a.getTo(), countMod);
			}
		}
		
		// Find max
		Integer max = data.entrySet().stream()
				.max((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).get()
				.getValue();
		
		return max;
	}
	
	private List<LineAssociation> processStatements(List<LineAssociation> links,
			List<Statement> stats, Set<RectangleObject> objs) throws ConversionException,
			InternalException
	{
		if (stats != null && stats.size() != 0)
		{
			// Set priority
			HashMap<String, Integer> priorityMap = setPriorityMap(stats);
			
			// Order based on priority
			links.sort((a1, a2) ->
			{
				if (priorityMap.containsKey(a1.getId()))
				{
					if (priorityMap.containsKey(a2.getId()))
					{
						return -1
								* Integer.compare(priorityMap.get(a1.getId()),
										priorityMap.get(a2.getId()));
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
			
			ArrayList<Statement> priorityless = new ArrayList<Statement>(stats);
			priorityless.removeIf(s -> s.getType().equals(StatementType.priority));
			
			// Set starts/ends for statemented assocs
			setPossibles(links, priorityless, objs);
		}
		
		return links;
	}
	
	private Set<Node> setSet(Pair<String, RouteConfig> key, Point start, Integer width,
			Set<Point> occupied, Boolean isReflexive, Boolean isStart)
	{
		Set<Point> result = new HashSet<Point>();
		
		// Statement given points
		if (_possibleStarts.containsKey(key))
			result.addAll(_possibleStarts.get(key));
		
		// Add Object's points
		RectangleObject tempObj = new RectangleObject("TEMP", start);
		tempObj.setWidth(width);
		
		if (result.size() == 0)
		{
			result.addAll(tempObj.getPerimiterPoints());
			if (isReflexive)
			{
				if (isStart)
					result = setReflexiveSet(result, tempObj, true);
				else
					result = setReflexiveSet(result, tempObj, false);
			}
		}
		
		// Remove occupied points
		result.removeIf(p -> occupied.contains(p));
		// Remove corner points
		result.removeIf(p -> Helper.isCornerPoint(p, tempObj));
		
		if (isStart)
			return convertToNodes(result, tempObj);
		else
			return convertToInvertedNodes(result, tempObj);
	}
	
	private Set<Point> setReflexiveSet(Set<Point> fromSet, RectangleObject obj,
			Boolean isStart)
	{
		Set<Point> result = new HashSet<Point>(fromSet);
		
		Integer halfway = ((obj.getWidth() % 2) == 0) ? ((obj.getWidth() / 2) - 1)
				: ((obj.getWidth() - 1) / 2);
		
		Point northern = Point.Add(obj.getPosition(), new Point(halfway, 0));
		if (isStart)
			result.removeIf(p -> p.getY().equals(northern.getY())
					&& p.getX() >= northern.getX());
		else
			result.removeIf(p -> p.getY().equals(northern.getY())
					&& p.getX() <= northern.getX());
		
		Point eastern = Point.Add(obj.getBottomRight(), new Point(0, halfway));
		if (isStart)
			result.removeIf(p -> p.getX().equals(eastern.getX())
					&& p.getY() <= eastern.getY());
		else
			result.removeIf(p -> p.getX().equals(eastern.getX())
					&& p.getY() >= eastern.getY());
		
		Point southern = Point.Add(obj.getBottomRight(), new Point(-1 * halfway, 0));
		if (isStart)
			result.removeIf(p -> p.getY().equals(southern.getY())
					&& p.getX() <= southern.getX());
		else
			result.removeIf(p -> p.getY().equals(southern.getY())
					&& p.getX() >= southern.getX());
		
		Point western = Point.Add(obj.getPosition(), new Point(0, -1 * halfway));
		if (isStart)
			result.removeIf(p -> p.getX().equals(western.getX())
					&& p.getY() >= western.getY());
		else
			result.removeIf(p -> p.getX().equals(western.getX())
					&& p.getY() <= western.getY());
		
		return result;
	}
	
	private Set<Node> convertToNodes(Set<Point> ps, RectangleObject obj)
	{
		Set<Node> result = new HashSet<Node>();
		
		for (Point p : ps)
		{
			if (obj.getTopLeft().getX().equals(p.getX()))
				result.add(new Node(p, Point.Add(p, Direction.west)));
			else if (obj.getTopLeft().getY().equals(p.getY()))
				result.add(new Node(p, Point.Add(p, Direction.north)));
			else if (obj.getBottomRight().getX().equals(p.getX()))
				result.add(new Node(p, Point.Add(p, Direction.east)));
			else if (obj.getBottomRight().getY().equals(p.getY()))
				result.add(new Node(p, Point.Add(p, Direction.south)));
		}
		
		return result;
	}
	
	private Set<Node> convertToInvertedNodes(Set<Point> ps, RectangleObject obj)
	{
		Set<Node> result = new HashSet<Node>();
		
		for (Point p : ps)
		{
			if (obj.getTopLeft().getX().equals(p.getX()))
				result.add(new Node(Point.Add(p, Direction.west), p));
			else if (obj.getTopLeft().getY().equals(p.getY()))
				result.add(new Node(Point.Add(p, Direction.north), p));
			else if (obj.getBottomRight().getX().equals(p.getX()))
				result.add(new Node(Point.Add(p, Direction.east), p));
			else if (obj.getBottomRight().equals(p.getY()))
				result.add(new Node(Point.Add(p, Direction.south), p));
		}
		
		return result;
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
	
	private HashMap<String, Integer> setPriorityMap(List<Statement> stats)
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
	
	private void setPossibles(List<LineAssociation> links, List<Statement> stats,
			Set<RectangleObject> objs) throws ConversionException, InternalException
	{
		for (Statement s : stats)
		{
			try
			{
				LineAssociation link = links.stream()
						.filter(a -> a.getId().equals(s.getParameter(0))).findFirst()
						.get();
				RectangleObject obj = objs.stream()
						.filter(o -> o.getName().equals(s.getParameter(1))).findFirst()
						.get();
				if (link.getFrom().equals(obj.getName())
						&& (s.getParameters().size() == 2 || s.getParameter(2)
								.toLowerCase().equals("start")))
				{
					// RouteConfig.START
					Point startPoint = getStartingPoint(Helper.asDirection(s.getType()),
							obj);
					Direction moveDir = getMoveDirection(s.getType());
					generatePossiblePoints(link, obj, startPoint, moveDir,
							RouteConfig.START);
				}
				if (link.getTo().equals(obj.getName())
						&& (s.getParameters().size() == 2 || s.getParameter(2)
								.toLowerCase().equals("end")))
				{
					// RouteConfig.END
					Point startPoint = getStartingPoint(Helper.asDirection(s.getType()),
							obj);
					Direction moveDir = getMoveDirection(s.getType());
					generatePossiblePoints(link, obj, startPoint, moveDir,
							RouteConfig.END);
				}
			}
			catch (NoSuchElementException e)
			{
				throw new InternalException("Inner Exception: [" + e.getMessage() + "], "
						+ "Probably a statment shouldn't have reached this code!");
			}
		}
	}
	
	private Point getStartingPoint(Direction dir, RectangleObject o)
			throws InternalException
	{
		if (dir.equals(Direction.north) || dir.equals(Direction.west))
			return o.getPosition();
		else if (dir.equals(Direction.south) || dir.equals(Direction.east))
			return o.getBottomRight();
		else
			throw new InternalException("Unknown Direction!");
	}
	
	private Direction getMoveDirection(StatementType ty) throws InternalException
	{
		switch (ty)
		{
			case north:
				return Direction.east;
			case west:
				return Direction.south;
			case south:
				return Direction.west;
			case east:
				return Direction.north;
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
				throw new InternalException("Cannot evaluate MoveDirection for "
						+ ty.toString() + "!");
		}
	}
	
	private void generatePossiblePoints(LineAssociation toModify,
			RectangleObject connectsTo, Point first, Direction toMove, RouteConfig r)
	{
		HashSet<Point> points = new HashSet<Point>();
		
		for (int i = 0; i < connectsTo.getWidth(); ++i)
		{
			points.add(Point.Add(first, Point.Multiply(toMove, i)));
		}
		Pair<String, RouteConfig> key = new Pair<String, LineAssociation.RouteConfig>(
				toModify.getId(), r);
		
		_possibleStarts.put(key, points);
	}
	
	private void arrangeLinks(Set<RectangleObject> diagramObjects)
			throws CannotStartAssociationRouteException,
			CannotFindAssociationRouteException, InternalException, ConversionException
	{
		Set<Painted<Point>> occupiedLinks = new HashSet<Painted<Point>>();
		
		Integer c = 0;
		for (LineAssociation a : _assocs)
		{
			++c;
			if (_logging)
				System.err.print(c + "/" + _assocs.size() + ": " + a.getId() + " ... ");
			
			if (a.isPlaced())
			{
				if (_logging)
					System.err.println("NOTHING TO DO!");
				continue;
			}
			
			Point START = a.getRoute(LineAssociation.RouteConfig.START);
			Point END = a.getRoute(LineAssociation.RouteConfig.END);
			Integer WIDTH = diagramObjects.stream().findFirst().get().getWidth();
			Set<Integer> myBatch = _batches.get(a.getId());
			
			Set<Node> STARTSET = setSet(
					new Pair<String, RouteConfig>(a.getId(), RouteConfig.START),
					START,
					WIDTH,
					occupiedLinks
							.stream()
							.filter(pp -> pp.Color.equals(Colors.Red)
									&& !pp.Batch.stream().anyMatch(
											b -> myBatch.contains(b))).map(p -> p.Inner)
							.collect(Collectors.toSet()), a.isReflexive(), true);
			Set<Node> ENDSET = setSet(
					new Pair<String, RouteConfig>(a.getId(), RouteConfig.END),
					END,
					WIDTH,
					occupiedLinks
							.stream()
							.filter(pp -> pp.Color.equals(Colors.Red)
									&& !pp.Batch.stream().anyMatch(
											b -> myBatch.contains(b))).map(p -> p.Inner)
							.collect(Collectors.toSet()), a.isReflexive(), false);
			
			// Assemble occupied points
			Set<Painted<Point>> OBJS = new HashSet<Painted<Point>>();
			OBJS.addAll(occupiedLinks);
			// Add objects transformed place to occupied list
			Set<Painted<Point>> occupied = new HashSet<Painted<Point>>();
			for (RectangleObject obj : diagramObjects)
			{
				occupied.addAll(obj.getPoints().stream()
						.map(p -> new Painted<Point>(Colors.Red, p))
						.collect(Collectors.toSet()));
			}
			OBJS.addAll(occupied);
			
			// Maximum distance between objects
			Integer top = calcMaxDistance(occupied.stream().map(p -> p.Inner)
					.collect(Collectors.toSet()));
			
			// Search for the route
			if (STARTSET.size() == 0 || ENDSET.size() == 0)
			{
				// Cannot start or end
				throw new CannotStartAssociationRouteException(
						"Cannot get out of start, or cannot enter end!");
			}
			
			GraphSearch gs = new GraphSearch(STARTSET, ENDSET, OBJS, top, _batches.get(a
					.getId()));
			a.setRoute(convertFromNodes(gs.value(), START, END));
			a.setExtends(gs.extendsNum());
			
			if (_logging)
				System.err.println("DONE!");
			
			// Update occupied places with the route of this link
			if (a.getRoute().size() < 3)
				throw new InternalException("Route is shorter then 3!");
			
			for (int ri = 1; ri < a.getRoute().size() - 1; ++ri)
			{
				if (!Point.Substract(a.getRoute().get(ri - 1), a.getRoute().get(ri))
						.equals(Point.Substract(a.getRoute().get(ri),
								a.getRoute().get(ri + 1))))
				{
					occupiedLinks.add(new Painted<Point>(Colors.Red, new Point(a
							.getRoute().get(ri)), _batches.get(a.getId())));
				}
				else
				{
					occupiedLinks.add(new Painted<Point>(Colors.Yellow, new Point(a
							.getRoute().get(ri)), _batches.get(a.getId())));
				}
			}
		}
	}
	
	private ArrayList<Point> convertFromNodes(ArrayList<Node> nodes, Point start,
			Point end)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		
		result.add(start);
		result.add(new Point(nodes.get(0).getFrom()));
		/*
		 * result.add(new Point(Point.Add(nodes.get(0).getTo(),
		 * Direction.opposite(Point
		 * .directionOf(nodes.get(0).getTo(), nodes.get(0).getFrom())))));
		 */
		
		for (int i = 1; i < nodes.size(); ++i)
		{
			result.add(new Point(nodes.get(i).getFrom()));
		}
		
		result.add(new Point(nodes.get(nodes.size() - 1).getTo()));
		result.add(end);
		
		return result;
	}
	
	/**
	 * Returns the final value of the lines of links.
	 * 
	 * @return Set of the arranged lines of links.
	 */
	public Set<LineAssociation> value()
	{
		Set<LineAssociation> result = _assocs.stream().collect(Collectors.toSet());
		
		return result;
	}
	
	/**
	 * Returns the final positions and width of the boxes.
	 * 
	 * @return the final positions and width of the boxes.
	 */
	public Set<RectangleObject> objects()
	{
		return _objects;
	}
	
}
