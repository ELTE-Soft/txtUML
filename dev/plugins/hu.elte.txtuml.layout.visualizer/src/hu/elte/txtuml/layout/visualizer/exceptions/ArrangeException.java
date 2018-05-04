package hu.elte.txtuml.layout.visualizer.exceptions;

/**
 * An exception for any arrangement error
 */
public class ArrangeException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * The Constructor
	 * @param message
	 */
	public ArrangeException(String message) {
		super(message);
	}
	
	/**
	 * The Constructor
	 * @param exception
	 */
	public ArrangeException(Exception exception){
		super(exception.getMessage());
	}
}
