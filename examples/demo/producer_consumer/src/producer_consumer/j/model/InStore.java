package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Association;

public class InStore extends Association {

	public class item extends Association.Many<Item> {
	}

	public class storage extends Association.MaybeOne<Storage> {
	}
}
