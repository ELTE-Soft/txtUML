package hu.elte.txtuml.examples.garage.control.model.signals.external;

import hu.elte.txtuml.api.model.Signal;

public class KeyPress extends Signal {
	public int key;

	public KeyPress(int k) {
		this.key = k;
	}
}