package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that helps the Graph Search algorithm to represent the graph.
 * 
 * @author Balázs Gregorics
 *
 */
public class Graph
{
	/**
	 * Nodes of the Graph.
	 */
	public Set<Point> Nodes;
	/**
	 * Links of the Graph.
	 */
	public Set<Link> Links;
	
	/**
	 * Create a Graph.
	 */
	public Graph()
	{
		Nodes = new HashSet<Point>();
		Links = new HashSet<Link>();
	}
	
	/**
	 * Add a link to the graph.
	 * 
	 * @param l
	 *            Link to add.
	 */
	public void add(Link l)
	{
		addLink(l);
	}
	
	/**
	 * Add a node to the graph.
	 * 
	 * @param n
	 *            Node to add.
	 */
	public void add(Point n)
	{
		addNode(n);
	}
	
	/**
	 * Add a node to the graph.
	 * 
	 * @param n
	 *            Node to add.
	 */
	public void addNode(Point n)
	{
		Nodes.add(n);
	}
	
	/**
	 * Add a link to the graph.
	 * 
	 * @param l
	 *            Link to add.
	 */
	public void addLink(Link l)
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
	public boolean contains(Point n)
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
	public boolean containsNode(Point n)
	{
		return Nodes.contains(n);
	}
	
	@Override
	public String toString()
	{
		String result = "";
		
		ArrayList<Point> ns = (ArrayList<Point>) Nodes.stream().collect(
				Collectors.toList());
		ns.sort((p1, p2) ->
		{
			if (p1.getX() < p2.getX())
				return -1;
			else if (p1.getX() == p2.getX() && p1.getY() < p2.getY())
				return -1;
			else if (p1.getX() == p2.getX() && p1.getY() == p2.getY())
				return 0;
			else
				return 1;
		});
		for (Point p : ns)
		{
			result += "\n" + p.toString() + ": ";
			for (Link l : Links)
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
