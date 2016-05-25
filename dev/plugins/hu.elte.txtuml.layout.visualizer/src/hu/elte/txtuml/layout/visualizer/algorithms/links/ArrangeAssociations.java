package hu.elte.txtuml.layout.visualizer.algorithms.links;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Color;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Node;
import hu.elte.txtuml.layout.visualizer.algorithms.links.utils.DefaultAssocStatements;
import hu.elte.txtuml.layout.visualizer.algorithms.links.utils.LinkArrangeDiagram;
import hu.elte.txtuml.layout.visualizer.algorithms.links.utils.LinkComparator;
import hu.elte.txtuml.layout.visualizer.algorithms.utils.Helper;
import hu.elte.txtuml.layout.visualizer.model.utils.RectangleObjectTreeEnumerator;
import hu.elte.txtuml.layout.visualizer.events.ProgressManager;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.model.Boundary;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Options;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation.RouteConfig;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;

/**
 * This class arranges the lines of links.
 */
public class ArrangeAssociations {
	private final Integer MAXIMUM_TRY_COUNT = 100;

	private LinkArrangeDiagram _diagram;
	private List<Statement> _statements;
	private HashMap<Pair<String, RouteConfig>, HashSet<Point>> _possibleStarts;

	private Integer _gId;
	private Options _options;

