package hu.elte.txtuml.layout.visualizer.algorithms.links;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Color;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Cost;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Graph;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Link;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Node;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils.Parent;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.Boundary;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.utils.Pair;

/**
 * This class implements an A^* graph search algorithm.
 */
class GraphSearch {

	// Constants

	private Double WEIGHT_LENGTH = 0.0; // 2.9
	private Double WEIGHT_REFLEXIVE_LENGTH = 0.0;
	private final Double WEIGHT_TURNS = 3.2; // 2.7
	private final Double WEIGHT_CROSSING = 2.0; // 2.0

	// end Constants

	// Variables

	private Node _end;
	private Set<Node> _endSet;
	private Map<Point, Color> _colors;
	private Boundary _boundary;
	private Integer _extends;

	private Graph<Node> G;
	private PriorityQueue<Node> AvailableNodes;
	private Cost<Node> g;
	private Parent<Node> PI;

	private HashMap<Node, Double> _heuristic;

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
	 * @param cols
	 *            Nodes we have to skip (already occupied).
	 * @param bounds
	 *            Bounds of the maximum width of the graph to search in.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if there is no route from start->end.
	 * @throws CannotStartAssociationRouteException
	 *             Throws if the algorithm cannot even start the route from
	 *             start.
	 * @throws ConversionException
	 *             Throws if the algorithm cannot convert a {@link Point} to a
	 *             {@link Direction} or vice versa.
	 * @throws InternalException
	 *             Throws if the algorithm encounters something which it should
	 *             not have.
	 */
	public GraphSearch(Set<Pair<Node, Double>> ss, Set<Pair<Node, Double>> es, Map<Point, Color> cols, Boundary bounds)
			throws CannotFindAssociationRouteException, CannotStartAssociationRouteException, ConversionException,
			InternalException {
		this(ss, es, cols, bounds, false);
	}
	
	/**
	 * Creates a graph to find a route in it from start to end. If the link is not recursive, the route length is
	 * minimal, otherwise the difference between the route length and the maximum distance between two endpoints is minimal. Route cornering is minimal.
	 * 
	 * @param ss
	 *            Set of start Nodes.
	 * @param es
	 *            Set of end Nodes to reach.
	 * @param cols
	 *            Nodes we have to skip (already occupied).
	 * @param bounds
	 *            Bounds of the maximum width of the graph to search in.
	 * @param isReflexive
	 * 			  Indicates whether the link represented by this route is reflexive.
	 * @throws CannotFindAssociationRouteException
	 *             Throws if there is no route from start->end.
	 * @throws CannotStartAssociationRouteException
	 *             Throws if the algorithm cannot even start the route from
	 *             start.
	 * @throws ConversionException
	 *             Throws if the algorithm cannot convert a {@link Point} to a
	 *             {@link Direction} or vice versa.
	 * @throws InternalException
	 *             Throws if the algorithm encounters something which it should
	 *             not have.
	 */
	public GraphSearch(Set<Pair<Node, Double>> ss, Set<Pair<Node, Double>> es, Map<Point, Color> cols, Boundary bounds, boolean isReflexive)
			throws CannotFindAssociationRouteException, CannotStartAssociationRouteException, ConversionException,
			InternalException {
		
		if(isReflexive){
			WEIGHT_LENGTH = 0.0;
			WEIGHT_REFLEXIVE_LENGTH = 1.0;
		}else{
			WEIGHT_LENGTH = 1.0;
			WEIGHT_REFLEXIVE_LENGTH = 0.0;
		}
		// Initialize
		G = new Graph<Node>(); // Graph
		AvailableNodes = new PriorityQueue<Node>((x, y) -> nodeComparator(x, y)); // 'Open'
																					// nodes
		g = new Cost<Node>(); // Cost function
		PI = new Parent<Node>(); // Parent relation
		_end = new Node(new Point(), new Point());
		_endSet = new HashSet<Node>();
		_colors = new HashMap<>(cols);

		_boundary = bounds;
		_extends = 0;
		_heuristic = new HashMap<Node, Double>();

		// Set up End Set
		for (Pair<Node, Double> pair : es) {
			_endSet.add(pair.getFirst());
			g.set(pair.getFirst(), pair.getSecond());
		}

		// Extend StartSet
		for (Pair<Node, Double> pair : ss) {
			PI.set(pair.getFirst(), null);
			g.set(pair.getFirst(), 2 * WEIGHT_LENGTH + pair.getSecond());
			AvailableNodes.add(pair.getFirst());
		}

		if (!search()) {
			throw new CannotFindAssociationRouteException("No Route from START to END!");
		}
	}

	// end Ctors

	// Methods

