package hu.elte.txtuml.export.javascript;

/**
 * 
 * Exception which occurs if a diagram of an unexpected type is attempted to be
 * exported.
 * 
 */
public class UnexpectedDiagramTypeException extends Exception {

	private static final long serialVersionUID = 5891525493494714121L;

	/**
	 * Constructor
	 * 
	 * @param diagramName
	 *            the name of the diagram
	 * @param diagramType
	 *            the type of the diagram
	 */
	public UnexpectedDiagramTypeException(String diagramName, String diagramType) {
		super("Could not export diagram: \"" + diagramName + "\" " + ". Unexpected diagram type: \""
				+ diagramType.toString() + "\"");
	}
}
