package hu.elte.txtuml.api.model.execution.impl;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.FIFOExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelRuntime;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadPortRuntime;

/**
 * The runtime of the default model executor engine.
 */
public final class DefaultModelRuntime extends SingleThreadModelRuntime<SingleThreadModelClassRuntime, SingleThreadPortRuntime> {

	private final FIFOExecutorThread thread;

	protected DefaultModelRuntime(AbstractModelExecutor<?> executor, Runnable initialization) {
		super(executor);

		this.thread = new FIFOExecutorThread(getExecutor(), this, initialization);
	}

	@Override
	public SingleThreadModelClassRuntime createModelClassRuntime(ModelClass object) {
		return new SingleThreadModelClassRuntime(object, getThread());
	}

	@Override
	public SingleThreadPortRuntime createPortRuntime(Port<?, ?> portInstance, ModelClass owner) {
		return new SingleThreadPortRuntime(portInstance, getRuntimeOf(owner));
	}

	@Override
	public void start() {
		thread.start();
	}

	@Override
	public AbstractExecutorThread getThread() {
		return thread;
	}

}
