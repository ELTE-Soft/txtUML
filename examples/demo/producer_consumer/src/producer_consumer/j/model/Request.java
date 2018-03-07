package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.ZeroToOne;

public class Request extends Association {

	public class storage extends End<ZeroToOne<Storage>> {
	}

	public class consumer extends End<Any<Consumer>> {
	}
}
