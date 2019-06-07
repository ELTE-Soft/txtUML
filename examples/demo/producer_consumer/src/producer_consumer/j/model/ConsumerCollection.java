package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.Min;

@Min(0)
public class ConsumerCollection<T> extends Collection<T, ConsumerCollection<T>> {
}
