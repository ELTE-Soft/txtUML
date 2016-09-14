package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.seqdiag.BaseLifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.Runtime;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public class LifelineWrapper<T extends ModelClass> extends AbstractWrapper<T> implements BaseLifelineWrapper<T> {

	InteractionWrapper parent;
	int position;
	String fieldName;

	public LifelineWrapper(InteractionWrapper parent, T instance, int position, String fieldName) {
		super(instance);
		this.position = position;
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	@Override
	public String getStringRepresentation() {
		return "Lifeline of:" + this.getWrapped().toString();
	}

	@Override
	public Runtime getRuntime() {
		return RuntimeContext.getCurrentExecutorThread().getRuntime();
	}
}
