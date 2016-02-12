package hu.elte.txtuml.examples.microwave.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class SetIntensity extends Signal {
	public int value;

	public SetIntensity(int value) {
		this.value = value;
	}
}