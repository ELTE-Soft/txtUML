package hu.elte.txtuml.layout.visualizer.algorithms;

import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Painted;
import hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers.Painted.Colors;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.annotations.StatementLevel;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
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
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class ArrangeAssociations
{
	private Integer _widthOfObjects;
	private Integer _transformAmount;
	
	private ArrayList<LineAssociation> _assocs;
	private ArrayList<LineAssociation> _reflexives;
	private HashMap<Pair<String, RouteConfig>, HashSet<Point>> _possibleStarts;
	
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
	
	public Integer getWidthAmount()
	{
		return _widthOfObjects;
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
	 * @param onSameSide
	 *            Whether the reflexive links should start and end on the same
	 *            side of the object or not.
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
			Set<LineAssociation> diagramAssocs, ArrayList<Statement> stats,
			boolean onSameSide) throws ConversionException, InternalException,
			CannotFindAssociationRouteException, UnknownStatementException
	{
		if (diagramAssocs == null || diagramAssocs.size() == 0 || diagramAssocs == null
				|| diagramAssocs.size() == 0)
			return;
		
		_transformAmount = 2;
		_widthOfObjects = 1;
		
		arrange(diagramObjects, diagramAssocs, stats, onSameSide);
	}
	
	private void arrange(Set<RectangleObject> diagramObjects,
			Set<LineAssociation> diagramAssocs, ArrayList<Statement> stats,
			boolean onSameSide) throws ConversionException, InternalException,
			UnknownStatementException
	{
		
		if (diagramAssocs.size() != 0)
		{
			_possibleStarts = new HashMap<Pair<String, RouteConfig>, HashSet<Point>>();
			
			// Slpit reflexive and other links
			// exception reflexives: have two direction statements
			Set<String> exceptionReflexives = getExceptionReflexives(stats);
			
			_reflexives = (ArrayList<LineAssociation>) diagramAssocs
					.stream()
					.filter(a -> !exceptionReflexives.contains(a.getId())
							&& a.isReflexive()).collect(Collectors.toList());
			ArrayList<Statement> reflexiveStats = (ArrayList<Statement>) stats
					.stream()
					.filter(s -> _reflexives.stream().anyMatch(
							a -> a.getId().equals(s.getParameter(0))))
					.collect(Collectors.toList());
			stats.removeIf(s -> reflexiveStats.contains(s));
			_assocs = Helper.cloneLinkList((ArrayList<LineAssociation>) diagramAssocs
					.stream().collect(Collectors.toList()));
			_assocs.removeIf(a -> _reflexives.contains(a));
			
			ArrayList<LineAssociation> originalAssocs = Helper.cloneLinkList(_assocs);
			ArrayList<LineAssociation> originalReflexives = Helper
					.cloneLinkList(_reflexives);
			
			// Transform Links' start and end route point
			transformAssocs();
			transformReflexives();
			diagramObjects = updateObjects(diagramObjects);
			
			// Inflate diagram to start with a object width enough for the
			// maximum number of links
			Integer maxLinks = calculateMaxLinks(_assocs, 1);
			Integer maxReflexives = transformReflexiveCount(calculateMaxLinks(
					_reflexives, 1));
			Integer maxSide = Math.max(maxLinks, maxReflexives);
			for (int i = 4; i <= maxSide || _widthOfObjects < 3; i = i + 8)
			{
				if (_widthOfObjects == 1)
				{
					i = i - 8;
				}
				// Grid * 2, ObjectWidth * 2 + 1
				_transformAmount = _transformAmount * 2;
				diagramObjects = enlargeObjects(diagramObjects);
				_assocs = Helper.cloneLinkList(originalAssocs);
				_reflexives = Helper.cloneLinkList(originalReflexives);
				transformAssocs();
				transformReflexives();
				System.err.println("(Default) Expanding Grid!");
			}
			
			// Pre-define priorities
			stats = predefinePriorities(stats);
			
			Boolean repeat = true;
			
			// Arrange normal links
			while (repeat)
			{
				// Arrange reflexive links
				
				try
				{
					// TODO
					for (int i = 0; false && i < _reflexives.size(); ++i)
					{
						LineAssociation refl = _reflexives.get(i);
						
						System.err.print((i + 1) + "/" + _reflexives.size() + ": "
								+ refl.getId() + " ... ");
						
						Set<LineAssociation> myReflexives = _reflexives
								.stream()
								.filter(a -> a.isPlaced()
										&& a.getFrom().equals(refl.getFrom()))
								.collect(Collectors.toSet());
						refl.setRoute(arrangeReflexive(refl, diagramObjects, myReflexives));
						
						System.err.println("DONE!");
						
						_reflexives.set(i, refl);
					}
				}
				catch (CannotFindAssociationRouteException e)
				{
					System.err.println("(Reflexive) Expanding grid!");
					repeat = true;
					// Grid * 2, ObjectWidth * 2 + 1
					_transformAmount = _transformAmount * 2;
					diagramObjects = enlargeObjects(diagramObjects);
					_assocs = Helper.cloneLinkList(originalAssocs);
					_reflexives = Helper.cloneLinkList(originalReflexives);
					transformAssocs();
					transformReflexives();
					continue;
				}
				
				// Process statements, priority and direction
				_assocs = processStatements(_assocs, stats, diagramObjects);
				repeat = false;
				
				try
				{
					// Search for the route of every Link
					arrangeLinks(diagramObjects);
				}
				catch (CannotStartAssociationRouteException
						| CannotFindAssociationRouteException e)
				{
					System.err.println("(Normal) Expanding grid!");
					repeat = true;
					// Grid * 2, ObjectWidth * 2 + 1
					_transformAmount = _transformAmount * 2;
					diagramObjects = enlargeObjects(diagramObjects);
					_assocs = Helper.cloneLinkList(originalAssocs);
					_reflexives = Helper.cloneLinkList(originalReflexives);
					transformAssocs();
					transformReflexives();
					continue;
				}
			}
		}
	}
	
	private Double distanceOfEnds(LineAssociation a)
	{
		Point from = a.getRoute(RouteConfig.START);
		Point to = a.getRoute(RouteConfig.END);
		
		return Point.Substract(from, to).length();
	}
	
	private Integer calculateMaxLinks(ArrayList<LineAssociation> as, Integer countAs)
	{
		return calculateMaxLinks(as, countAs, null);
	}
	
	private Integer calculateMaxLinks(ArrayList<LineAssociation> as, Integer countAs,
			Set<String> exceptions)
	{
		if (as.size() == 0)
			return 0;
		
		// Gather data
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		
		for (LineAssociation a : as)
		{
			Integer countMod = countAs;
			if (exceptions != null && exceptions.contains(a))
				countMod = 2;
			
			// From
			if (data.containsKey(a.getFrom()))
			{
				data.put(a.getFrom(), data.get(a.getFrom()) + countMod);
			}
			else
			{
				data.put(a.getFrom(), countMod);
			}
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
	
	private Set<String> getExceptionReflexives(ArrayList<Statement> stats)
			throws InternalException, UnknownStatementException
	{
		Set<String> result = new HashSet<String>();
		
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		for (Statement s : stats)
		{
			if (s.getParameters().size() < 3)
				continue;
			
			if (data.containsKey(s.getParameter(0)))
			{
				if (data.get(s.getParameter(0)) == 1)
				{
					if (s.getParameter(2).toLowerCase().equals("end"))
					{
						data.put(s.getParameter(0), 3);
						result.add(s.getParameter(0));
					}
					else
						throw new InternalException("Too many direction statements on "
								+ s.getParameter(0) + "!");
				}
				else if (data.get(s.getParameter(0)) == 2)
				{
					if (s.getParameter(2).toLowerCase().equals("start"))
					{
						data.put(s.getParameter(0), 3);
						result.add(s.getParameter(0));
					}
					else
						throw new InternalException("Too many direction statements on "
								+ s.getParameter(0) + "!");
				}
				else
					throw new InternalException("Too many direction statements on "
							+ s.getParameter(0) + "!");
			}
			else
			{
				if (s.getParameter(2).toLowerCase().equals("start"))
				{
					data.put(s.getParameter(0), 1);
				}
				else if (s.getParameter(2).toLowerCase().equals("end"))
				{
					data.put(s.getParameter(0), 2);
				}
				else
					throw new UnknownStatementException("Unknown statement parameter: "
							+ s.toString() + "!");
			}
		}
		
		return result;
	}
	
	private ArrayList<Statement> predefinePriorities(ArrayList<Statement> stats)
	{
		ArrayList<Statement> result = stats;
		
		// select min priority
		Set<Statement> priorities = result.stream()
				.filter(s -> s.getType().equals(StatementType.priority))
				.collect(Collectors.toSet());
		Optional<Integer> minPriority = result.stream()
				.filter(s -> s.getType().equals(StatementType.priority))
				.map(s -> Integer.valueOf(s.getParameter(1))).min((i, j) ->
				{
					return Integer.compare(i, j);
				});
		if (minPriority.isPresent())
		{
			Integer min = minPriority.get();
			if ((_assocs.size() - priorities.size()) >= min)
			{
				// not good, ++ every prior
				Integer alterAmount = (_assocs.size() - priorities.size());
				for (Statement s : result)
				{
					if (s.getType().equals(StatementType.priority))
					{
						s.setParameter(1, ""
								+ (Integer.valueOf(s.getParameter(1)) + alterAmount));
					}
				}
			}
		}
		
		Integer actPrior = 0;
		ArrayList<LineAssociation> orderedAssocs = (ArrayList<LineAssociation>) _assocs
				.stream().collect(Collectors.toList());
		orderedAssocs.sort((a1, a2) ->
		{
			return -1 * Double.compare(distanceOfEnds(a1), distanceOfEnds(a2));
		});
		for (LineAssociation a : orderedAssocs)
		{
			if (!priorities.stream().anyMatch(s -> s.getParameter(0).equals(a.getId())))
			{
				result.add(new Statement(StatementType.priority, StatementLevel.Low, a
						.getId(), actPrior.toString()));
				++actPrior;
			}
		}
		
		return result;
	}
	
	private void transformAssocs()
	{
		for (int i = 0; i < _assocs.size(); ++i)
		{
			LineAssociation mod = _assocs.get(i);
			ArrayList<Point> temp = mod.getRoute();
			ArrayList<Point> route = new ArrayList<Point>();
			for (Point p : temp)
			{
				route.add(Point.Multiply(p, _transformAmount));
			}
			mod.setRoute(route);
			_assocs.set(i, mod);
		}
	}
	
	private void transformReflexives()
	{
		for (int i = 0; i < _reflexives.size(); ++i)
		{
			LineAssociation mod = _reflexives.get(i);
			ArrayList<Point> temp = mod.getRoute();
			ArrayList<Point> route = new ArrayList<Point>();
			for (Point p : temp)
			{
				route.add(Point.Multiply(p, _transformAmount));
			}
			mod.setRoute(route);
			_reflexives.set(i, mod);
		}
	}
	
	private Set<RectangleObject> updateObjects(Set<RectangleObject> objs)
	{
		Set<RectangleObject> result = new HashSet<RectangleObject>();
		
		for (RectangleObject o : objs)
		{
			RectangleObject temp = new RectangleObject(o);
			temp.setPosition(transformDimension(o.getPosition()));
			result.add(temp);
		}
		
		return result;
	}
	
	private HashMap<Direction, Integer> getLeastPopulatedDirection(RectangleObject obj,
			Set<RectangleObject> diagramObjects)
	{
		HashMap<Direction, Integer> arrangement = new HashMap<Direction, Integer>();
		
		for (Direction dir : Direction.values())
		{
			arrangement.put(dir, 0);
		}
		
		for (RectangleObject o : diagramObjects)
		{
			for (Direction dir : Direction.values())
			{
				if (Point.isInTheDirection(obj.getPosition(), o.getPosition(), dir))
				{
					arrangement.put(dir, arrangement.get(dir) + 1);
				}
			}
		}
		
		return arrangement;
	}
	
	private ArrayList<LineAssociation> processStatements(
			ArrayList<LineAssociation> links, ArrayList<Statement> stats,
			Set<RectangleObject> objs) throws ConversionException, InternalException
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
	
	private Set<Point> setStartSet(Pair<String, RouteConfig> key, Point start,
			Integer width, Set<Point> occupied)
	{
		Set<Point> statementPoints = _possibleStarts.get(key);
		if (statementPoints != null)
		{
			// Remove occupied points
			statementPoints.removeIf(p -> occupied.contains(p));
			// Remove corner points
			statementPoints.removeIf(p -> Math.abs(start.getX() - p.getX()) == Math
					.abs(start.getY() - p.getY()));
			return statementPoints;
		}
		
		Set<Point> result = new HashSet<Point>();
		
		if (width == 1)
		{
			result.add(Point.Add(start, Direction.north));
			result.add(Point.Add(start, Direction.east));
			result.add(Point.Add(start, Direction.south));
			result.add(Point.Add(start, Direction.west));
			
			result.removeIf(p -> occupied.contains(p));
			
			return result;
		}
		
		// Add Object's points
		RectangleObject tempObj = new RectangleObject("TEMP", start);
		tempObj.setWidth(width);
		result.addAll(tempObj.getPerimiterPoints());
		
		// Remove occupied points
		result.removeIf(p -> occupied.contains(p));
		// Remove corner points
		result.removeIf(p -> Helper.isCornerPoint(p, tempObj));
		
		return result;
	}
	
	private Set<Point> setEndSet(Pair<String, RouteConfig> key, Point end, Integer width,
			Set<Point> occupied)
	{
		Set<Point> result = _possibleStarts.get(key);
		
		RectangleObject temp = new RectangleObject("TEMP", end);
		temp.setWidth(width);
		
		if (result == null || result.size() == 0)
		{
			result = new HashSet<Point>();
			result.addAll(temp.getPoints());
		}
		
		// Other link's points
		result.removeIf(p -> occupied.contains(p));
		// Corners
		result.removeIf(p -> Helper.isCornerPoint(p, temp));
		
		return result;
	}
	
	private Set<RectangleObject> enlargeObjects(Set<RectangleObject> objs)
	{
		Set<RectangleObject> result = new HashSet<RectangleObject>();
		
		for (RectangleObject o : objs)
		{
			RectangleObject temp = new RectangleObject(o);
			_widthOfObjects = o.getWidth() + 2;
			temp.setPosition(Point.Multiply(o.getPosition(), 2));
			temp.setWidth(_widthOfObjects);
			result.add(temp);
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
	
	private void setPossibles(ArrayList<LineAssociation> links,
			ArrayList<Statement> stats, Set<RectangleObject> objs)
			throws ConversionException, InternalException
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
					if (obj.getWidth() == 1)
					{
						Pair<String, RouteConfig> tempKey = new Pair<String, RouteConfig>(
								link.getId(), RouteConfig.START);
						HashSet<Point> tempSet = new HashSet<Point>();
						tempSet.add(Point.Add(obj.getPosition(),
								Helper.asDirection(s.getType())));
						_possibleStarts.put(tempKey, tempSet);
					}
					else
					{
						Point startPoint = getStartingPoint(
								Helper.asDirection(s.getType()), obj);
						Direction moveDir = getMoveDirection(s.getType());
						generatePossiblePoints(link, obj, startPoint, moveDir,
								RouteConfig.START);
					}
				}
				if (link.getTo().equals(obj.getName())
						&& (s.getParameters().size() == 2 || s.getParameter(2)
								.toLowerCase().equals("end")))
				{
					// RouteConfig.END
					if (obj.getWidth() == 1)
					{
						Pair<String, RouteConfig> tempKey = new Pair<String, RouteConfig>(
								link.getId(), RouteConfig.END);
						HashSet<Point> tempSet = new HashSet<Point>();
						tempSet.add(Point.Add(obj.getPosition(),
								Helper.asDirection(s.getType())));
						_possibleStarts.put(tempKey, tempSet);
					}
					else
					{
						Point startPoint = getStartingPoint(
								Helper.asDirection(s.getType()), obj);
						Direction moveDir = getMoveDirection(s.getType());
						generatePossiblePoints(link, obj, startPoint, moveDir,
								RouteConfig.END);
					}
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
			return o.getTopLeft();
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
	
	private ArrayList<Point> arrangeReflexive(LineAssociation refl,
			Set<RectangleObject> diagramObjects, Set<LineAssociation> myReflexives)
			throws CannotFindAssociationRouteException
	{
		RectangleObject obj = diagramObjects.stream()
				.filter(o -> o.getName().equals(refl.getFrom())).findFirst().get();
		
		// get direction to put
		HashMap<Direction, Integer> directionPopulations = getLeastPopulatedDirection(
				obj, diagramObjects);
		
		// Compute direction to draw
		ArrayList<Entry<Direction, Integer>> sortedDirections = (ArrayList<Entry<Direction, Integer>>) directionPopulations
				.entrySet().stream().collect(Collectors.toList());
		sortedDirections.sort((e1, e2) ->
		{
			return Integer.compare(e1.getValue(), e2.getValue());
		});
		
		for (Direction dir : sortedDirections.stream().map(e -> e.getKey())
				.collect(Collectors.toList()))
		{
			ArrayList<Point> possibleStarts = new ArrayList<Point>();
			
			switch (dir)
			{
				case north:
					for (int i = 0; i < obj.getWidth(); ++i)
						possibleStarts.add(Point.Add(obj.getTopLeft(),
								Point.Multiply(Direction.east, i)));
					break;
				case east:
					for (int i = 0; i < obj.getWidth(); ++i)
						possibleStarts.add(Point.Add(obj.getBottomRight(),
								Point.Multiply(Direction.north, i)));
					break;
				case south:
					for (int i = 0; i < obj.getWidth(); ++i)
						possibleStarts.add(Point.Add(obj.getBottomRight(),
								Point.Multiply(Direction.west, i)));
					break;
				case west:
					for (int i = 0; i < obj.getWidth(); ++i)
						possibleStarts.add(Point.Add(obj.getTopLeft(),
								Point.Multiply(Direction.south, i)));
					break;
			}
			possibleStarts.removeIf(p -> myReflexives.stream().anyMatch(
					a -> a.getRoute().contains(p)));
			
			for (int i = 0; i < possibleStarts.size(); ++i)
			{
				Point p = possibleStarts.get(i);
				// assamble end points, compute end point and end direction
				Pair<Point, Direction> ends = getEndPointDirectionForReflexive(p, obj,
						dir, myReflexives);
				if (ends == null)
					continue;
				
				try
				{
					return tryDrawReflexive(p, ends.First, dir, ends.Second,
							diagramObjects);
				}
				catch (CannotFindAssociationRouteException e)
				{
					
				}
			}
		}
		
		throw new CannotFindAssociationRouteException(
				"Cannot find route for reflexive link: " + refl.toString() + "!");
	}
	
	private Pair<Point, Direction> getEndPointDirectionForReflexive(Point p,
			RectangleObject obj, Direction startDir, Set<LineAssociation> myReflexives)
	{
		for (Direction dir : Direction.values())
		{
			if (dir.equals(startDir))
				continue;
			
			if (Point.isInTheDirection(obj.getPosition(), p, dir))
			{
				// dir is the end side
				ArrayList<Point> possibleEnds = new ArrayList<Point>();
				switch (dir)
				{
					case north:
						for (int i = 0; i < obj.getWidth(); ++i)
							possibleEnds.add(Point.Add(obj.getTopLeft(),
									Point.Multiply(Direction.east, i)));
						break;
					case east:
						for (int i = 0; i < obj.getWidth(); ++i)
							possibleEnds.add(Point.Add(obj.getBottomRight(),
									Point.Multiply(Direction.north, i)));
						break;
					case south:
						for (int i = 0; i < obj.getWidth(); ++i)
							possibleEnds.add(Point.Add(obj.getBottomRight(),
									Point.Multiply(Direction.west, i)));
						break;
					case west:
						for (int i = 0; i < obj.getWidth(); ++i)
							possibleEnds.add(Point.Add(obj.getTopLeft(),
									Point.Multiply(Direction.south, i)));
						break;
				}
				possibleEnds.removeIf(p_lambda -> !Point.isInTheDirection(
						obj.getPosition(), p_lambda, startDir));
				possibleEnds.removeIf(p_lambda -> myReflexives.stream().anyMatch(
						a_lambda -> a_lambda.getRoute().contains(p_lambda)));
				
				for (Point poi : possibleEnds)
				{
					if (Point.Substract(p, obj.getPosition()).length() == Point
							.Substract(poi, obj.getPosition()).length())
					{
						return new Pair<Point, Direction>(poi, dir);
					}
				}
			}
		}
		
		return null;
	}
	
	private ArrayList<Point> tryDrawReflexive(Point from, Point to, Direction startDir,
			Direction endDir, Set<RectangleObject> objects)
			throws CannotFindAssociationRouteException
	{
		ArrayList<Point> route1 = new ArrayList<Point>();
		ArrayList<Point> route2 = new ArrayList<Point>();
		route1.add(from);
		route2.add(to);
		Integer outDistance = 0;
		Boolean movingForward1 = true;
		Boolean movingForward2 = true;
		
		while (movingForward1 && movingForward2)
		{
			Point tempTurn = Point.Add(from, endDir);
			if (!_reflexives.stream().anyMatch(a -> a.getRoute().contains(tempTurn))
					&& !objects.stream().anyMatch(o -> o.getPoints().contains(tempTurn)))
			{
				movingForward1 = false;
				from = tempTurn;
				route1.add(from);
			}
			else
			{
				Point tempForward = Point.Add(from, startDir);
				if (!_reflexives.stream().anyMatch(
						a -> a.getRoute().contains(tempForward))
						&& !objects.stream().anyMatch(
								o -> o.getPoints().contains(tempForward)))
				{
					movingForward1 = true;
					from = tempForward;
					route1.add(from);
					
					++outDistance;
				}
				else
					throw new CannotFindAssociationRouteException(
							"Cannot draw reflexive link!");
			}
			
			Point tempTurn2 = Point.Add(to, startDir);
			if (!_reflexives.stream().anyMatch(a -> a.getRoute().contains(tempTurn2))
					&& !objects.stream().anyMatch(o -> o.getPoints().contains(tempTurn2)))
			{
				movingForward2 = false;
				to = tempTurn2;
				route2.add(to);
			}
			else
			{
				Point tempForward2 = Point.Add(to, endDir);
				if (!_reflexives.stream().anyMatch(
						a -> a.getRoute().contains(tempForward2))
						&& !objects.stream().anyMatch(
								o -> o.getPoints().contains(tempForward2)))
				{
					movingForward2 = true;
					to = tempForward2;
					route2.add(to);
				}
				else
					throw new CannotFindAssociationRouteException(
							"Cannot draw reflexive link!");
			}
		}
		
		if (!movingForward1.equals(movingForward2))
			throw new CannotFindAssociationRouteException("Cannot draw reflexive link!");
		
		for (int i = 0; i < (outDistance * 2) - 1; ++i)
		{
			Point tempForward = Point.Add(from, endDir);
			if (!_reflexives.stream().anyMatch(a -> a.getRoute().contains(tempForward))
					&& !objects.stream().anyMatch(
							o -> o.getPoints().contains(tempForward)))
			{
				from = tempForward;
				route1.add(from);
			}
			else
				throw new CannotFindAssociationRouteException(
						"Cannot draw reflexive link!");
			
			Point tempForward2 = Point.Add(to, startDir);
			if (!_reflexives.stream().anyMatch(a -> a.getRoute().contains(tempForward2))
					&& !objects.stream().anyMatch(
							o -> o.getPoints().contains(tempForward2)))
			{
				to = tempForward2;
				route2.add(to);
			}
			else
				throw new CannotFindAssociationRouteException(
						"Cannot draw reflexive link!");
		}
		
		// Unite the routes
		for (int i = (route2.size() - 2); i >= 0; --i)
		{
			route1.add(route2.get(i));
		}
		
		return route1;
	}
	
	private void arrangeLinks(Set<RectangleObject> diagramObjects)
			throws CannotStartAssociationRouteException,
			CannotFindAssociationRouteException, InternalException
	{
		Set<Painted<Point>> occupiedLinks = new HashSet<Painted<Point>>();
		
		for (int i = 0; i < _assocs.size(); ++i)
		{
			LineAssociation a = _assocs.get(i);
			
			System.err.print((i + 1) + "/" + _assocs.size() + ": " + a.getId() + " ... ");
			
			Point START = a.getRoute(LineAssociation.RouteConfig.START);
			Point END = a.getRoute(LineAssociation.RouteConfig.END);
			Integer WIDTH = diagramObjects.stream().findFirst().get().getWidth();
			
			Set<Point> STARTSET = setStartSet(new Pair<String, RouteConfig>(a.getId(),
					RouteConfig.START), START, WIDTH,
					occupiedLinks.stream().map(p -> p.Inner).collect(Collectors.toSet()));
			Set<Point> ENDSET = setEndSet(new Pair<String, RouteConfig>(a.getId(),
					RouteConfig.END), END, WIDTH, occupiedLinks.stream()
					.map(p -> p.Inner).collect(Collectors.toSet()));
			
			// Assemble occupied points
			Set<Painted<Point>> OBJS = new HashSet<Painted<Point>>();
			OBJS.addAll(occupiedLinks);
			// Add objects transformed place to occupied list
			Set<Painted<Point>> occupied = new HashSet<Painted<Point>>();
			for (RectangleObject obj : diagramObjects)
			{
				if (START.equals(END))
				{
					// add all except start and end set
					for (Point p : obj.getPoints())
					{
						if (ENDSET.contains(p))
							continue;
						if (STARTSET.contains(p))
							continue;
						
						occupied.add(new Painted<Point>(Colors.Red, p));
					}
				}
				else
				{
					if (END.equals(obj.getPosition()))
					{
						// add all except endset
						for (Point p : obj.getPoints())
						{
							if (!ENDSET.contains(p))
								occupied.add(new Painted<Point>(Colors.Red, p));
						}
						continue;
					}
					else if (START.equals(obj.getPosition()))
					{
						// add all except startset
						for (Point p : obj.getPoints())
						{
							if (!STARTSET.contains(p))
								occupied.add(new Painted<Point>(Colors.Red, p));
						}
						continue;
					}
					else
					{
						for (Point p : obj.getPoints())
							occupied.add(new Painted<Point>(Colors.Red, p));
					}
				}
			}
			OBJS.addAll(occupied);
			for (Point p : STARTSET)
				OBJS.add(new Painted<Point>(Colors.Blue, p));
			
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
			
			GraphSearch gs = new GraphSearch(START, STARTSET, END, ENDSET, OBJS, top,
					_widthOfObjects);
			a.setRoute(gs.value());
			a.setTurns(gs.turns());
			a.setExtends(gs.extendsNum());
			
			System.err.println("DONE!");
			
			_assocs.set(i, a);
			
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
							.getRoute().get(ri))));
				}
				else
				{
					occupiedLinks.add(new Painted<Point>(Colors.Yellow, new Point(a
							.getRoute().get(ri))));
				}
			}
		}
	}
	
	private Integer transformReflexiveCount(Integer count)
	{
		Integer sideSize = 3;
		for (int i = 2; i < count; i = i + 4)
		{
			sideSize = sideSize + 2;
		}
		
		return sideSize;
	}
	
	private Point transformDimension(Point p)
	{
		return Point.Multiply(p, _transformAmount);
	}
	
	public Set<LineAssociation> value()
	{
		Set<LineAssociation> result = _assocs.stream().collect(Collectors.toSet());
		result.addAll(_reflexives.stream().collect(Collectors.toSet()));
		
		return result;
	}
}
