package hu.elte.txtuml.api.model.execution.impl.singlethread;

import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractPortRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractSignalTargetRuntime;

/**
 * A {@link hu.elte.txtuml.api.model.impl.PortRuntime} implementation for model
 * executors that use only one model executor thread. This may not be used in a
 * multi-thread executor as it lacks the necessary synchronizations.
 */
public class SingleThreadPortRuntime extends AbstractPortRuntime {

	private AbstractSignalTargetRuntime<?> inner = null;
	private AbstractSignalTargetRuntime<?> outer = null;

	public SingleThreadPortRuntime(Port<?, ?> wrapped, AbstractModelClassRuntime owner) {
		super(wrapped, owner);
	}

	@Override
	public AbstractSignalTargetRuntime<?> getOuterConnection() {
		return outer;
	}

	@Override
	public AbstractSignalTargetRuntime<?> getInnerConnection() {
		return inner;
	}

	@Override
	public void setOuterConnection(AbstractSignalTargetRuntime<?> other) {
		outer = other;
	}

	@Override
	public void setInnerConnection(AbstractSignalTargetRuntime<?> other) {
		inner = other;
	}

	@Override
	public void receive(Signal signal, AbstractPortRuntime sender) {
		if (sender == outer) {
			tryToSend(signal, inner);
		} else {
			tryToSend(signal, outer);
		}
	}

}
