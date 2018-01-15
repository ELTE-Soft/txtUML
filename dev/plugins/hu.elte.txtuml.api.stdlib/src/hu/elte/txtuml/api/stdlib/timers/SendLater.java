package hu.elte.txtuml.api.stdlib.timers;

import java.util.concurrent.Callable;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

@External
public class SendLater implements Callable<Void> {

	private final Signal signal;
	private final ModelClass targetObject;

	public SendLater(Signal signal, ModelClass targetObject) {
		this.signal = signal;
		this.targetObject = targetObject;
	}

	@Override
	public Void call() {
		API.send(signal, targetObject);
		return null;
	}

}
