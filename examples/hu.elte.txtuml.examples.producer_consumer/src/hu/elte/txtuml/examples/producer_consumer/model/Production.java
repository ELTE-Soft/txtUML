package hu.elte.txtuml.examples.producer_consumer.model;

import hu.elte.txtuml.api.model.Association;

public class Production extends Association {

	public class producer extends Association.Many<Producer> {
	}

	public class storage extends Association.One<Storage> {
	}
}
