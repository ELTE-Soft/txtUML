package hu.elte.txtuml.layout.visualizer.model;

import java.util.Random;

/**
 * Enumeration for Directions.
 * 
 * @author Balázs Gregorics
 *
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
	
	/***
	 * Convert from {@link Integer} to {@link Direction}.
	 * 
	 * @param x
	 *            {@link Integer} to convert from.
	 * @return {@link Direction} which corresponds to the {@link Integer}.
	 */
	public static Direction fromInteger(int x)
	{
		switch (x)
		{
			case 0:
				return north;
			case 1:
				return east;
			case 2:
				return south;
			case 3:
				return west;
		}
		return null;
	}
	
	/**
	 * Returns the next {@link Direction} in the enumeration.
	 * 
	 * @param dir
	 *            Current {@link Direction}.
	 * @return the next {@link Direction} in the enumeration.
	 */
	public static Direction nextDirection(Direction dir)
	{
		return Direction.fromInteger((dir.ordinal() + 1) % 4);
	}
	
	/**
	 * Returns the oposite {@link Direction} of the given dir.
	 * 
	 * @param dir
	 *            the {@link Direction} you want the opposite of.
	 * @return the oposite {@link Direction} of the given dir.
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
	 * Returns the values of the Enumeration {@link Direction} in a random
	 * order.
	 * 
	 * @param seed
	 *            Seed to generate random order.
	 * @return the values of the Enumeration {@link Direction} in a random
	 *         order.
	 */
	public static Direction[] valuesRandom(Integer seed)
	{
		Direction[] result = Direction.values();
		
		Random r = new Random(seed);
		int spin = r.nextInt() % 7;
		
		for (int i = 0; i < spin; ++i)
		{
			int fIndex = r.nextInt() % 3;
			int sIndex;
			do
			{
				sIndex = r.nextInt() % 3;
			} while (sIndex == fIndex);
			
			Direction swap = result[fIndex];
			result[fIndex] = result[sIndex];
			result[sIndex] = swap;
			
		}
		
		return result;
	}
}
