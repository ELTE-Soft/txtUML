package hu.elte.txtuml.layout.visualizer.algorithms.boxes.utils.bellmanfordutils;

import hu.elte.txtuml.layout.visualizer.statements.Statement;

/**
 * The <tt>DirectedEdge</tt> class represents a weighted edge in an
 * {@link EdgeWeightedDigraph}. Each edge consists of two integers (naming the
 * two vertices) and a real-value weight. The data type provides methods for
 * accessing the two endpoints of the directed edge and the weight.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of <i>Algorithms,
 * 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * 
 * Additional data added to aid Diagram generation.
 */

public class DirectedEdge
{
	
	private final int v;
	private final int w;
	private final double weight;
	private final Statement stat;
	
	/**
	 * Initializes a directed edge from vertex <tt>v</tt> to vertex <tt>w</tt>
	 * with the given <tt>weight</tt>.
	 * 
	 * @param p_v
	 *            the tail vertex
	 * @param p_w
	 *            the head vertex
	 * @param p_weight
	 *            the weight of the directed edge
	 * @param p_stat
	 *            the statement associated with this edge.
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if either <tt>v</tt> or <tt>w</tt> is a negative integer
	 * @throws IllegalArgumentException
	 *             if <tt>weight</tt> is <tt>NaN</tt>
	 */
	public DirectedEdge(int p_v, int p_w, double p_weight, Statement p_stat)
	{
		if (p_v < 0)
			throw new IndexOutOfBoundsException(
					"Vertex names must be nonnegative integers");
		if (p_w < 0)
			throw new IndexOutOfBoundsException(
					"Vertex names must be nonnegative integers");
		if (Double.isNaN(p_weight))
			throw new IllegalArgumentException("Weight is NaN");
		this.v = p_v;
		this.w = p_w;
		this.weight = p_weight;
		this.stat = p_stat;
	}
	
	/**
	 * Returns the tail vertex of the directed edge.
	 * 
	 * @return the tail vertex of the directed edge
	 */
	public int from()
	{
		return v;
	}
	
	/**
	 * Returns the head vertex of the directed edge.
	 * 
	 * @return the head vertex of the directed edge
	 */
	public int to()
	{
		return w;
	}
	
	/**
	 * Returns the weight of the directed edge.
	 * 
	 * @return the weight of the directed edge
	 */
	public double weight()
	{
		return weight;
	}
	
	/**
	 * Returns the statement associated with this edge.
	 * 
	 * @return the statement associated with this edge.
	 */
	public Statement stat()
	{
		return stat;
	}
	
	/**
	 * Returns a string representation of the directed edge.
	 * 
	 * @return a string representation of the directed edge
	 */
	@Override
	public String toString()
	{
		return v + "->" + w + " " + String.format("%5.2f", weight);
	}
	
	/**
	 * Unit tests the <tt>DirectedEdge</tt> data type.
	 */
	/*
	 * public static void main(String[] args) { DirectedEdge e = new
	 * DirectedEdge(12, 23, 3.14); StdOut.println(e); }
	 */
}
