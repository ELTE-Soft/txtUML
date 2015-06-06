package hu.elte.txtuml.layout.visualizer.model;

/***
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
	 * Convert from Integer to Direction.
	 * 
	 * @param x
	 *            Integer to convert from.
	 * @return Direction which corresponds to the Integer.
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
	 * Method to get the next Direction in the enumeration.
	 * 
	 * @param dir
	 *            Current Direction.
	 * @return Next Direction.
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
	
}
