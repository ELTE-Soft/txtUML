package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class Production extends Association {

	public class producer extends End<Producers<Producer>> {
	}

	public class storage extends End<One<Storage>> {
	}
}
