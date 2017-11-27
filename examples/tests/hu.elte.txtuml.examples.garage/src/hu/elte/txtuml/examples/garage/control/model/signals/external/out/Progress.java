package hu.elte.txtuml.examples.garage.control.model.signals.external.out;

import hu.elte.txtuml.api.stdlib.world.SignalToWorld;

public class Progress extends SignalToWorld {
	public final int percent;

	public Progress(int percent) {
		this.percent = percent;
	}

}
