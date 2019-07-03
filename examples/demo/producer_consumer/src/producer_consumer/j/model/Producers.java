package producer_consumer.j.model;

import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.UniqueCollection;

@Min(0)
@Max(2)
public class Producers<T> extends UniqueCollection<T, Producers<T>> {

}
