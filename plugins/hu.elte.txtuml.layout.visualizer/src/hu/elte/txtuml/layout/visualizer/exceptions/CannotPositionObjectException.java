package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Exception indicationg that the algorithm couldn't find a proper position for
 * a specific object in the diagram. This can be because the statements provided
 * were conflictious.
 * 
 * @author Balázs Gregorics
 */
public class CannotPositionObjectException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create CannotPositionObjectException.
	 */
	public CannotPositionObjectException()
	{
		super();
	}
	
	/**
	 * Create CannotPositionObjectException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public CannotPositionObjectException(String m)
	{
		super(m);
	}
}
