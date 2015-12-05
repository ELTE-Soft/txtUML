package hu.elte.txtuml.api.model.models.erronous.notransitionfromchoice;

import hu.elte.txtuml.api.model.Signal;

public class Sig extends Signal {
	int value;

	public Sig() {
		this(0);
	}

	public Sig(int value) {
		this.value = value;
	}
}