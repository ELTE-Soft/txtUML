package hu.elte.txtuml.layout.visualizer.algorithms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

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
	
	/**
	 * Greatest Common Divisor
	 * @param a
	 * @param b
	 * @return
	 */
	public static Integer gcd(Integer a, Integer b)
	{
		if(b == 0)
		{
			return a;
		}
		else
		{
			return Helper.gcd(b, a % b);
		}
	}
	
	/**
	 * Least Common Multiplier
	 * @param a
	 * @param b
	 * @return
	 */
	public static Integer lcm(Integer a, Integer b)
	{
		return ( Math.abs(a)/ gcd(a,b) ) * Math.abs(b);
	}
	
	public static Integer lcm(List<Integer> nums)
	{
		if(nums.size() < 2)
			throw new IllegalArgumentException("Need a list of at least 2 numbers!");
		
		Integer result = lcm(nums.get(0), nums.get(1));
		
		for(Integer i : nums.subList(2, nums.size()))
		{
			result = lcm(i, result);
		}
		
		return result;
	}
}
