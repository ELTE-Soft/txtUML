package hu.elte.txtuml.layout.visualizer.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

/**
 * Static class for various algorithm related helping tasks, such as
 * conversions, cloning or parsing.
 */
public class Helper
{
	
	/**
	 * Method to clone a specific Set. A set of {@link Point}s.
	 * 
	 * @param toClone
	 *            The set to clone.
	 * @return A completly cloned set.
	 */
	public static Set<Point> clonePointSet(Set<Point> toClone)
	{
		Set<Point> result = new HashSet<Point>();
		
		for (Point p : toClone)
		{
			result.add(p.clone());
		}
		
		return result;
	}
	
	/**
	 * Method to clone a specific List. A list of {@link Statement}s.
	 * 
	 * @param toClone
	 *            List of {@link Statement}s
	 * @return The cloned list.
	 */
	public static List<Statement> cloneStatementList(List<Statement> toClone)
	{
		List<Statement> result = new ArrayList<Statement>();
		
		for (Statement s : toClone)
		{
			result.add(new Statement(s));
		}
		
		return result;
	}
	
	/**
	 * Method to clone a specific Set. A set of Strings.
	 * 
	 * @param toClone
	 *            The set to clone.
	 * @return A completly cloned set.
	 */
	public static Set<String> cloneStringSet(Set<String> toClone)
	{
		Set<String> result = new HashSet<String>();
		
		for (String s : toClone)
		{
			result.add(new String(s));
		}
		
		return result;
	}
	
	/**
	 * Method to clone a specific map. A map of String and List\<String\>.
	 * 
	 * @param toClone
	 *            The map to clone.
	 * @return A completly cloned map.
	 */
	public static HashMap<String, List<String>> cloneMap(
			HashMap<String, List<String>> toClone)
	{
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		
		for (Entry<String, List<String>> entry : toClone.entrySet())
		{
			result.put(new String(entry.getKey()), cloneStringList(entry.getValue()));
		}
		
		return result;
	}
	
	/**
	 * Method to clone a specific list. A list of Strings.
	 * 
	 * @param toClone
	 *            The list to clone.
	 * @return A completly cloned list.
	 */
	public static List<String> cloneStringList(List<String> toClone)
	{
		List<String> result = new ArrayList<String>();
		
		for (String s : toClone)
		{
			result.add(new String(s));
		}
		
		return result;
	}
	
	/**
	 * Method to clone a specific list. A list of Points.
	 * 
	 * @param toClone
	 *            The list to clone.
	 * @return A completly cloned list.
	 */
	public static List<Point> clonePointList(List<Point> toClone)
	{
		List<Point> result = new ArrayList<Point>();
		
		for (Point p : toClone)
		{
			result.add(p.clone());
		}
		
		return result;
	}
	
	/**
	 * Method to clone a specific list. A list of LineAssociations.
	 * 
	 * @param toClone
	 *            The list to clone.
	 * @return A completly cloned list.
	 */
	public static List<LineAssociation> cloneLinkList(List<LineAssociation> toClone)
	{
		List<LineAssociation> result = new ArrayList<LineAssociation>();
		
		for (LineAssociation a : toClone)
		{
			result.add(a.clone());
		}
		
		return result;
	}
	
