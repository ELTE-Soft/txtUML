package hu.elte.txtuml.api.model.execution.impl.seqdiag;

public class SequenceDiagramExecutorThread extends Thread {

	private SequenceDiagramExecutor executor;
	
	public SequenceDiagramExecutorThread(SequenceDiagramExecutor executor)
	{
		super(executor);
		this.executor = executor;
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
}
