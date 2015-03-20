package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

/**
 * Class that represents an edge in the graph that the Graph search algorithm
 * uses.
 * 
 * @author Balázs Gregorics
 *
 */
public class Link
{
	/**
	 * One end of the edge.
	 */
	public Point From;
	/**
	 * Other end of the edge.
	 */
	public Point To;
	
	/**
	 * Create edge.
	 * 
	 * @param f
	 *            One end.
	 * @param t
	 *            Other end.
	 */
	public Link(Point f, Point t)
	{
		From = f;
		To = t;
	}
}