	private boolean search() {
		while (true) {
			if (AvailableNodes.isEmpty())
				return false;

			Node n = AvailableNodes.poll();

			if (_endSet.contains(n)) {
				// Node e = _endSet.stream()
				// .filter(node -> node.getFrom().equals(n.getTo()))
				// .findFirst().get();
				// PI.set(e, n);
				PI.set(_end, n);
				return true;
			}

			++_extends;
			Set<Pair<Node, Double>> childs = Gamma(n);
			for (Pair<Node, Double> pair : childs) {
				Node m = pair.getFirst();
				Double newCost = (g.get(n) + pair.getSecond());

				if (!G.contains(m) || newCost < g.get(m)) {
					PI.set(m, n);
					g.set(m, newCost);
					AvailableNodes.add(m);
				}
				G.addNode(m);
				G.addLink(new Link<Node>(n, m));
			}
		}
	}

	private Integer nodeComparator(Node a, Node b) {
		return Double.compare(f(a), f(b));
	}

	@SuppressWarnings("unused")
	private Node minf() {
		Node min = null;
		Double minval = Double.MAX_VALUE;

		for (Node p : AvailableNodes) {
			Double f = f(p);
			if (f < minval) {
				minval = f;
				min = p;
			}
		}

		return min;
	}

	// Evaluation function
	private Double f(Node p) {
		Double cost = g.get(p);
		Double heuristic = h(p);

		return cost + heuristic;
	}

	private Double h(Node p) {
		if (_heuristic.containsKey(p))
			return _heuristic.get(p);

		Pair<Double, Node> distance = manhattanDistance(p);
		Double remainingTurns = manhattanLeastTurns(p);
		Double reflexiveLength = reflexiveLinkMinLength(p);
		// manhattanLeastTurnsCheckingOccupied(p, distance.getSecond());
		Double result = (WEIGHT_TURNS * remainingTurns + WEIGHT_LENGTH * distance.getFirst() + WEIGHT_REFLEXIVE_LENGTH * reflexiveLength);

		_heuristic.put(p, result);

		return result;
	}

	// Metrics
	
	private Double reflexiveLinkMinLength(Node a){
		int maxDistanceBetweenPossibleEndpoints = _endSet.stream().map(p1 -> {
			return _endSet.stream().map(p2 -> {
				return Math.max(Math.abs(p1.getTo().getX() - p2.getTo().getX()), Math.abs(p1.getTo().getY() - p2.getTo().getY()));
			}).max((d1, d2) -> Integer.compare(d1, d2)).get();
		}).max((d1, d2) -> Integer.compare(d1, d2)).get();
		
		return Math.abs(maxDistanceBetweenPossibleEndpoints - linkLengthUpTo(a));
	}
	
	private Double linkLengthUpTo(Node a){
		if(a == null)
			return 0.0;
		
		double length = Math.pow(a.getFrom().getX() - a.getTo().getX(), 2.0) + Math.pow(a.getFrom().getY() - a.getTo().getY(), 2.0);
		a = PI.get(a);
		while(a != null){
			length += Math.pow(a.getFrom().getX() - a.getTo().getX(), 2.0) + Math.pow(a.getFrom().getY() - a.getTo().getY(), 2.0);
			a = PI.get(a);
		}
		return length;
	}
	
	private Pair<Double, Node> manhattanDistance(Node a) {
		Pair<Integer, Node> result = _endSet.stream().map(p -> {
			Integer dx = Math.abs(a.getTo().getX() - p.getTo().getX());
			Integer dy = Math.abs(a.getTo().getY() - p.getTo().getY());
			Integer tempResult = dx + dy;

			return new Pair<Integer, Node>(tempResult, p);
		}).min((p1, p2) -> {
			return Integer.compare(p1.getFirst(), p2.getFirst());
		}).get();

		return new Pair<Double, Node>((double) result.getFirst(), result.getSecond());
	}

	private Double manhattanLeastTurns(Node a) {
		Node closest = _endSet.stream().map(p -> {
			Integer dx = Math.abs(a.getTo().getX() - p.getTo().getX());
			Integer dy = Math.abs(a.getTo().getY() - p.getTo().getY());
			Integer tempResult = dx + dy;

			return new Pair<Node, Integer>(p, tempResult);
		}).min((d1, d2) -> {
			return Integer.compare(d1.getSecond(), d2.getSecond());
		}).get().getFirst();

		if (!closest.getTo().getX().equals(a.getTo().getX()) && !closest.getTo().getY().equals(a.getTo().getY())) {
			return 1.0;
		}

		return 0.0;
	}

