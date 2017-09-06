package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.model.Signal;

public class RequestSignal extends Signal {

	public int amount;

	public RequestSignal(int amount) {
		this.amount = amount;
	}
	
}
