package clock.j.model.interfaces;

import clock.j.model.signals.Tick;
import hu.elte.txtuml.api.model.Interface;

public interface TickIfc extends Interface {
	public void reception(Tick signal);
}
