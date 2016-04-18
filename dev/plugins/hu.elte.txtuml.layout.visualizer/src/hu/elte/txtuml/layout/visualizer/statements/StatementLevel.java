package hu.elte.txtuml.layout.visualizer.statements;

/**
 * Enumeration that represents the level of a statement.
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
	 * Medium level statement.
	 */
	Medium,
	/**
	 * High level statement. Delete first.
	 */
	High
}
