package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelClassRuntime;

public class SeqDiagModelClassRuntime extends SingleThreadModelClassRuntime {

	public SeqDiagModelClassRuntime(ModelClass wrapped, AbstractExecutorThread thread) {
		super(wrapped, thread);
	}

	public Class<?> getCurrentState() {
		return getCurrentVertexWrapper().getWrapped().getClass();
	}

}
