package hu.elte.txtuml.api.model.execution.impl;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.FIFOModelExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.base.ModelExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelClassWrapper;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadPortWrapper;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadRuntime;

/**
 * The runtime of the default model executor engine.
 */
public final class DefaultRuntime extends SingleThreadRuntime<SingleThreadModelClassWrapper, SingleThreadPortWrapper> {

	private final FIFOModelExecutorThread thread;

	protected DefaultRuntime(AbstractModelExecutor<?> executor, Runnable initialization) {
		super(executor);

		this.thread = new FIFOModelExecutorThread(getExecutor(), this, initialization);
	}

	@Override
	protected SingleThreadModelClassWrapper createModelClassWrapper(ModelClass object) {
		return new SingleThreadModelClassWrapper(object, getThread());
	}

	@Override
	protected SingleThreadPortWrapper createPortWrapper(Port<?, ?> portInstance, ModelClass owner) {
		return new SingleThreadPortWrapper(portInstance, getInfo(owner));
	}

	@Override
	public void start() {
		thread.start();
	}

	@Override
	public ModelExecutorThread getThread() {
		return thread;
	}

}
