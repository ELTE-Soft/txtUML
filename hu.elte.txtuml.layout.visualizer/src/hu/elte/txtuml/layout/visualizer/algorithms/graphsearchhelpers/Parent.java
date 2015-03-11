package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.HashMap;
import java.util.Map;

public class Parent
{
	private Map<Point, Point> _pi;
	
	public Parent()
	{
		_pi = new HashMap<Point, Point>();
	}
	
	public void set(Point child, Point parent)
	{
		_pi.put(child, parent);
	}
	
	public Point get(Point child)
	{
		return _pi.get(child);
	}
}
