package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public interface ImprintedListener {

	public void addToPattern(ModelClass from,Signal sig,ModelClass to);
	
}
