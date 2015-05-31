package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Abstract generic form of the algorithm's exceptions.
 * 
 * @author Balázs Gregorics
 *
 */
public class MyException extends Throwable
{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	private String _message;
	
	/**
	 * Create MyException.
	 */
	public MyException()
	{
		
	}
	
	/**
	 * Create MyException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public MyException(String m)
	{
		_message = m;
	}
	
	public String getMessage()
	{
		return _message;
	}
}
