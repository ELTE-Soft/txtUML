package hu.elte.txtuml.layout.visualizer.exceptions;

public class CannotPositionObjectException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotPositionObjectException()
	{
		super();
	}
	
	public CannotPositionObjectException(String m)
	{
		super(m);
	}
}
