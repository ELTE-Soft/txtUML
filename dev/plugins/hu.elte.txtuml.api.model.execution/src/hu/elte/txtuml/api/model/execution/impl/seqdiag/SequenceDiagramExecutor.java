package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.ArrayList;

import hu.elte.txtuml.api.model.error.seqdiag.InvalidMessageError;
import hu.elte.txtuml.api.model.error.seqdiag.ValidationError;
import hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public class SequenceDiagramExecutor implements Runnable {

	protected InvalidMessageSentListener messageListener;
	protected CommunicationListener traceListener;
	protected DefaultModelExecutor executor;
	
	private Boolean isLocked;
	private SequenceDiagramExecutorThread thread;
	private Interaction base;
	private ArrayList<ValidationError> errors;
	
	public SequenceDiagramExecutor() {
		isLocked = false;
		this.messageListener = new InvalidMessageSentListener(this);
		this.traceListener = new CommunicationListener(this);
		this.executor = new DefaultModelExecutor();
		executor.addWarningListener(messageListener);
		executor.addTraceListener(traceListener);
		
		this.errors = new ArrayList<ValidationError>();
		
		this.thread = new SequenceDiagramExecutorThread(this);
	}
	
	public void setInteraction(Interaction interaction) throws Exception
	{
		if(isLocked)
			throw new Exception("Invalid method call! Executor is currently executing an interaction");
		this.base = interaction;
	}
	
	public SequenceDiagramExecutor start()
	{
		thread.start();
		return this;
	}
	
	public void run()
	{
		this.start().shutdown().awaitTermination();
	}
	
	public void execute()
	{
		isLocked = true;
		
		InteractionWrapper interaction = ( (RuntimeContext)Thread.currentThread() ).getRuntime().getInteractionWrapper(base);
		this.thread.getRuntime().setCurrentInteraction(interaction);
		
		executor.setInitialization(new Runnable(){
			public void run()
			{
				interaction.getWrapped().initialize();
				interaction.prepare();
				interaction.getWrapped().run();
			}
		});
		
		executor.launch();
		executor.shutdown();
		executor.awaitTermination();
		
		if(traceListener.suggestedMessagePattern.size() != 0)
		{
			this.errors.add(new InvalidMessageError(this.thread.getInteractionWrapper().getLifelines().get(0).getWrapped(),"The pattern given is bigger than the model"));
		}
		
		PlantUmlGenerator generator = new PlantUmlGenerator("plantUml.txt", interaction);
		generator.generate();
	}
	
	public SequenceDiagramExecutor shutdown()
	{

		isLocked = false;
		return this;
	}
	
	public void awaitTermination()
	{				
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
