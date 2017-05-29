package hu.elte.txtuml.export.diagrams.common.layout;

/**
 * Exception for Layout export errors
 */
public class LayoutExportException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The Constructor
	 * @param message
	 */
	public LayoutExportException(String message) {
		super(message);
	}
	
	/**
	 * The Constructor
	 * @param exception
	 */
	public LayoutExportException(Exception exception){
		super(exception.getMessage());
	}

}
