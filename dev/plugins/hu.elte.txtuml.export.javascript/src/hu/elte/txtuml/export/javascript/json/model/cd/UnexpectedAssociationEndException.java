package hu.elte.txtuml.export.javascript.json.model.cd;

public class UnexpectedAssociationEndException extends Exception {
	
	private static final long serialVersionUID = -101420531564556373L;

	public UnexpectedAssociationEndException(String from, String to, String actual) {
	      super("Unexpected association end: "+ actual + " Expected either: " + from +" or " + to);
	}
	
}
