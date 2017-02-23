package hu.elte.txtuml.export.cpp.statemachine;

public class TransitionConditions {
	
	public TransitionConditions (String event, String state, String port) {
		this.event = event;
		this.state = state;
		this.port = port;
	}
	
	public String getEvent() {
		return event;
	}

	public String getState() {
		return state;
	}



	public String getPort() {
		return port;
	}

	private String event;
	private String state;
	private String port;
}
