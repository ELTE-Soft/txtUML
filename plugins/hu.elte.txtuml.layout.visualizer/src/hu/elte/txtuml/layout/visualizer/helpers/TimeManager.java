package hu.elte.txtuml.layout.visualizer.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Time measuring class.
 * 
 * @author Balázs Gregorics
 *
 */
public class TimeManager
{
	private static List<Double> _measures;
	
	public static void init()
	{
		_measures = new ArrayList<Double>();
	}
	
	public static void addMeasure(Double m)
	{
		_measures.add(m);
	}
	
	public static Double avg()
	{
		return sum() / _measures.size();
	}
	
	public static Double sum()
	{
		Double summ = 0.0;
		for (Double d : _measures)
		{
			summ += d;
		}
		
		return summ;
	}
	
	public static Double min()
	{
		return _measures.stream().min((d1, d2) ->
		{
			return Double.compare(d1, d2);
		}).get();
	}
	
	public static Double max()
	{
		return _measures.stream().max((d1, d2) ->
		{
			return Double.compare(d1, d2);
		}).get();
	}
	
	public static Integer count()
	{
		return _measures.size();
	}
	
}
