package hu.elte.txtuml.layout.visualizer.annotations;

/**
 * Enumeration that represents the level of a statement.
 * 
 * @author Balázs Gregorics
 *
 */
public enum StatementLevel
{
	/**
	 * User created statement. Not deletable.
	 */
	User,
	/**
	 * Low level statement. Delete last.
	 */
	Low,
	/**
	 * Medium level statement. Delete in midst.
	 */
	Medium,
	/**
	 * High level statement. Delete first.
	 */
	High
}
