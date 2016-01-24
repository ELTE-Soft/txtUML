package hu.elte.txtuml.examples.pingpong.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class PongSignal extends Signal {
	public int count;

	public PongSignal(int count) {
		this.count = count;
	}
}
