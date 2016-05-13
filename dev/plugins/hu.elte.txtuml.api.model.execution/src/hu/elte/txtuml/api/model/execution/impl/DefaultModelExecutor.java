package hu.elte.txtuml.api.model.execution.impl;

import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.SwitchOnLogging;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelExecutor;

/**
 * The default model executor engine which uses a single FIFO model executor
 * thread. That means, it executes the model sequentially, processing events
 * (like sending signals) in the order in which they were raised. It supports
 * all optional methods of {@link ModelExecutor}.
 */
public final class DefaultModelExecutor extends SingleThreadModelExecutor<DefaultModelExecutor> {

	public DefaultModelExecutor() {
		this("");
	}

	public DefaultModelExecutor(String name) {
		super(name, SwitchOnLogging.DEFAULT_LOGGING_AND_DIAGNOSTICS_SERVICE);
	}

	@Override
	protected AbstractRuntime<?, ?> createRuntime(Runnable initialization) {
		return new DefaultRuntime(this, initialization);
	}

	@Override
	public DefaultModelExecutor self() {
		return this;
	}

}
