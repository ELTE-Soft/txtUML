package hu.elte.txtuml.api.model.execution.impl;

import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.SwitchOnLogging;

/**
 * The default model executor engine which uses a single FIFO model executor
 * thread. That means, it executes the model sequentially, processing events
 * (like sending signals) in the order in which they were raised. It supports
 * all optional methods of {@link ModelExecutor}.
 */
public final class DefaultModelExecutor extends AbstractModelExecutor<DefaultModelExecutor> {

	public DefaultModelExecutor() {
		this("");
	}

	public DefaultModelExecutor(String name) {
		super(name, SwitchOnLogging.DEFAULT_LOGGING_AND_DIAGNOSTICS_SERVICE);
	}

	@Override
	protected AbstractModelRuntime<?, ?> createRuntime(Runnable initialization) {
		return new DefaultModelRuntime(this, initialization);
	}

	@Override
	public DefaultModelExecutor self() {
		return this;
	}

}
