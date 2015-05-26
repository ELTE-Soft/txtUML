package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that helps the Graph Search algorithm to represent the graph.
 * 
 * @author Balázs Gregorics
 * @param <N>
 *            The type of nodes.
 *
 */
public class Graph<N>
{
	/**
	 * Nodes of the Graph.
	 */
	public Set<N> Nodes;
	/**
	 * Links of the Graph.
	 */
	public Set<Link<N>> Links;
	
	/**
	 * Create a Graph.
	 */
	public Graph()
	{
		Nodes = new HashSet<N>();
		Links = new HashSet<Link<N>>();
	}
	
	/**
	 * Add a Link<N> to the graph.
	 * 
	 * @param l
	 *            Link<N> to add.
	 */
	public void add(Link<N> l)
	{
		addLink(l);
	}
	
	/**
	 * Add a node to the graph.
	 * 
	 * @param n
	 *            Node to add.
	 */
	public void add(N n)
	{
		addNode(n);
	}
	
	/**
	 * Add a node to the graph.
	 * 
	 * @param n
	 *            Node to add.
	 */
	public void addNode(N n)
	{
		Nodes.add(n);
	}
	
	/**
	 * Add a Link<N> to the graph.
	 * 
	 * @param l
	 *            Link<N> to add.
	 */
	public void addLink(Link<N> l)
	{
		Links.add(l);
	}
	
	/**
	 * Method to determine if a node is already present in the graph.
	 * 
	 * @param n
	 *            Node to search for.
	 * @return True if the graph contains the node, else False.
	 */
	public boolean contains(N n)
	{
		return containsNode(n);
	}
	
	/**
	 * Method to determine if a node is already present in the graph.
	 * 
	 * @param n
	 *            Node to search for.
	 * @return True if the graph contains the node, else False.
	 */
	public boolean containsNode(N n)
	{
		return Nodes.contains(n);
	}
	
	/**
	 * Returns the adjacent nodes of a certain node in the graph.
	 * 
	 * @param n
	 *            The node that we seek the adjacents of.
	 * @return The set of the adjacent nodes.
	 */
	public Set<N> adjacentNodes(N n)
	{
		return (Set<N>) Links.stream().filter(l -> l.From.equals(n)).map(l -> l.To)
				.collect(Collectors.toSet());
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		ArrayList<N> ns = (ArrayList<N>) Nodes.stream().collect(Collectors.toList());
		for (N p : ns)
		{
			result += "\n" + p.toString() + ": ";
			for (Link<N> l : Links)
			{
				if (l.From.equals(p))
				{
					result += l.To.toString() + ", ";
				}
				else if (l.To.equals(p))
				{
					result += l.From.toString() + ", ";
				}
			}
		}
		
		return result;
	}
}
