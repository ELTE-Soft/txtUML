package airlock.model;

import hu.elte.txtuml.api.model.Signal;

public class OutputSignal extends Signal{
	public OutputSignal(double pressure){
		this.pressure = pressure;
	}
	
	public double pressure;
}
