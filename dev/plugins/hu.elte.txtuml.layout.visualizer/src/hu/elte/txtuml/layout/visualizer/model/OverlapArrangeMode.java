package hu.elte.txtuml.layout.visualizer.model;

/**
 * Mode of the overlap arrangement.
 */
public enum OverlapArrangeMode
{
	/**
	 * No arrangement of overlapping elements.
	 */
	none,
	/**
	 * Try with one fixed arrangement.
	 */
	one,
	/**
	 * Try a few possible arrangement.
	 */
	few,
	/**
	 * Try every arrangement until a certain treshold is reached.
	 */
	limited,
	/**
	 * Try every arrangement and get the solution if there is one.
	 */
	full
}
