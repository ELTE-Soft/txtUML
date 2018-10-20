package control.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class InputSignal extends Signal{
	public int program;
	public double actualTemperature;
	
	InputSignal(int program, double temperature){
		this.program = program;
		this.actualTemperature = temperature;
	}
}
