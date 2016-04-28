package machine2.j.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class DoTasks extends Signal {
	public int count;

	public DoTasks(int count) {
		this.count = count;
	}
}
