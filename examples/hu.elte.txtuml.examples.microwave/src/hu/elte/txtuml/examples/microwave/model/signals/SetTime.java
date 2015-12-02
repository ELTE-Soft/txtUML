package hu.elte.txtuml.examples.microwave.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class SetTime extends Signal {
	public int value;

	public SetTime(int value) {
		this.value = value;
	}
}