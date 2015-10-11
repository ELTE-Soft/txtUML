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
	
	/**
	 * Initializes class.
	 */
	public static void init()
	{
		_measures = new ArrayList<Double>();
	}
	
	/**
	 * Add a measurement.
	 * 
	 * @param m
	 *            measurement to add.
	 */
	public static void addMeasure(Double m)
	{
		_measures.add(m);
	}
	
	/**
	 * Returns the average of the measures.
	 * 
	 * @return the average of the measures.
	 */
	public static Double avg()
	{
		return sum() / _measures.size();
	}
	
	/**
	 * Returns the sum of measures.
	 * 
	 * @return the sum of measures.
	 */
	public static Double sum()
	{
		Double summ = 0.0;
		for (Double d : _measures)
		{
			summ += d;
		}
		
		return summ;
	}
	
	/**
	 * Returns the minimal measurement.
	 * 
	 * @return the minimal measurement.
	 */
	public static Double min()
	{
		return _measures.stream().min((d1, d2) ->
		{
			return Double.compare(d1, d2);
		}).get();
	}
	
	/**
	 * Returns the maximal measurement.
	 * 
	 * @return the maximal measurement.
	 */
	public static Double max()
	{
		return _measures.stream().max((d1, d2) ->
		{
			return Double.compare(d1, d2);
		}).get();
	}
	
	/**
	 * Returns the count of the measures.
	 * 
	 * @return the count of the measures.
	 */
	public static Integer count()
	{
		return _measures.size();
	}
	
}
