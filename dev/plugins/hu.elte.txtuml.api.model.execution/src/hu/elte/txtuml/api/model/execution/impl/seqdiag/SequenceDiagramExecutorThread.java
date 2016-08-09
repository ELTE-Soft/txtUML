package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.seqdiag.ImprintedListener;
import hu.elte.txtuml.api.model.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.Runtime;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public class SequenceDiagramExecutorThread extends Thread implements RuntimeContext {

	private SequenceDiagramExecutor executor;
	private Runtime runtime;
	
	static SequenceDiagramExecutorThread getCurrentExecutorThread()
	{
		Thread[] threads = new Thread[Thread.activeCount()];
		for(Thread thread : threads)
		{
			if(thread instanceof SequenceDiagramExecutorThread)
			{
				return (SequenceDiagramExecutorThread) thread;
			}
		}
		
		return null;
	}
	
	public SequenceDiagramExecutorThread(SequenceDiagramExecutor executor)
	{
		super(executor);
		this.executor = executor;
		this.setName("SequenceDiagram-Thread-" + this.getId());
		
		runtime = new Runtime();
	}
	
	public SequenceDiagramExecutor getExecutor()
	{
		return this.executor;
	}
	
	public SequenceDiagramExecutorThread getThread()
	{
		return this;
	}
	
	@Override
	public void run()
	{
		this.executor.execute();
	}

	@Override
	public Runtime getRuntime() {
		return this.runtime;
	}

	@Override
	public InteractionWrapper getInteractionWrapper() {
		return this.runtime.getCurrentInteraction();
	}

	@Override
	public ImprintedListener getTraceListener() {
		return executor.traceListener;
	}	
}
