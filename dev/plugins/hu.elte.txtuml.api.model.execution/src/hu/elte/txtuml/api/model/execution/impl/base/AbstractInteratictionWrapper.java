package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.List;
import java.util.Map;

import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.error.seqdiag.ValidationError;
import hu.elte.txtuml.api.model.runtime.InteractionWrapper;
import hu.elte.txtuml.api.model.runtime.LifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.Interaction;

public abstract class AbstractInteratictionWrapper extends BaseWrapper<Interaction> implements InteractionWrapper {

	public AbstractInteratictionWrapper(Interaction self) {
		super(self);
	}
	@Override
	public String getStringRepresentation() {
		return "Interaction:";
	}

	@Override
	public Runtime getRuntime() {
		// TODO Auto-generated method stub
		return Runtime.currentRuntime();
	}

}
