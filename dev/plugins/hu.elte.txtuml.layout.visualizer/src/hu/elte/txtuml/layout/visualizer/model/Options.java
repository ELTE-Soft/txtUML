package hu.elte.txtuml.layout.visualizer.model;

/**
 * Class that encapsulates the multiple options of the algorithm.
 */
public class Options {
	/**
	 * Whether to print log messages.
	 */
	public Boolean Logging;
	/**
	 * Whether to arrange overlapping elements or not.
	 */
	public OverlapArrangeMode ArrangeOverlaps;
	/**
	 * The width of the corridors between boxes relative to the boxes' side.
	 */
	public Double CorridorRatio;
	/**
	 * The percentage of grid points around the corner that are considered
	 * corner points.
	 */
	public Double CornerPercentage;
}
