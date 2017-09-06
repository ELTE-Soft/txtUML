package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.model.Signal;

public class ResponseSignal extends Signal {

	public double data;

	public ResponseSignal(double data) {
		this.data = data;
	}
	
}
