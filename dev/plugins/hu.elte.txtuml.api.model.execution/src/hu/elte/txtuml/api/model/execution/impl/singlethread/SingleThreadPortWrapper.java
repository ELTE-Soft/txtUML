package hu.elte.txtuml.api.model.execution.impl.singlethread;

import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassWrapper;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractPortWrapper;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractSignalTargetWrapper;

/**
 * A {@link hu.elte.txtuml.api.model.runtime.PortWrapper} implementation for
 * model executors that use only one model executor thread. This may not be used
 * in a multi-thread executor as it lacks the necessary synchronizations.
 */
public class SingleThreadPortWrapper extends AbstractPortWrapper {

	private AbstractSignalTargetWrapper<?> inner = null;
	private AbstractSignalTargetWrapper<?> outer = null;

	public SingleThreadPortWrapper(Port<?, ?> wrapped, AbstractModelClassWrapper owner) {
		super(wrapped, owner);
	}

	@Override
	public AbstractSignalTargetWrapper<?> getOuterConnection() {
		return outer;
	}

	@Override
	public AbstractSignalTargetWrapper<?> getInnerConnection() {
		return inner;
	}

	@Override
	public void setOuterConnection(AbstractSignalTargetWrapper<?> other) {
		outer = other;
	}

	@Override
	public void setInnerConnection(AbstractSignalTargetWrapper<?> other) {
		inner = other;
	}

	@Override
	public void receive(Signal signal, Boolean isApi) {
		tryToSend(signal, outer);
	}

	@Override
	public void receive(Signal signal, AbstractPortWrapper sender) {
		if (sender == outer) {
			tryToSend(signal, inner);
		} else {
			tryToSend(signal, outer);
		}
	}

}
