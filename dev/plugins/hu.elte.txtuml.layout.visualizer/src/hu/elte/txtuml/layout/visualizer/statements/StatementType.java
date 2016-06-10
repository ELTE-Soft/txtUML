package hu.elte.txtuml.layout.visualizer.statements;

import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.model.Direction;

/***
 * Enumeration of the Types of Statements.
 */
public enum StatementType
{
	/**
	 * No knowledge of this type.
	 */
	unknown,
	/**
	 * Corridor size.
	 */
	corridorsize,
	/**
	 * Overlap arrange mode.
	 */
	overlaparrange,
	/**
	 * North
	 */
	north,
	/**
	 * South
	 */
	south,
	/**
	 * East
	 */
	east,
	/**
	 * West
	 */
	west,
	/**
	 * Above
	 */
	above,
	/**
	 * Below
	 */
	below,
	/**
	 * Right
	 */
	right,
	/**
	 * left
	 */
	left,
	/**
	 * Priority
	 */
	priority,
	/**
	 * Phantom
	 */
	phantom,
	/**
	 * Vertical
	 */
	vertical,
	/**
	 * Horizontal
	 */
	horizontal;
	
	/**
	 * Returns whether this {@link StatementType} is applicable on Objects/Boxes.
	 * 
	 * @return whether this {@link StatementType} is applicable on Objects/Boxes.
	 */
	public boolean isOnObjects()
	{
		switch (this)
		{
			case above:
			case below:
			case right:
			case left:
			case north:
			case south:
			case east:
			case west:
				return true;
			case horizontal:
			case phantom:
			case priority:
			case unknown:
			case vertical:
			case corridorsize:
			case overlaparrange:
			default:
				return false;
		}
	}
	
	/**
	 * Returns whether this {@link StatementType} is applicable on Links.
	 * @return whether this {@link StatementType} is applicable on Links.
	 */
	public boolean isAssocType() {
		switch (this) {
		case north:
		case south:
		case east:
		case west:
		case priority:
			return true;
		case above:
		case below:
		case horizontal:
		case left:
		case phantom:
		case right:
		case unknown:
		case vertical:
		case corridorsize:
		case overlaparrange:
		default:
			break;
		}

		return false;
	}

	/**
	 * Method to convert {@link StatementType} to {@link Direction}.
	 * 
	 * @param ty
	 *            StatementType to convert.
	 * @return The converted Direction if possible.
	 * @throws ConversionException
	 *             Throws if the given StatementType is cannot be converted to
	 *             any Direction.
	 */
	public Direction asDirection() throws ConversionException
	{
		switch (this)
		{
			case north:
			case above:
				return Direction.north;
			case south:
			case below:
				return Direction.south;
			case east:
			case right:
				return Direction.east;
			case west:
			case left:
				return Direction.west;
			case horizontal:
			case phantom:
			case priority:
			case unknown:
			case vertical:
			case corridorsize:
			case overlaparrange:
			default:
				throw new ConversionException("Cannot convert type " + this.toString()
						+ " to Direction!");
		}
	}
}
