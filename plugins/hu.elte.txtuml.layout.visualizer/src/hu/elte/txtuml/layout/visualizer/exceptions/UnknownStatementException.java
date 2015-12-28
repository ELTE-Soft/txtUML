package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Exception indicating that a statement which is unknown to the algorithm
 * reached a certain point where it couldn't be processed.
 * 
 * @author Balázs Gregorics
 *
 */
public class UnknownStatementException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Unknown statement string.
	 */
	public String Statement;
	
	/**
	 * Create UnknownStatementException.
	 */
	public UnknownStatementException()
	{
		super();
	}
	
	/**
	 * Create UnknownStatementException.
	 * 
	 * @param m
	 *            Message of the exception.
	 * @param str
	 *            The string of the unknown statement.
	 */
	public UnknownStatementException(String m, String str)
	{
		super(m);
		Statement = str;
	}
}
