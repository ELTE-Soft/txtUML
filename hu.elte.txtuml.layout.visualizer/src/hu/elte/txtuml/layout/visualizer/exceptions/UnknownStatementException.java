package hu.elte.txtuml.layout.visualizer.exceptions;

public class UnknownStatementException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	public UnknownStatementException()
	{
		super();
	}
	
	public UnknownStatementException(String m)
	{
		super(m);
	}
}
