package hu.elte.txtuml.export.uml2.mapping;

public class ModelMapException extends Exception {

	private static final long serialVersionUID = 1L;
	
	ModelMapException(Exception e) {
		super(e);
	}
	
	ModelMapException(String cause) {
		super(cause);
	}
}
