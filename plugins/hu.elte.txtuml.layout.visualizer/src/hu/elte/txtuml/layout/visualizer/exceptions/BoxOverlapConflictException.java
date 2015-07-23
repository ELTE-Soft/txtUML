package hu.elte.txtuml.layout.visualizer.exceptions;

import java.util.List;

/**
 * Exception indicating that the algorithm encountered an unsolvable conflict
 * during the arrangement of overlapping boxes.
 * 
 * @author Balázs Gregorics
 */
public class BoxOverlapConflictException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Gets or sets the Statement that caused conflict.
	 */
	public List<String> OverlappingBoxes;
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 */
	public BoxOverlapConflictException(List<String> s)
	{
		super("The layout of the following overlapping boxes could not be resolved!");
		OverlappingBoxes = s;
	}
	
}
