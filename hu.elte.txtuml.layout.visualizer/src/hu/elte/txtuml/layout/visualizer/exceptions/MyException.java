package hu.elte.txtuml.layout.visualizer.exceptions;

public class MyException extends Throwable
{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	private String _message;
	
	public MyException()
	{
		
	}
	
	public MyException(String m)
	{
		_message = m;
	}
	
	public String getMessage()
	{
		return _message;
	}
}