	/**
	 * Method for parsing a String value to Integer if possible.
	 * 
	 * @param value
	 *            String to parse.
	 * @return True if the value can be parsed, else False.
	 */
	public static boolean tryParseInt(String value)
	{
		try
		{
			Integer.parseInt(value);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
	
	/**
	 * Method to convert {@link StatementType} to {@link Direction}.
	 * 
	 * @param ty
	 *            StatementType to convert.
	 * @return The converted Direction if possible.
	 * @throws ConversionException
	 *             Throws if the given StatementType is cannot be converted to
	 *             any Direction.
	 */
	public static Direction asDirection(StatementType ty) throws ConversionException
	{
		switch (ty)
		{
			case north:
			case above:
				return Direction.north;
			case south:
			case below:
				return Direction.south;
			case east:
			case right:
				return Direction.east;
			case west:
			case left:
				return Direction.west;
			case horizontal:
			case phantom:
			case priority:
			case unknown:
			case vertical:
			case corridorsize:
			case overlaparrange:
			default:
				throw new ConversionException("Cannot convert type " + ty
						+ " to Direction!");
		}
	}
	
	/**
	 * Method to convert {@link Direction} to {@link StatementType}.
	 * 
	 * @param dir
	 *            Dire.ction to convert
	 * @return The converted StatementType
	 * @throws ConversionException
	 *             Throws if the given Direction is cannot be converted to any
	 *             StatementType.
	 */
	public static StatementType asStatementType(Direction dir) throws ConversionException
	{
		switch (dir)
		{
			case north:
				return StatementType.north;
			case south:
				return StatementType.south;
			case east:
				return StatementType.east;
			case west:
				return StatementType.west;
			default:
				throw new ConversionException("Cannot convert direction " + dir
						+ " to StatementType!");
		}
	}
	
	/**
	 * Detects whether we should arrange reflexive links on an object's same
	 * side or not, depending on the diagram's type.
	 * 
	 * @param type
	 *            Diagram' type we want to layout.
	 * @return True if the reflexive links should be on the same side of an
	 *         object.
	 */
	public static boolean isReflexiveOnSameSide(DiagramType type)
	{
		switch (type)
		{
			case Class:
				return true;
			case State:
			case Activity:
				return false;
			default:
				return true;
		}
	}
	
	/**
	 * Converts a Point(Vector) to a Direction.
	 * 
	 * @param p
	 *            Point to convert.
	 * @return The converted Direction.
	 * @throws ConversionException
	 *             Throws if no such Direction exists.
	 */
	public static Direction asDirection(Point p) throws ConversionException
	{
		if (p.getX() == 0 && p.getY() > 0)
			return Direction.north;
		if (p.getX() == 0 && p.getY() < 0)
			return Direction.south;
		if (p.getX() > 0 && p.getY() == 0)
			return Direction.east;
		if (p.getX() < 0 && p.getY() == 0)
			return Direction.west;
		
		throw new ConversionException("Cannot convert Point " + p.toString()
				+ " to any Direction!");
	}
	
	/**
	 * Concatenates a list of lists of points to a single list of points.
	 * 
	 * @param list
	 *            List to flatten.
	 * @return List of points.
	 */
	public static List<Point> concatPointList(List<List<Point>> list)
	{
		List<Point> result = new ArrayList<Point>();
		
		for (List<Point> innerlist : list)
		{
			for (Point p : innerlist)
			{
				result.add(p);
			}
		}
		
		return result;
	}
	
	/**
	 * Concatenates a set of sets of points to a single set of points.
	 * 
	 * @param set
	 *            Set to flatten.
	 * @return Set of points.
	 */
	public static Set<Point> concatPointSet(Set<Set<Point>> set)
	{
		Set<Point> result = new HashSet<Point>();
		
		for (Set<Point> innerset : set)
		{
			for (Point p : innerset)
			{
				result.add(p);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns whether the Point p is the corner point of the Object obj.
	 * 
	 * @param p
	 *            Point to check.
	 * @param obj
	 *            Object to check.
	 * @return True if p is a corner point of obj.
	 */
	public static Boolean isCornerPoint(Point p, RectangleObject obj)
	{
		if (p.equals(obj.getPosition())
				|| p.equals(Point.Add(obj.getPosition(),
						Point.Multiply(Direction.east, obj.getWidth() - 1)))
				|| p.equals(Point.Add(obj.getPosition(),
						Point.Multiply(Direction.south, obj.getWidth() - 1)))
				|| p.equals(obj.getBottomRight()))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if two {@link Double} values are close enough to each other to a
	 * certain error value so they can be handled as equal.
	 * 
	 * @param a
	 *            first value.
	 * @param b
	 *            second value.
	 * @param error
	 *            error value.
	 * @return true if ( |a-b|<error )
	 */
	public static Boolean isAlmostEqual(Double a, Double b, Double error)
	{
		return Math.abs(a - b) < error;
	}
	
}
