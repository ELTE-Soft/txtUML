package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

/**
 * Abstract Node for the graph.
 */
public class Node
{
	
	private Point _from;
	
	private Point _to;
	
	/**
	 * Returns the start point.
	 * 
	 * @return the start point.
	 */
	public Point getFrom()
	{
		return _from;
	}
	
	/**
	 * Returns the end point.
	 * 
	 * @return the end point.
	 */
	public Point getTo()
	{
		return _to;
	}
	
	/**
	 * Create Node.
	 * 
	 * @param f
	 *            Start point.
	 * @param t
	 *            End point.
	 */
	public Node(Point f, Point t)
	{
		_from = f;
		_to = t;
	}
	
	/**
	 * Copy a Node.
	 * 
	 * @param n
	 *            Node to copy.
	 */
	public Node(Node n)
	{
		_from = n._from;
		_to = n._to;
	}
	
	// Equality
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
		if (obj instanceof Node)
		{
			Node n = this;
			Node m = (Node) obj;
			
			return n._from.equals(m._from) && n._to.equals(m._to);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _from.hashCode();
		result = prime * result + _to.hashCode();
		return result;
	}
	
	// end Equality
	
	@Override
	public String toString()
	{
		String result = "";
		
		result += "(" + _from.toString() + " -> " + _to.toString() + ")";
		
		return result;
	}
}
