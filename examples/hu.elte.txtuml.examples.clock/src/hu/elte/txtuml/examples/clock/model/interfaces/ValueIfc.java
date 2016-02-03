package hu.elte.txtuml.examples.clock.model.interfaces;

import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.examples.clock.model.signals.HandValue;

public interface ValueIfc extends Interface {
	public void reception(HandValue signal);
}
