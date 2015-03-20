package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that helps the Graph search algorithm to store the costs of each point.
 * 
 * @author Balázs Gregorics
 *
 */
public class Cost
{
	private Map<Point, Integer> _c;
	
	/**
	 * Create Cost.
	 */
	public Cost()
	{
		_c = new HashMap<Point, Integer>();
	}
	
	/**
	 * Set cost of a point.
	 * 
	 * @param p
	 *            Point to set the cost of.
	 * @param i
	 *            Cost to set.
	 */
	public void set(Point p, Integer i)
	{
		_c.put(p, i);
	}
	
	/**
	 * Get the cost of a point.
	 * 
	 * @param p
	 *            Point to get the cost of.
	 * @return The cost of the point.
	 */
	public Integer get(Point p)
	{
		return _c.get(p);
	}
	
	@Override
	public String toString()
	{
		String result;
		
		result = _c.toString();
		
		return result;
	}
}
