package hu.elte.txtuml.api.model.error.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;

public class InvalidMessageError implements ValidationError {

	protected ModelClass source;
	protected String message;
	
	public InvalidMessageError(ModelClass source,String message) {
		this.source = source;
		this.message = message;
	}
	
	@Override
	public ModelClass sourceClass() {
		return source;
	}

	@Override
	public String errorMsg() {
		return message;
	}
	
	public String toString()
	{
		return source.getClass().getSimpleName() + ":" + message;
	}

}
