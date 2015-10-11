package hu.elte.txtuml.layout.visualizer.helpers;

import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.OverlapArrangeMode;

/**
 * Class that encapsulates the multiple options of the algorithm.
 * 
 * @author Balázs Gregorics
 *
 */
public class Options
{
	/**
	 * The type of the diagram to arrange.
	 */
	public DiagramType DiagramType;
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
}
