package hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers;

import java.util.Set;

/**
 * This class is used to add additional information to other classes using
 * coloring.
 * 
 * @param <Item>
 *            Inner class to extend.
 */
public class Painted<Item>
{
	/**
	 * Color of the item.
	 */
	public Color Color;
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
	public Painted(Color c, Item i)
	{
		Color = c;
		Inner = i;
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
	public Painted(Color c, Item i, Set<Integer> bs)
	{
		Color = c;
		Inner = i;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param p
	 *            {@link Painted} to copy.
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
