package hu.elte.txtuml.layout.visualizer.model;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;

/**
 * Enumeration for Directions.
 */
public enum Direction
{
	/**
	 * North (0, 1).
	 */
	north,
	/**
	 * East (1, 0).
	 */
	east,
	/**
	 * South (0, -1).
	 */
	south,
	/**
	 * West (-1, 0).
	 */
	west;
	
	/**
	 * Returns the opposite {@link Direction} of the given dir.
	 * 
	 * @param dir
	 *            the {@link Direction} you want the opposite of.
	 * @return the opposite {@link Direction} of the given dir.
	 */
	public static Direction opposite(Direction dir)
	{
		switch (dir)
		{
			case north:
				return south;
			case east:
				return west;
			case south:
				return north;
			case west:
				return east;
		}
		return null;
	}
	
	/**
	 * Returns the {@link Direction} equivalent of an {@link Integer}.
	 * 
	 * @param i the {@link Integer} to convert.
	 * @return the {@link Direction} equivalent of an {@link Integer}.
	 * @throws ConversionException
	 *             Throws if the {@link Integer} can not be converted into
	 *             {@link Direction}.
	 */
	public static Direction fromInteger(Integer i) throws ConversionException
	{
		switch (i)
		{
			case 0:
				return Direction.north;
			case 1:
				return Direction.east;
			case 2:
				return Direction.south;
			case 3:
				return Direction.west;
		}
		
		throw new ConversionException("Cannot convert Integer " + i.toString()
				+ " to Direction!");
	}
	
}
