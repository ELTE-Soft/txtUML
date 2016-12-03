package hu.elte.txtuml.export.javascript.json.model.cd;

public class UnexpectedEndException extends Exception {
	public UnexpectedEndException(String endName) {
		super("Unexpected AttributeLink end: " + endName);
	}
}
