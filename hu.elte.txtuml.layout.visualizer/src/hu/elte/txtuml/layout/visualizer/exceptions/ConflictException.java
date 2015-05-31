package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Exception indicating that conflicts were detected as a result of the user
 * statements.
 * 
 * @author Balázs Gregorics
 */
public class ConflictException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create ConflictException.
	 */
	public ConflictException()
	{
		super();
	}
	
	/**
	 * Create ConflictException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public ConflictException(String m)
	{
		super(m);
	}
}
