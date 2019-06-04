package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.Min;

@Min(0)
public class C1<T> extends Collection<T, C1<T>> {
}
