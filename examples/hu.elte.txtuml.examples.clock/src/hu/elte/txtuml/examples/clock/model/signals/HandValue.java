package hu.elte.txtuml.examples.clock.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class HandValue extends Signal {
	public HandValue(int value) {
		this.value = value;
	}
	
	public int value;
}
