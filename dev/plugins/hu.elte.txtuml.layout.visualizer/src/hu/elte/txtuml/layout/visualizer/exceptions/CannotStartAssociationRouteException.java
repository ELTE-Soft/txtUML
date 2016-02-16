package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * Exception indicating that the algorithm couldn't start to find the route for
 * a specific link. This means that all of the object's edge points are
 * occupied.
 */
public class CannotStartAssociationRouteException extends MyException
{
	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create CannotStartAssociationRouteException.
	 */
	public CannotStartAssociationRouteException()
	{
		super();
	}
	
	/**
	 * Create CannotStartAssociationRouteException.
	 * 
	 * @param m
	 *            Message of the exception.
	 */
	public CannotStartAssociationRouteException(String m)
	{
		super(m);
	}
}
