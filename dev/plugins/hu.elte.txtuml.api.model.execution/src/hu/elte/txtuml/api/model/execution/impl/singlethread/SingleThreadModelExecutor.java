package hu.elte.txtuml.api.model.execution.impl.singlethread;

import java.util.concurrent.atomic.AtomicReference;

import hu.elte.txtuml.api.model.execution.impl.base.SwitchOnLogging;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.ModelExecutorThread;
import hu.elte.txtuml.utils.Logger;

/**
 * Abstract base class for
 * {@link hu.elte.txtuml.api.model.execution.ModelExecutor} implementations
 * which use only one model executor thread.
 */
public abstract class SingleThreadModelExecutor<S extends SingleThreadModelExecutor<S>>
		extends AbstractModelExecutor<S> {

	private final AtomicReference<ModelExecutorThread> thread = new AtomicReference<>();

	public SingleThreadModelExecutor(String name, SwitchOnLogging logging) {
		super(name, logging);
	}

	@Override
	public void awaitTerminationNoCatch() throws InterruptedException {
		ModelExecutorThread t = thread.get();
		if (t != null) {
			t.join();
		}
	}

	@Override
	protected void wakeAllThreads() {
		ModelExecutorThread t = thread.get();
		if (t != null) {
			t.wake();
		}
	}

	@Override
	protected void registerThread(ModelExecutorThread thread) {
		ModelExecutorThread t = this.thread.getAndSet(thread);
		if (t != null) {
			Logger.sys
					.error("More than one model executor thread has been started for a single-thread model executor.");
		}
	}

	@Override
	protected boolean unregisterThread(ModelExecutorThread thread) {
		performTermination();
		return true;
	}

}
