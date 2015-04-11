package hu.elte.txtuml.layout.visualizer.helpers;

import hu.elte.txtuml.layout.visualizer.annotations.StatementType;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Static class for various algorithm related helping tasks, such as
 * conversions, cloning or parsing.
 * 
 * @author Balázs Gregorics
 *
 */
public class Helper
{
	
	/**
	 * Method to clone a specific Set. A set of Points.
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
	 * Method to clone a specific map. A map of String and ArrayList\<String\>.
	 * 
	 * @param toClone
	 *            The map to clone.
	 * @return A completly cloned map.
	 */
	public static HashMap<String, ArrayList<String>> cloneMap(
			HashMap<String, ArrayList<String>> toClone)
	{
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		
		for (Entry<String, ArrayList<String>> entry : toClone.entrySet())
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
	public static ArrayList<String> cloneStringList(ArrayList<String> toClone)
	{
		ArrayList<String> result = new ArrayList<String>();
		
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
	public static ArrayList<Point> clonePointList(ArrayList<Point> toClone)
	{
		ArrayList<Point> result = new ArrayList<Point>();
		
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
	public static ArrayList<LineAssociation> cloneLinkList(
			ArrayList<LineAssociation> toClone)
	{
		ArrayList<LineAssociation> result = new ArrayList<LineAssociation>();
		
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
				return Direction.north;
			case south:
				return Direction.south;
			case east:
				return Direction.east;
			case west:
				return Direction.west;
			default:
				throw new ConversionException("Cannot convert type " + ty
						+ " to Direction!");
		}
	}
	
}
