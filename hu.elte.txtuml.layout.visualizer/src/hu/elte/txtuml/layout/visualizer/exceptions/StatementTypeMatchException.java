package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Exception indicating that a specific statement was not in correct format: not
 * enough parameters, too few parameters or parameters are not correct types.
 * 
 * @author Balázs Gregorics
 *
 */
public class StatementTypeMatchException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create StatementTypeMatchException.
	 */
	public StatementTypeMatchException()
	{
		super();
	}
	
	/**
	 * Create StatementTypeMatchException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public StatementTypeMatchException(String m)
	{
		super(m);
	}
}
