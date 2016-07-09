package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.ArrayList;

import hu.elte.txtuml.api.model.ModelClass;

import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.seqdiag.InvalidMessageError;
import hu.elte.txtuml.api.model.execution.WarningListener;

public class InvalidMessageSentListener extends AbstractSequenceDiagramModelListener implements WarningListener {

	public InvalidMessageSentListener(SequenceDiagramExecutor executor)
	{
		super(executor);
	}
	
	@Override
	public void lostSignalAtObject(Signal signal, ModelClass obj) {		
		executor.addError(new InvalidMessageError(obj, "Error: Invalid message sent!"));
	}
	
	@Override
	public void signalArrivedToDeletedObject(ModelClass obj, Signal signal)
	{
		executor.addError(new InvalidMessageError(obj, "Error: Invalid message sent!"));
	}
	
	@Override
	public void lostSignalAtPort(Signal signal, Port<?, ?> portInstance) {
		//portError
	}
}
