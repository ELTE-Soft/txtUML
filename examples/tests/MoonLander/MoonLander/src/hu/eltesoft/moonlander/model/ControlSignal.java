package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Signal;

public class ControlSignal extends Signal {
	
	public ControlSignal(double u) {
		this.u = u;
	}

	public double u;
}
