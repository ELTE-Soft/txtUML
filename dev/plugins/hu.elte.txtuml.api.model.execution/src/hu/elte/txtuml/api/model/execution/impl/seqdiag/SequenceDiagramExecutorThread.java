package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.seqdiag.Runtime;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.DefaultRuntime;

public class SequenceDiagramExecutorThread extends Thread implements RuntimeContext {

	private SequenceDiagramExecutor executor;
	private Runtime runtime;

	public SequenceDiagramExecutorThread(SequenceDiagramExecutor executor) {
		super(executor);
		this.executor = executor;
		this.setName("SequenceDiagram-Thread-" + this.getId());

		runtime = new DefaultRuntime(executor);
	}

	public SequenceDiagramExecutor getExecutor() {
		return this.executor;
	}

	public SequenceDiagramExecutorThread getThread() {
		return this;
	}

	@Override
	public void run() {
		this.executor.execute();
	}

	@Override
	public Runtime getRuntime() {
		return this.runtime;
	}
}