	/**
	 * Returns the last use Group Id number.
	 * 
	 * @return the last use Group Id number.
	 */
	public Integer getGId() {
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
	 * @param opt
	 *            Options of the algorithm.
	 * @throws ConversionException
	 *             Throws if algorithm cannot convert certain StatementType into
	 *             Direction.
	 * @throws InternalException
	 *             Throws if any error occurs which should not happen. Contact
	 *             developer!
	 * @throws CannotFindAssociationRouteException
	 *             Throws if a certain link's path cannot be found.
	 * @throws UnknownStatementException
	 *             Throws if some unknown statements are found during
	 *             processing.
	 */
	public ArrangeAssociations(Diagram diag, List<Statement> stats, Integer gid, Options opt)
			throws ConversionException, InternalException, CannotFindAssociationRouteException,
			UnknownStatementException {
		// Setup variables and default values
		_gId = gid;
		_options = opt;

		_diagram = new LinkArrangeDiagram(diag);
		_statements = new ArrayList<Statement>(stats);

		// Emit Event
		ProgressManager.getEmitter().OnLinkArrangeStart();

		// Arrange the links
		arrange();

		// Emit Event
		ProgressManager.getEmitter().OnLinkArrangeEnd();
	}

	private void arrange() throws ConversionException, InternalException {
		_possibleStarts = new HashMap<Pair<String, RouteConfig>, HashSet<Point>>();

		// Inflate diagram to start with a object width enough for the
		// maximum number of links
		_diagram.initialExpand(_options.CorridorRatio);

		// Get default statements on links
		DefaultAssocStatements das = new DefaultAssocStatements(_gId, _statements, _diagram.Assocs);
		_statements = das.value();
		_gId = das.getGroupId();

		Boolean needRepeat = true;

		// Arrange until (needs a repeat or reached maximum try count)
		Integer tryCount = 0;
		while (needRepeat && tryCount <= MAXIMUM_TRY_COUNT) {
			++tryCount;

			// Process statements, priority and direction
			processStatements();
			needRepeat = false;

			try {
				// Maximum distance between objects
				Boundary bounds = new Boundary(_diagram.getArea());
				bounds.addError(20, _diagram.WidthOfCells, _diagram.HeightOfCells);

				// Add objects transformed place to occupied list
				Map<Point, Color> occupied = new HashMap<Point, Color>();
				for (RectangleObject box : _diagram.Objects) {
					occupied.putAll(getBoxPaintedPoints(box));
				}

				// Search for the route of every Link
				arrangeLinks(occupied, bounds);
			} catch (CannotStartAssociationRouteException | CannotFindAssociationRouteException e) {
				if (_options.Logging)
					Logger.sys.info("(Normal) Expanding grid!");

				needRepeat = true;
				// Grid * 2
				_diagram.expand();
				continue;
			}
		}
	}

	private void processStatements() throws ConversionException, InternalException {
		if (_statements != null && _statements.size() != 0) {
			// Set priority
			setPriorityMap();

			ArrayList<Statement> priorityless = new ArrayList<Statement>(_statements);
			priorityless.removeIf(s -> s.getType().equals(StatementType.priority));

			// Set starts/ends for statemented assocs
			setPossibles(priorityless);
		}
	}

	private Set<Pair<Node, Double>> getSet(Pair<String, RouteConfig> key, Point start, Integer p_width,
			Integer p_height, Set<Point> occupied, Boolean isReflexive, Boolean isStart) throws InternalException {
		Set<Pair<Point, Double>> result = new HashSet<Pair<Point, Double>>();
		Double defaultWeight = -1.0;

		// Statement given points
		if (_possibleStarts.containsKey(key))
			result.addAll(_possibleStarts.get(key).stream().map(poi -> new Pair<Point, Double>(poi, defaultWeight))
					.collect(Collectors.toSet()));

		// Add Object's points
		RectangleObject tempObj = new RectangleObject("TEMP", start);
		tempObj.setWidth(p_width);
		tempObj.setHeight(p_height);

		if (result.size() == 0) {
			result.addAll(tempObj.getPerimiterPoints().stream().map(poi -> new Pair<Point, Double>(poi, defaultWeight))
					.collect(Collectors.toSet()));
			if (isReflexive) {
				if (isStart)
					result = setReflexiveSet(result, tempObj, true);
				else
					result = setReflexiveSet(result, tempObj, false);
			}
		}

		// Remove occupied points
		result.removeIf(p -> occupied.contains(p.getFirst()));
		// Remove corner points
		result.removeIf(p -> tempObj.isCornerPoint(p.getFirst()));

		// Set the weights of nodes.
		for (Pair<Point, Double> pair : result) {
			if (pair.getSecond() < 0.0) {
				Double distance1 = Point.Substract(pair.getFirst(), tempObj.getTopLeft()).length();
				Double distance2 = Point.Substract(pair.getFirst(), tempObj.getBottomRight()).length();

				Double w = p_width / 2.0;
				Double h = p_height / 2.0;

				if (Helper.isAlmostEqual(distance1, w, 0.8) || Helper.isAlmostEqual(distance1, h, 0.8)
						|| Helper.isAlmostEqual(distance2, w, 0.8) || Helper.isAlmostEqual(distance2, h, 0.8)) {
					pair = new Pair<Point, Double>(pair.getFirst(), 0.0);
				} else {
					pair = new Pair<Point, Double>(pair.getFirst(), 1.0);
				}
			}
		}

		if (isStart)
			return convertToNodes(result, tempObj);
		else
			return convertToInvertedNodes(result, tempObj);
	}

	private Set<Pair<Point, Double>> setReflexiveSet(Set<Pair<Point, Double>> fromSet, RectangleObject obj,
			Boolean isStart) {
		Set<Pair<Point, Double>> result = new HashSet<Pair<Point, Double>>(fromSet);

		Integer halfwayH = ((obj.getWidth() % 2) == 0) ? ((obj.getWidth() / 2) - 1) : ((obj.getWidth() - 1) / 2);
		Integer halfwayV = ((obj.getHeight() % 2) == 0) ? ((obj.getHeight() / 2) - 1) : ((obj.getHeight() - 1) / 2);

		Point northern = Point.Add(obj.getPosition(), new Point(halfwayH, 0));
		if (isStart)
			result.removeIf(p -> p.getFirst().getY().equals(northern.getY()) && p.getFirst().getX() >= northern.getX());
		else
			result.removeIf(p -> p.getFirst().getY().equals(northern.getY()) && p.getFirst().getX() <= northern.getX());

		Point eastern = Point.Add(obj.getBottomRight(), new Point(0, halfwayV));
		if (isStart)
			result.removeIf(p -> p.getFirst().getX().equals(eastern.getX()) && p.getFirst().getY() <= eastern.getY());
		else
			result.removeIf(p -> p.getFirst().getX().equals(eastern.getX()) && p.getFirst().getY() >= eastern.getY());

		Point southern = Point.Add(obj.getBottomRight(), new Point(-1 * halfwayH, 0));
		if (isStart)
			result.removeIf(p -> p.getFirst().getY().equals(southern.getY()) && p.getFirst().getX() <= southern.getX());
		else
			result.removeIf(p -> p.getFirst().getY().equals(southern.getY()) && p.getFirst().getX() >= southern.getX());

		Point western = Point.Add(obj.getPosition(), new Point(0, -1 * halfwayV));
		if (isStart)
			result.removeIf(p -> p.getFirst().getX().equals(western.getX()) && p.getFirst().getY() >= western.getY());
		else
			result.removeIf(p -> p.getFirst().getX().equals(western.getX()) && p.getFirst().getY() <= western.getY());

		// Set the weights of points
		for (Pair<Point, Double> pair : result) {
			pair = new Pair<Point, Double>(pair.getFirst(), 0.0);
		}

		return result;
	}

	private Set<Pair<Node, Double>> convertToNodes(Set<Pair<Point, Double>> ps, RectangleObject obj)
			throws InternalException {
		Set<Pair<Node, Double>> result = new HashSet<Pair<Node, Double>>();

		for (Pair<Point, Double> pair : ps) {
			if (obj.getTopLeft().getX().equals(pair.getFirst().getX()))
				result.add(new Pair<Node, Double>(new Node(pair.getFirst(), Point.Add(pair.getFirst(), Direction.west)),
						pair.getSecond()));
			else if (obj.getTopLeft().getY().equals(pair.getFirst().getY()))
				result.add(new Pair<Node, Double>(
						new Node(pair.getFirst(), Point.Add(pair.getFirst(), Direction.north)), pair.getSecond()));
			else if (obj.getBottomRight().getX().equals(pair.getFirst().getX()))
				result.add(new Pair<Node, Double>(new Node(pair.getFirst(), Point.Add(pair.getFirst(), Direction.east)),
						pair.getSecond()));
			else if (obj.getBottomRight().getY().equals(pair.getFirst().getY()))
				result.add(new Pair<Node, Double>(
						new Node(pair.getFirst(), Point.Add(pair.getFirst(), Direction.south)), pair.getSecond()));
			else
				throw new InternalException("BOOM1!");
		}

		return result;
	}

	private Set<Pair<Node, Double>> convertToInvertedNodes(Set<Pair<Point, Double>> ps, RectangleObject obj)
			throws InternalException {
		Set<Pair<Node, Double>> result = new HashSet<Pair<Node, Double>>();

		for (Pair<Point, Double> pair : ps) {
			if (obj.getTopLeft().getX().equals(pair.getFirst().getX()))
				result.add(new Pair<Node, Double>(new Node(Point.Add(pair.getFirst(), Direction.west), pair.getFirst()),
						pair.getSecond()));
			else if (obj.getTopLeft().getY().equals(pair.getFirst().getY()))
				result.add(new Pair<Node, Double>(
						new Node(Point.Add(pair.getFirst(), Direction.north), pair.getFirst()), pair.getSecond()));
			else if (obj.getBottomRight().getX().equals(pair.getFirst().getX()))
				result.add(new Pair<Node, Double>(new Node(Point.Add(pair.getFirst(), Direction.east), pair.getFirst()),
						pair.getSecond()));
			else if (obj.getBottomRight().getY().equals(pair.getFirst().getY()))
				result.add(new Pair<Node, Double>(
						new Node(Point.Add(pair.getFirst(), Direction.south), pair.getFirst()), pair.getSecond()));
			else
				throw new InternalException("BOOM2!");
		}

		return result;
	}

	private void setPriorityMap() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		for (Statement s : _statements) {
			if (s.getType().equals(StatementType.priority)) {
				result.put(s.getParameter(0), Integer.parseInt(s.getParameter(1)));
			}
		}

		// Set the priority for all links
		for (LineAssociation link : _diagram.Assocs) {
			if (result.containsKey(link.getId())) {
				link.setPriority(result.get(link.getId()));
			} else {
				link.setPriority(Integer.MIN_VALUE);
			}
		}
	}

