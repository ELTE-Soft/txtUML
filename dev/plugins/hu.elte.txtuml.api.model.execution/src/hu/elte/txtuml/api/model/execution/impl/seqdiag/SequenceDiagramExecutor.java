package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.ArrayList;

import hu.elte.txtuml.api.model.error.seqdiag.ValidationError;
import hu.elte.txtuml.api.model.execution.ModelExecutor.Status;
import hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.SwitchOnLogging;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelExecutor;
import hu.elte.txtuml.api.model.seqdiag.Interaction;

public class SequenceDiagramExecutor implements Runnable {
	
	protected Interaction interaction;
	protected InvalidMessageSentListener messageListener;
	protected CommunicationListener traceListener;
	protected DefaultModelExecutor executor;
	
	private SequenceDiagramExecutorThread thread;
	
	private ArrayList<ValidationError> errors;
	
	public SequenceDiagramExecutor() {
		this.messageListener = new InvalidMessageSentListener(this);
		this.traceListener = new CommunicationListener(this);
		this.executor = new DefaultModelExecutor();
		executor.addWarningListener(messageListener);
		executor.addTraceListener(traceListener);
		
		this.errors = new ArrayList<ValidationError>();
		
		this.thread = new SequenceDiagramExecutorThread(this);
	}
	
	public void setInteraction(Interaction interaction)
	{
		this.interaction = interaction;
	}
	
	public void run()
	{
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void execute()
	{
		executor.setInitialization(new Runnable(){
			public void run()
			{
				interaction.initialize();
				interaction.run();
			}
		});
		
		executor.run();
	}
	
	public ArrayList<ValidationError> getErrors()
	{
		return this.errors;
	}
	
	public void addError(ValidationError error)
	{
		this.errors.add(error);
	}
	
	public SequenceDiagramExecutor self()
	{
		return this;
	}
	
	public SequenceDiagramExecutorThread getThread()
	{
		return this.thread;
	}
}
