package hu.elte.txtuml.examples.producer_consumer.model;

import hu.elte.txtuml.api.model.Association;

public class Request extends Association {

	public class storage extends Association.MaybeOne<Storage> {
	}

	public class consumer extends Association.Many<Consumer> {
	}
}
