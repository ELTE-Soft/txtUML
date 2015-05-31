package hu.elte.txtuml.layout.visualizer.algorithms.links;

import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.helpers.Helper;
import hu.elte.txtuml.layout.visualizer.helpers.Pair;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

class ArrangeReflexiveAssociations
{
	private ArrayList<LineAssociation> _reflexives;
	
	public ArrangeReflexiveAssociations(ArrayList<LineAssociation> refls)
	{
		_reflexives = Helper.cloneLinkList(refls);
	}
	
	public ArrayList<Point> arrangeReflexive(LineAssociation refl,
			Set<RectangleObject> diagramObjects, Set<LineAssociation> myReflexives)
			throws CannotFindAssociationRouteException
	{
		RectangleObject obj = diagramObjects.stream()
				.filter(o -> o.getName().equals(refl.getFrom())).findFirst().get();
		
		// get direction to put
		HashMap<Direction, Integer> directionPopulations = getLeastPopulatedDirection(
				obj, diagramObjects, myReflexives);
		
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
					a -> a.getRoute().contains(p))
					|| Helper.isCornerPoint(p, obj));
			possibleStarts.sort((p1, p2) ->
			{
				return -1
						* Double.compare(Point.Substract(p1, obj.getPosition()).length(),
								Point.Substract(p2, obj.getPosition()).length());
			});
			
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
					ArrayList<Point> result = tryDrawReflexive(p, ends.First, dir,
							ends.Second, diagramObjects);
					result.add(0, obj.getPosition());
					result.add(obj.getPosition());
					
					return result;
				}
				catch (CannotFindAssociationRouteException e)
				{
					String out = "[\n" + p + "(" + dir + ") -> " + ends.First + "("
							+ ends.Second + ")\n";
					out = out
							+ "obj: "
							+ obj.getPerimiterPoints()
									.stream()
									.filter(p_lambda -> (p_lambda.isInTheDirection(
											obj.getPosition(), dir, true) || p_lambda
											.isInTheDirection(obj.getPosition(),
													ends.Second, true))
											&& !myReflexives.stream().anyMatch(
													a -> a.getRoute().contains(p_lambda))
											&& !Helper.isCornerPoint(p_lambda, obj))
									.collect(Collectors.toSet()) + "\n";
					out = out
							+ "isInStartDir:"
							+ obj.getPerimiterPoints()
									.stream()
									.filter(p_lambda -> p_lambda.isInTheDirection(
											obj.getPosition(), dir, true))
									.collect(Collectors.toSet()) + "\n";
					out = out
							+ "isInEndDir: "
							+ obj.getPerimiterPoints()
									.stream()
									.filter(p_lambda -> p_lambda.isInTheDirection(
											obj.getPosition(), ends.Second, true))
									.collect(Collectors.toSet()) + "\n";
					out = out
							+ "occupied by other: "
							+ obj.getPerimiterPoints()
									.stream()
									.filter(p_lambda -> myReflexives.stream().anyMatch(
											a -> a.getRoute().contains(p_lambda)))
									.collect(Collectors.toSet()) + "\n";
					out = out + "]";
					System.out.println(out);
				}
			}
		}
		
		throw new CannotFindAssociationRouteException(
				"Cannot find route for reflexive link: " + refl.toString() + "!");
	}
	
	private HashMap<Direction, Integer> getLeastPopulatedDirection(RectangleObject obj,
			Set<RectangleObject> diagramObjects, Set<LineAssociation> myReflexives)
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
				if (Point.isInTheDirection(obj.getPosition(), o.getPosition(), dir, true))
				{
					arrangement.put(dir, arrangement.get(dir) + 1);
				}
			}
		}
		
		for (LineAssociation a : myReflexives.stream()
				.filter(a_lambda -> a_lambda.isPlaced()).collect(Collectors.toSet()))
		{
			for (Direction dir : Direction.values())
			{
				if (Point.isInTheDirection(obj.getPosition(), a.getRoute().get(1), dir,
						true)
						|| Point.isInTheDirection(obj.getPosition(),
								a.getRoute().get(a.getRoute().size() - 2), dir, true))
				{
					arrangement.put(dir, arrangement.get(dir) + 4);
				}
			}
		}
		
		return arrangement;
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
				possibleEnds.removeIf(p_lambda -> Helper.isCornerPoint(p_lambda, obj));
				possibleEnds.removeIf(p_lambda -> !Point.isInTheDirection(
						obj.getPosition(), p_lambda, startDir, true));
				possibleEnds.removeIf(p_lambda -> myReflexives.stream().anyMatch(
						a_lambda -> a_lambda.getRoute().contains(p_lambda)));
				
				for (Point poi : possibleEnds)
				{
					Double _a_ = Point.Substract(p, obj.getPosition()).length();
					Double _b_ = Point.Substract(poi, obj.getPosition()).length();
					if (_a_.equals(_b_))
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
							"Cannot draw reflexive link! (Start could not turn nor go forward)");
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
							"Cannot draw reflexive link! (End  could not turn nor go forward)");
			}
		}
		
		if (!movingForward1.equals(movingForward2))
			throw new CannotFindAssociationRouteException(
					"Cannot draw reflexive link! (One couldn't turn when other did)");
		
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
						"Cannot draw reflexive link! (Start second part could not go forward)");
			
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
						"Cannot draw reflexive link! (End second part could not go forward)");
		}
		
		// Unite the routes
		for (int i = (route2.size() - 2); i >= 0; --i)
		{
			route1.add(route2.get(i));
		}
		
		return route1;
	}
	
}
