package hu.elte.txtuml.layout.visualizer.exceptions;

public class CannotFindAssociationRouteException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotFindAssociationRouteException()
	{
		super();
	}
	
	public CannotFindAssociationRouteException(String m)
	{
		super(m);
	}
}
