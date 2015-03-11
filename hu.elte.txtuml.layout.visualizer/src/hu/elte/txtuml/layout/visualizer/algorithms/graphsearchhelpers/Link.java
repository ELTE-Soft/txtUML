package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

public class Link
{
	public Point From;
	public Point To;
	
	public Link(Point f, Point t)
	{
		From = f;
		To = t;
	}
}
