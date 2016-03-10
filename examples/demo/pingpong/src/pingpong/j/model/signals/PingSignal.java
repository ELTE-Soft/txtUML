package pingpong.j.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class PingSignal extends Signal {
	public int count;

	public PingSignal(int count) {
		this.count = count;
	}
}
