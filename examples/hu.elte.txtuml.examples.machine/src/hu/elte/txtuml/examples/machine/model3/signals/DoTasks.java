package hu.elte.txtuml.examples.machine.model3.signals;

import hu.elte.txtuml.api.model.Signal;

public class DoTasks extends Signal {
	public int count;

	public DoTasks(int count) {
		this.count = count;
	}
}
