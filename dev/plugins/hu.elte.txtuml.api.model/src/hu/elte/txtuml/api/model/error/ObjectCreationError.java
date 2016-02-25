package hu.elte.txtuml.api.model.error;

@SuppressWarnings("serial")
public class ObjectCreationError extends Error {

	public ObjectCreationError() {
		super("The creation of a model object was impossible with the given arguments.");
	}

}
