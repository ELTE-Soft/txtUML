package pingpong.j.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class Ball extends Signal {
	public int countdown;

	public Ball(int countdown) {
		this.countdown = countdown;
	}
}
