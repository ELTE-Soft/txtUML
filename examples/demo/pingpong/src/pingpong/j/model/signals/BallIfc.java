package pingpong.j.model.signals;

import hu.elte.txtuml.api.model.Interface;

public interface BallIfc extends Interface {
	void reception(Ball b);
}
