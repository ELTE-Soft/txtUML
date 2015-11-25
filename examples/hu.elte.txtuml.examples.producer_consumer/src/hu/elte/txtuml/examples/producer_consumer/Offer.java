package hu.elte.txtuml.examples.producer_consumer;

import hu.elte.txtuml.api.model.Association;

public class Offer extends Association {

	public class producer extends Association.Many<Producer> {
	}

	public class storage extends Association.MaybeOne<Storage> {
	}
}
