package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Signal;

public class OutputSignal extends Signal {
	
	public OutputSignal(double u) {
		this.u = u;
	}

	public double u;
}
