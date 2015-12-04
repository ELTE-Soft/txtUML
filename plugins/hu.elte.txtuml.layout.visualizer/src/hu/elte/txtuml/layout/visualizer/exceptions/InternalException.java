package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * This is a generic exception indicating that something in the algorithm went
 * wrong. Something happened which should not be allowed or the programmer did
 * not prepared for it.
 * 
 * @author Balázs Gregorics
 */
public class InternalException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create InternalException.
	 */
	public InternalException()
	{
		super();
	}
	
	/**
	 * Create InternalException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public InternalException(String m)
	{
		super(m);
	}
}
