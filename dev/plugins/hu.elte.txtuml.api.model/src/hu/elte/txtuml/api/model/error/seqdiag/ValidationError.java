package hu.elte.txtuml.api.model.error.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;

public interface ValidationError {
	public ModelClass sourceClass();
	public String errorMsg();
}
