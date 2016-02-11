package hu.elte.txtuml.examples.clock.model.interfaces;

import hu.elte.txtuml.examples.clock.model.signals.Tick;
import hu.elte.txtuml.api.model.Interface;

public interface TickIfc extends Interface {
	public void reception(Tick signal);
}