	private void setPossibles(List<Statement> prioritylessStats) throws ConversionException, InternalException {
		for (Statement s : prioritylessStats) {
			try {
				LineAssociation link = _diagram.Assocs.stream().filter(a -> a.getId().equals(s.getParameter(0)))
						.findFirst().get();
				RectangleObject obj = _diagram.Objects.stream().filter(o -> o.getName().equals(s.getParameter(1)))
						.findFirst().get();
				if (link.getFrom().equals(obj.getName())
						&& (s.getParameters().size() == 2 || s.getParameter(2).toLowerCase().equals("start"))) {
					// RouteConfig.START
					Point startPoint = getStartingPoint(s.getType().asDirection(), obj);
					Direction moveDir = s.getType().asDirection();
					generatePossiblePoints(link, obj, startPoint, moveDir, RouteConfig.START);
				}
				if (link.getTo().equals(obj.getName())
						&& (s.getParameters().size() == 2 || s.getParameter(2).toLowerCase().equals("end"))) {
					// RouteConfig.END
					Point startPoint = getStartingPoint(s.getType().asDirection(), obj);
					Direction moveDir = s.getType().asDirection();
					generatePossiblePoints(link, obj, startPoint, moveDir, RouteConfig.END);
				}
			} catch (NoSuchElementException e) {
				throw new InternalException("Inner Exception: [" + e.getMessage() + "], "
						+ "Probably a statment shouldn't have reached this code!");
			}
		}
	}

