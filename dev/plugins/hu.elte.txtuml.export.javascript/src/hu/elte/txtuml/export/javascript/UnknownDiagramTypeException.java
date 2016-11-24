package hu.elte.txtuml.export.javascript;

public class UnknownDiagramTypeException extends Exception {

	public UnknownDiagramTypeException(String diagramName, String diagramType) {
		super("Could not export diagram: \"" + diagramName + "\" " + ". Unknown diagram type: \""
				+ diagramType.toString() + "\"");
	}
}
