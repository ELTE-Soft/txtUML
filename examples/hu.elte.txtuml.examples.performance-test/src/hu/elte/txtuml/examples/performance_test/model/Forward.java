package hu.elte.txtuml.examples.performance_test.model;

import hu.elte.txtuml.api.model.Signal;

public class Forward extends Signal {
	int nf;

	public Forward(final int nf) {
		this.nf = nf;
	}
}