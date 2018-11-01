package hu.elte.txtuml.layout.visualizer.algorithms.boxes.utils.bellmanfordutils;

import java.util.Stack;

/**
 * The <tt>EdgeWeightedDirectedCycle</tt> class represents a data type for
 * determining whether an edge-weighted digraph has a directed cycle. The
 * <em>hasCycle</em> operation determines whether the edge-weighted digraph has
 * a directed cycle and, if so, the <em>cycle</em> operation returns one.
 * <p>
 * This implementation uses depth-first search. The constructor takes time
 * proportional to <em>V</em> + <em>E</em> (in the worst case), where <em>V</em>
 * is the number of vertices and <em>E</em> is the number of edges. Afterwards,
 * the <em>hasCycle</em> operation takes constant time; the <em>cycle</em>
 * operation takes time proportional to the length of the cycle.
 * <p>
 * See Topological to compute a topological order if the edge-weighted digraph
 * is acyclic.
 * <p>
 * For additional documentation, see <a href="/algs4/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedDirectedCycle
{
	private boolean[] marked; // marked[v] = has vertex v been marked?
	private DirectedEdge[] edgeTo; // edgeTo[v] = previous edge on path to v
	private boolean[] onStack; // onStack[v] = is vertex on the stack?
	private Stack<DirectedEdge> cycle; // directed cycle (or null if no such
										// cycle)
	
	/**
	 * Determines whether the edge-weighted digraph <tt>G</tt> has a directed
	 * cycle and, if so, finds such a cycle.
	 * 
	 * @param G
	 *            the edge-weighted digraph
	 */
	public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G)
	{
		marked = new boolean[G.V()];
		onStack = new boolean[G.V()];
		edgeTo = new DirectedEdge[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v])
				dfs(G, v);
		
		// check that digraph has a cycle
		assert check();
	}
	
	// check that algorithm computes either the topological order or finds a
	// directed cycle
	private void dfs(EdgeWeightedDigraph G, int v)
	{
		onStack[v] = true;
		marked[v] = true;
		for (DirectedEdge e : G.adj(v))
		{
			int w = e.to();
			
			// short circuit if directed cycle found
			if (cycle != null)
				return;
			
			// found new vertex, so recur
			else if (!marked[w])
			{
				edgeTo[w] = e;
				dfs(G, w);
			}
			
			// trace back directed cycle
			else if (onStack[w])
			{
				cycle = new Stack<DirectedEdge>();
				while (e.from() != w)
				{
					cycle.push(e);
					e = edgeTo[e.from()];
				}
				cycle.push(e);
			}
		}
		
		onStack[v] = false;
	}
	
	/**
	 * Does the edge-weighted digraph have a directed cycle?
	 * 
	 * @return <tt>true</tt> if the edge-weighted digraph has a directed cycle,
	 *         <tt>false</tt> otherwise
	 */
	public boolean hasCycle()
	{
		return cycle != null;
	}
	
	/**
	 * Returns a directed cycle if the edge-weighted digraph has a directed
	 * cycle, and <tt>null</tt> otherwise.
	 * 
	 * @return a directed cycle (as an iterable) if the edge-weighted digraph
	 *         has a directed cycle, and <tt>null</tt> otherwise
	 */
	public Iterable<DirectedEdge> cycle()
	{
		return cycle;
	}
	
	// certify that digraph is either acyclic or has a directed cycle
	private boolean check()
	{
		
		// edge-weighted digraph is cyclic
		if (hasCycle())
		{
			// verify cycle
			DirectedEdge first = null, last = null;
			for (DirectedEdge e : cycle())
			{
				if (first == null)
					first = e;
				if (last != null)
				{
					if (last.to() != e.from())
					{
						return false;
					}
				}
				last = e;
			}
			
			if (last.to() != first.from())
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Unit tests the <tt>EdgeWeightedDirectedCycle</tt> data type.
	 */
	/*
	 * public static void main(String[] args) {
	 * 
	 * // create random DAG with V vertices and E edges; then add F random //
	 * edges int V = Integer.parseInt(args[0]); int E =
	 * Integer.parseInt(args[1]); int F = Integer.parseInt(args[2]);
	 * EdgeWeightedDigraph G = new EdgeWeightedDigraph(V); int[] vertices = new
	 * int[V]; for (int i = 0; i < V; i++) vertices[i] = i;
	 * StdRandom.shuffle(vertices); for (int i = 0; i < E; i++) { int v, w; do {
	 * v = StdRandom.uniform(V); w = StdRandom.uniform(V); } while (v >= w);
	 * double weight = Math.random(); G.addEdge(new DirectedEdge(v, w, weight));
	 * }
	 * 
	 * // add F extra edges for (int i = 0; i < F; i++) { int v = (int)
	 * (Math.random() * V); int w = (int) (Math.random() * V); double weight =
	 * Math.random(); G.addEdge(new DirectedEdge(v, w, weight)); }
	 * 
	 * StdOut.println(G);
	 * 
	 * // find a directed cycle EdgeWeightedDirectedCycle finder = new
	 * EdgeWeightedDirectedCycle(G); if (finder.hasCycle()) {
	 * StdOut.print("Cycle: "); for (DirectedEdge e : finder.cycle()) {
	 * StdOut.print(e + " "); } StdOut.println(); }
	 * 
	 * // or give topologial sort else { StdOut.println("No directed cycle"); }
	 * }
	 */
	
}
