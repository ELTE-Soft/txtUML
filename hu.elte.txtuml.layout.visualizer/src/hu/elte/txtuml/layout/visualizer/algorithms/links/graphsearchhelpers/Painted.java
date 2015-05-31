package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to add additional information to other classes using
 * coloring.
 * 
 * @author Balázs Gregorics
 * @param <Item>
 *            Inner class to extend.
 */
public class Painted<Item>
{
	
	/**
	 * Enumeration for available colors.
	 * 
	 * @author Balázs Gregorics
	 */
	public enum Colors
	{
		/**
		 * White color.
		 */
		White,
		/**
		 * Gray color.
		 */
		Gray,
		/**
		 * Black color.
		 */
		Black,
		/**
		 * Red color.
		 */
		Red,
		/**
		 * Blue color.
		 */
		Blue,
		/**
		 * Green color.
		 */
		Green,
		/**
		 * Purple color.
		 */
		Purple,
		/**
		 * Yellow color.
		 */
		Yellow,
		/**
		 * Brown color.
		 */
		Brown
	}
	
	/**
	 * Color of the item.
	 */
	public Colors Color;
	/**
	 * Id of the batch.
	 */
	public Set<Integer> Batch;
	/**
	 * Inner item.
	 */
	public Item Inner;
	
	// Ctors
	
	/**
	 * Create an empty Painted class.
	 */
	public Painted()
	{
		
	}
	
	/**
	 * Create a Painted class.
	 * 
	 * @param c
	 *            Color of the Item.
	 * @param i
	 *            Inner Item to paint.
	 */
	public Painted(Colors c, Item i)
	{
		Color = c;
		Inner = i;
		Batch = new HashSet<Integer>();
	}
	
	/**
	 * Create a Painted class.
	 * 
	 * @param c
	 *            Color of the Item.
	 * @param i
	 *            Inner Item to paint.
	 * @param bs
	 *            Set of Ids of the batch.
	 */
	public Painted(Colors c, Item i, Set<Integer> bs)
	{
		Color = c;
		Inner = i;
		Batch = bs;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param p
	 */
	public Painted(Painted<Item> p)
	{
		Color = p.Color;
		Inner = p.Inner;
		Batch = p.Batch;
	}
	
	// end Ctors
	
	@Override
	public boolean equals(Object obj)
	{
		return this.Inner.equals(obj);
	}
	
	@Override
	public int hashCode()
	{
		return Inner.hashCode();
	}
	
	@Override
	protected Painted<Item> clone()
	{
		return new Painted<Item>(this);
	}
	
	@Override
	public String toString()
	{
		String result;
		
		result = "Color: " + Color.toString();
		result += "[Batch: " + Batch.toString() + "]";
		result += ", Inner: " + Inner.toString() + ".";
		
		return result;
	}
	
}
