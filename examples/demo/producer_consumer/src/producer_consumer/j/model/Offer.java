package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.ZeroToOne;

public class Offer extends Association {

	public class producer extends End<Any<Producer>> {
	}

	public class storage extends End<ZeroToOne<Storage>> {
	}
}
