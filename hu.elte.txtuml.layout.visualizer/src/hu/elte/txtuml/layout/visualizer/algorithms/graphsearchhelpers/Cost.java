package hu.elte.txtuml.layout.visualizer.algorithms.graphsearchhelpers;

import hu.elte.txtuml.layout.visualizer.model.Point;

import java.util.HashMap;
import java.util.Map;

public class Cost
{
	private Map<Point, Integer> _c;
	
	public Cost()
	{
		_c = new HashMap<Point, Integer>();
	}
	
	public void set(Point p, Integer i)
	{
		_c.put(p, i);
	}
	
	public Integer get(Point p)
	{
		return _c.get(p);
	}
}