	private Point getStartingPoint(Direction dir, RectangleObject o) throws InternalException {
		if (dir.equals(Direction.north) || dir.equals(Direction.west))
			return o.getPosition();
		else if (dir.equals(Direction.south) || dir.equals(Direction.east))
			return o.getBottomRight();
		else
			throw new InternalException("Unknown Direction!");
	}

	private void generatePossiblePoints(LineAssociation toModify, RectangleObject connectsTo, Point first,
			Direction toMove, RouteConfig r) {
		HashSet<Point> points = new HashSet<Point>();

		Integer endOfSide = (toMove.equals(Direction.north) || toMove.equals(Direction.south)) ? connectsTo.getHeight()
				: connectsTo.getWidth();

		for (int i = 0; i < endOfSide; ++i) {
			points.add(Point.Add(first, Point.Multiply(toMove, i)));
		}
		Pair<String, RouteConfig> key = new Pair<String, LineAssociation.RouteConfig>(toModify.getId(), r);

		_possibleStarts.put(key, points);
	}

	private void arrangeLinks(Map<Point, Color> occupied, Boundary bounds) throws CannotStartAssociationRouteException,
			CannotFindAssociationRouteException, InternalException, ConversionException {
		Map<Point, Color> occupiedLinks = new HashMap<Point, Color>();

		Integer countOfLinksDone = 0;
		for (LineAssociation link : _diagram.Assocs.stream().sorted(new LinkComparator())
				.collect(Collectors.toList())) {
			++countOfLinksDone;
			if (_options.Logging)
				Logger.sys.info(countOfLinksDone + "/" + _diagram.Assocs.size() + ": " + link.getId() + " ... ");

			if (link.isPlaced()) {
				if (_options.Logging)
					Logger.sys.info("NOTHING TO DO!");

				Map<Point, Color> routePoints = getRoutePaintedPoints(link);
				occupiedLinks.putAll(routePoints);
				occupied.putAll(routePoints);
				continue;
			}

			doGraphSearch(link, occupiedLinks, occupied, bounds);

			if (_options.Logging)
				Logger.sys.info("DONE!");

			if (link.getRoute().size() < 3)
				throw new InternalException("Route is shorter then 3!");

			// Update occupied places with the route of this link
			Map<Point, Color> routePoints = getRoutePaintedPoints(link);
			occupiedLinks.putAll(routePoints);
			occupied.putAll(routePoints);

			if (countOfLinksDone == (int) (_diagram.Assocs.size() * 25.0 / 100.0)) {
				ProgressManager.getEmitter().OnLinkArrangeFirstQuarter();
			} else if (countOfLinksDone == (int) (_diagram.Assocs.size() * 50.0 / 100.0)) {
				ProgressManager.getEmitter().OnLinkArrangeHalf();
			} else if (countOfLinksDone == (int) (_diagram.Assocs.size() * 75.0 / 100.0)) {
				ProgressManager.getEmitter().OnLinkArrangeThirdQuarter();
			}
		}
	}