	@SuppressWarnings("unused")
	private Double manhattanLeastTurnsCheckingOccupied(Node from, Node to) {
		/*
		 * Integer min = Integer.MAX_VALUE;
		 * 
		 * Point ending = to.getTo(); double dx = Math.abs(from.getTo().getX() -
		 * ending.getX()); double sx = ending.getX() - from.getTo().getX();
		 * double dy = Math.abs(from.getTo().getY() - ending.getY()); double sy
		 * = ending.getY() - from.getTo().getY(); Integer resultF = 0; Integer
		 * resultB = 0;
		 * 
		 * if (dx > 0 && dy > 0) { ++resultF; ++resultB; }
		 * 
		 * // examining two different paths from the beginning and from the end
		 * // along the X axis
		 * Point tempF = new Point(from.getTo()); Point tempB = new
		 * Point(ending); for (int i = 0; i < dx; ++i) { if (sx > 0) { tempF =
		 * Point.Add(tempF, Direction.east); tempB = Point.Add(tempF,
		 * Direction.west); } else if (sx < 0) { tempF = Point.Add(tempF,
		 * Direction.west); tempB = Point.Add(tempF, Direction.east); }
		 * 
		 * // tempF is not the end and not red // tempB is not the beginning and not red
		 * Point tF = new Point(tempF); if (resultF <= 1 &&
		 * !tempF.equals(ending) && (_objects.stream().anyMatch(pp -> pp.Color
		 * .equals(Colors.Red) && pp.Inner.equals(tF)))) { resultF = resultF +
		 * _penalizeTurns; } Point tB = new Point(tempB); if (resultB <= 1 &&
		 * !tempB.equals(from) && (_objects.stream().anyMatch(pp -> pp.Color
		 * .equals(Colors.Red) && pp.Inner.equals(tB)))) { resultB = resultB +
		 * _penalizeTurns; } }
		 * 
		 * // along the Y axis
		 * for (int i = 0; i < dy; ++i) { if (sy > 0) {
		 * tempF = Point.Add(tempF, Direction.north); tempB = Point.Add(tempB,
		 * Direction.south); } else if (sy < 0) { tempF = Point.Add(tempF,
		 * Direction.south); tempB = Point.Add(tempB, Direction.north); }
		 * 
		 * Point tF = new Point(tempF); if (resultF <= 1 &&
		 * !tempF.equals(ending) && (_objects.stream().anyMatch(pp -> pp.Color
		 * .equals(Colors.Red) && pp.Inner.equals(tF)))) { resultF = resultF +
		 * _penalizeTurns; } Point tB = new Point(tempB); if (resultB <= 1 &&
		 * !tempB.equals(from) && (_objects.stream().anyMatch(pp -> pp.Color
		 * .equals(Colors.Red) && pp.Inner.equals(tB)))) { resultB = resultB +
		 * _penalizeTurns; } }
		 * 
		 * Integer tempMin = Math.min(resultF, resultB); min = Math.min(min,
		 * tempMin);
		 */

		return (double) 0;// min;
	}

	// end Metrics

	// Methods

	private Set<Pair<Node, Double>> Gamma(Node parent) {
		Set<Pair<Node, Double>> result = new HashSet<Pair<Node, Double>>();

		if (!_boundary.isIn(parent.getTo()))
			return result;

		// Add possible neighbors
		if (!_colors.containsKey(parent.getTo())) {
			Direction sub = Point.directionOf(parent.getTo(), parent.getFrom());

			for (Direction dir : Direction.values()) {
				Double w = 0.0;

				if (dir.equals(sub))
					w = WEIGHT_LENGTH;
				else if (dir.equals(Direction.opposite(sub)))
					continue;
				else
					w = WEIGHT_LENGTH + WEIGHT_TURNS;

				Point p = Point.Add(parent.getTo(), dir);
				result.add(new Pair<Node, Double>(new Node(parent.getTo(), p), w));
			}
		} else if (_colors.get(parent.getTo()).equals(Color.Yellow)) {
			Direction sub = Point.directionOf(parent.getTo(), parent.getFrom());
			// straight, crossing
			result.add(new Pair<Node, Double>(new Node(parent.getTo(), Point.Add(parent.getTo(), sub)),
					WEIGHT_LENGTH + WEIGHT_CROSSING));
		}

		return result;
	}

	/**
	 * Returns the route from start to end.
	 * 
	 * @return the route from start to end.
	 */
	public ArrayList<Node> value() {
		ArrayList<Node> result = new ArrayList<Node>();

		Node n = PI.get(_end);

		do {
			result.add(n);
			n = PI.get(n);
		} while (n != null);

		// Reverse order
		ArrayList<Node> result2 = new ArrayList<Node>();
		for (int i = result.size() - 1; i >= 0; --i) {
			result2.add(result.get(i));
		}

		return result2;
	}

	/**
	 * Returns the number of node extensions completed during the path search.
	 * 
	 * @return the number of node extensions completed during the path search.
	 */
	public Integer extendsNum() {
		return _extends;
	}

}
