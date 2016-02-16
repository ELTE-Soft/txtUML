package hu.elte.txtuml.layout.visualizer.exceptions;

/***
 * Exception indicating that the algorithm couldn't find a route for a specific
 * link during it's run. This also means that the algorithm couldn't find any
 * ways to solve this problem. Probably the diagram is unsolvable.
 */
public class CannotFindAssociationRouteException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create CannotFindAssociationRouteException.
	 */
	public CannotFindAssociationRouteException()
	{
		super();
	}
	
	/**
	 * Create CannotFindAssociationRouteException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public CannotFindAssociationRouteException(String m)
	{
		super(m);
	}
}
