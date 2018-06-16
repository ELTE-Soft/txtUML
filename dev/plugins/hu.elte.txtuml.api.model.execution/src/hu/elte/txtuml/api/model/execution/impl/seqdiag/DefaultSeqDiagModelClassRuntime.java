package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelClassRuntime;

/**
 * Provides additional runtime information for a model class instance.
 * <p>
 * Note that this class is a subclass of {@link SingleThreadModelClassRuntime},
 * so it may not be used in a multi-threaded executor as it lacks the necessary
 * synchronizations.
 */
class DefaultSeqDiagModelClassRuntime extends SingleThreadModelClassRuntime {

	public DefaultSeqDiagModelClassRuntime(ModelClass wrapped, AbstractExecutorThread thread) {
		super(wrapped, thread);
	}

	/**
	 * Returns the current state of the wrapped model class instance.
	 */
	public Class<?> getCurrentState() {
		return getCurrentVertexWrapper().getWrapped().getClass();
	}

}
