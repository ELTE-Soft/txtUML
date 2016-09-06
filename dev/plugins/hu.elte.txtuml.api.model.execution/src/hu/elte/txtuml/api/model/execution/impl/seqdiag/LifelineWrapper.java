package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.AssociationEnd;
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

	public void delete() {
		hu.elte.txtuml.api.model.Action.delete(this.getWrapped());
	}

	public void start() {
		hu.elte.txtuml.api.model.Action.start(this.getWrapped());
	}

	protected <R extends ModelClass> void link(Class<? extends AssociationEnd<T, ?>> leftEnd,
			Class<? extends AssociationEnd<R, ?>> rightEnd, LifelineWrapper<R> rightObj) {
		hu.elte.txtuml.api.model.Action.link(leftEnd, this.getWrapped(), rightEnd, rightObj.getWrapped());
	}

	protected <R extends ModelClass> void unlink(Class<? extends AssociationEnd<T, ?>> leftEnd,
			Class<? extends AssociationEnd<R, ?>> rightEnd, LifelineWrapper<R> rightObj) {
		hu.elte.txtuml.api.model.Action.unlink(leftEnd, this.getWrapped(), rightEnd, rightObj.getWrapped());
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
