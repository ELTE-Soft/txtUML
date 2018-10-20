package control.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class AbortSignal extends Signal{
	public String message;
	
	AbortSignal(String message){
		this.message = message;
	}
}
