package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class to help the Graph search algorithm to store the parent of a node in the
 * graph.
 * 
 * @author Balázs Gregorics
 * @param <T>
 *            Type of the Nodes.
 *
 */
public class Parent<T>
{
	private Map<T, T> _pi;
	
	/**
	 * Create Parent.
	 */
	public Parent()
	{
		_pi = new HashMap<T, T>();
	}
	
	/**
	 * Set a child-parent relationship.
	 * 
	 * @param child
	 *            Child.
	 * @param parent
	 *            Parent.
	 */
	public void set(T child, T parent)
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
	public T get(T child)
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
	public Boolean hasChild(T parent)
	{
		for (Entry<T, T> entry : _pi.entrySet())
		{
			if (entry.getValue() != null && entry.getValue().equals(parent))
			{
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
