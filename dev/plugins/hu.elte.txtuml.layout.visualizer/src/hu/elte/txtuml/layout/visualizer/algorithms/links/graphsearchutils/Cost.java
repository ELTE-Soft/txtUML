package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchutils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that helps the Graph search algorithm to store the costs of each point.
 * 
 * @param <T>
 *            Type of the Nodes.
 */
public class Cost<T>
{
	private Map<T, Double> _c;
	
	/**
	 * Create Cost.
	 */
	public Cost()
	{
		_c = new HashMap<T, Double>();
	}
	
	/**
	 * Set cost of a point.
	 * 
	 * @param p
	 *            T to set the cost of.
	 * @param i
	 *            Cost to set.
	 */
	public void set(T p, Double i)
	{
		_c.put(p, i);
	}
	
	/**
	 * Get the cost of a point.
	 * 
	 * @param p
	 *            T to get the cost of.
	 * @return The cost of the point.
	 */
	public Double get(T p)
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
