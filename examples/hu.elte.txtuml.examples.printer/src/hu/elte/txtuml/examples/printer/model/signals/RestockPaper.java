package hu.elte.txtuml.examples.printer.model.signals;

import hu.elte.txtuml.api.model.Signal;

public class RestockPaper extends Signal {
	public int amount;

	public RestockPaper(int amount) {
		this.amount = amount;
	}
}