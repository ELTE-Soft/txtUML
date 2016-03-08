package clock.j.model.interfaces;

import clock.j.model.signals.HandValue;
import hu.elte.txtuml.api.model.Interface;

public interface ValueIfc extends Interface {
	public void reception(HandValue signal);
}
