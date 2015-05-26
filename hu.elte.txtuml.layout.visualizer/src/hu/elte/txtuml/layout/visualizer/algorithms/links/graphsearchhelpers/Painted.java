package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

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
		result += ", Inner: " + Inner.toString() + ".";
		
		return result;
	}
	
}
