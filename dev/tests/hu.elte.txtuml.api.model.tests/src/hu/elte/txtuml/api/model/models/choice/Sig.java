package hu.elte.txtuml.api.model.models.choice;

import hu.elte.txtuml.api.model.Signal;

public class Sig extends Signal {
	public int value;

	public Sig() {
		this(0);
	}

	public Sig(int value) {
		this.value = value;
	}
}