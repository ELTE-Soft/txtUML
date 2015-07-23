package hu.elte.txtuml.layout.visualizer.model;


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
	
}
