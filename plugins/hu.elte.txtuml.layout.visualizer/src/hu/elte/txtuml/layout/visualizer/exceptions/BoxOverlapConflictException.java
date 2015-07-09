package hu.elte.txtuml.layout.visualizer.exceptions;

import java.util.ArrayList;
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
	public ArrayList<String> OverlappingBoxes;
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message to show.
	 */
	public BoxOverlapConflictException(String m)
	{
		super(m);
		OverlappingBoxes = null;
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 */
	public BoxOverlapConflictException(List<String> s)
	{
		super();
		OverlappingBoxes = (ArrayList<String>) s;
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param s
	 *            Statement that caused conflict.
	 * @param m
	 *            Message to show.
	 */
	public BoxOverlapConflictException(List<String> s, String m)
	{
		super(m);
		OverlappingBoxes = (ArrayList<String>) s;
	}
}
