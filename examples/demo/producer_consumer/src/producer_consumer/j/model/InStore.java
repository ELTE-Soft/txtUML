package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.ZeroToOne;

public class InStore extends Association {

	public class item extends End<Any<Item>> {
	}

	public class storage extends End<ZeroToOne<Storage>> {
	}
}
