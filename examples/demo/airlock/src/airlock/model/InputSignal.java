package airlock.model;

import hu.elte.txtuml.api.model.Signal;

public class InputSignal extends Signal{
	InputSignal(boolean doCycle){
		this.doCycle = doCycle;
	}
	
	public boolean doCycle;
}
