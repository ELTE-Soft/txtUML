package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Exception indicating that somewhere in the algorithm there was either an
 * illegal conversion between types or a conversion which is not defined.
 */
public class ConversionException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create ConversionException.
	 */
	public ConversionException()
	{
		super();
	}
	
	/**
	 * Create ConversionException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public ConversionException(String m)
	{
		super(m);
	}
}
