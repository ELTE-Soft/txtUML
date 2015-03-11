package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph
{
	public Set<Point> Nodes;
	public Set<Link> Links;
	
	public Graph()
	{
		Nodes = new HashSet<Point>();
		Links = new HashSet<Link>();
	}
	
	public void add(Link l)
	{
		addLink(l);
	}
	
	public void add(Point n)
	{
		addNode(n);
	}
	
	public void addNode(Point n)
	{
		Nodes.add(n);
	}
	
	public void addLink(Link l)
	{
		Links.add(l);
	}
	
	public boolean contains(Point n)
	{
		return containsNode(n);
	}
	
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
