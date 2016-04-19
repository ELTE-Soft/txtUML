package pingpong.j.model.signals;

import hu.elte.txtuml.api.model.Interface;

public interface HitOrMissIfc extends Interface {
	void reception(HitTheBall s);

	void reception(MissedTheBall s);

}