	private void doGraphSearch(LineAssociation link, Map<Point, Color> occupiedLinks, Map<Point, Color> occupied,
			Boundary bounds) throws InternalException, CannotStartAssociationRouteException,
					CannotFindAssociationRouteException, ConversionException {
		RectangleObject STARTBOX = getBox(link.getFrom());
		RectangleObject ENDBOX = getBox(link.getTo());

		Set<Pair<Node, Double>> STARTSET = getSet(new Pair<String, RouteConfig>(link.getId(), RouteConfig.START),
				STARTBOX.getPosition(), STARTBOX.getWidth(), STARTBOX.getHeight(), occupiedLinks.keySet(),
				link.isReflexive(), true);

		Set<Pair<Node, Double>> ENDSET = getSet(new Pair<String, RouteConfig>(link.getId(), RouteConfig.END),
				ENDBOX.getPosition(), ENDBOX.getWidth(), ENDBOX.getHeight(), occupiedLinks.keySet(), link.isReflexive(),
				false);

		// Search for the route
		if (STARTSET.size() == 0 || ENDSET.size() == 0) {
			// Cannot start or end
			throw new CannotStartAssociationRouteException("Cannot get out of start, or cannot enter end!");
		}

		GraphSearch gs = new GraphSearch(STARTSET, ENDSET, occupied, bounds);

		link.setRoute(convertFromNodes(gs.value(), STARTBOX.getPosition(), ENDBOX.getPosition()));
		link.setExtends(gs.extendsNum());
	}

	private RectangleObject getBox(String name) {
		RectangleObject result = _diagram.Objects.stream().filter(o -> o.getName().equals(name)).findFirst()
				.orElse(null);

		if (result == null) {
			// Link goes into sub package

			// Not handled currently
			for (RectangleObject box : new RectangleObjectTreeEnumerator(_diagram.Objects)) {
				if (box.getName().equals(name)) {
					return box;
				}
			}
		}

		return result;
	}

	private Map<Point, Color> getBoxPaintedPoints(RectangleObject box)
	{
		Map<Point, Color> result = new HashMap<Point, Color>();
		
		if (box.hasInner()) {
			// Add compositeBox's outer rim as a warning line
			for (Point p : box.getPerimiterPoints()) {
				result.put(p, Color.Yellow);
			}
			
			// Add compositeBox's inner boxes
			for(RectangleObject innerBox : box.getInner().Objects)
			{
				result.putAll(getBoxPaintedPoints(innerBox));
			}
			
			// Add compositeBox's inner links
			for(LineAssociation innerLink : box.getInner().Assocs)
			{
				result.putAll(getRoutePaintedPoints(innerLink));
			}
			
		} else {
			for (Point p : box.getPoints()) {
				result.put(p, Color.Red);
			}
		}
		
		return result;
	}

	private Map<Point, Color> getRoutePaintedPoints(LineAssociation link) {
		Map<Point, Color> result = new HashMap<Point, Color>();

		for (int ri = 1; ri < link.getRoute().size() - 1; ++ri) {
			if (!Point.Substract(link.getRoute().get(ri - 1), link.getRoute().get(ri))
					.equals(Point.Substract(link.getRoute().get(ri), link.getRoute().get(ri + 1)))) {
				result.put(new Point(link.getRoute().get(ri)), Color.Red);
			} else {
				result.put(new Point(link.getRoute().get(ri)), Color.Yellow);
			}
		}

		return result;
	}

	private ArrayList<Point> convertFromNodes(ArrayList<Node> nodes, Point start, Point end) {
		ArrayList<Point> result = new ArrayList<Point>();

		result.add(start);
		result.add(new Point(nodes.get(0).getFrom()));

		for (int i = 0; i < nodes.size(); ++i) {
			result.add(new Point(nodes.get(i).getTo()));
		}

		result.add(end);

		return result;
	}

	/**
	 * Returns the final arranged {@link Diagram}.
	 * 
	 * @return the final arranged {@link Diagram}.
	 */
	public Diagram getDiagram() {
		return _diagram.getDiagram();
	}

}
