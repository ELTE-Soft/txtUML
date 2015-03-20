package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class to help the Graph search algorithm to store the parent of a node in the
 * graph.
 * 
 * @author Balázs Gregorics
 *
 */
public class Parent
{
	private Map<Point, Point> _pi;
	
	/**
	 * Create Parent.
	 */
	public Parent()
	{
		_pi = new HashMap<Point, Point>();
	}
	
	/**
	 * Set a child-parent relationship.
	 * 
	 * @param child
	 *            Child.
	 * @param parent
	 *            Parent.
	 */
	public void set(Point child, Point parent)
	{
		_pi.put(child, parent);
	}
	
	/**
	 * Get the parent of a child.
	 * 
	 * @param child
	 *            Child.
	 * @return parent of the child.
	 */
	public Point get(Point child)
	{
		return _pi.get(child);
	}
	
	/**
	 * Method that determines whether a specific parent has any child.
	 * 
	 * @param parent
	 *            Parent to search for.
	 * @return True if the given parent has a child, else false.
	 */
	public Boolean hasChild(Point parent)
	{
		for (Entry<Point, Point> entry : _pi.entrySet())
		{
			if (entry.getValue() != null)
			{
				if (entry.getValue().equals(parent) && entry.getKey() != null)
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString()
	{
		String result;
		
		result = _pi.toString();
		
		return result;
	}
	
}
