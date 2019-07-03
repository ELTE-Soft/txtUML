package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.OrderedUniqueCollection;

@Min(0)
@Max(3)
public class Consumers<T> extends OrderedUniqueCollection<T,Consumers<T>> {
}
