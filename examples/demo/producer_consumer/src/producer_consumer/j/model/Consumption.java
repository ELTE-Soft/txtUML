package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class Consumption extends Association {

	public class storage extends End<One<Storage>> {
	}

	public class consumer extends End<Any<Consumer>> {
	}
}
