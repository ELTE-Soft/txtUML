package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

/**
 * Class that represents an edge in the graph that the Graph search algorithm
 * uses.
 * 
 * @author Balázs Gregorics
 * @param <N>
 *            Type of the nodes that the link connects.
 *
 */
public class Link<N>
{
	/**
	 * One end of the edge.
	 */
	public N From;
	/**
	 * Other end of the edge.
	 */
	public N To;
	
	/**
	 * Create edge.
	 * 
	 * @param f
	 *            One end.
	 * @param t
	 *            Other end.
	 */
	public Link(N f, N t)
	{
		From = f;
		To = t;
	}
}
