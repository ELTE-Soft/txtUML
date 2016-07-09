package hu.elte.txtuml.api.model.execution.impl.seqdiag;

public class AbstractSequenceDiagramModelListener {
	protected SequenceDiagramExecutor executor;
	
	public AbstractSequenceDiagramModelListener(SequenceDiagramExecutor executor)
	{
		this.executor = executor;
	}
}
