package hu.elte.txtuml.api.model.error.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;

public class PendingReceiveError implements ValidationError {
	protected ModelClass source;
	protected String message;
	
	public PendingReceiveError(ModelClass source,String message) {
		this.source = source;
		this.message = message;
	}
	
	@Override
	public ModelClass sourceClass() {
		return this.source;
	}
	@Override
	public String errorMsg() {
		return this.message;
	}
}
