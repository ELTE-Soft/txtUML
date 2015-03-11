package hu.elte.txtuml.layout.visualizer.exceptions;

public class ConflictException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	public ConflictException()
	{
		super();
	}
	
	public ConflictException(String m)
	{
		super(m);
	}
}
