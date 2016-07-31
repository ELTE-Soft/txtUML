package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Signal;

public class ControlCycleSignal extends Signal {
	
	public ControlCycleSignal(double h, double v) {
		this.h = h;
		this.v = v;
	}
	
	public double h;
	public double v;	

}
