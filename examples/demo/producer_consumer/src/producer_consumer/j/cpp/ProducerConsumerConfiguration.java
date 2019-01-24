package producer_consumer.j.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Storage;

@Group(contains = { Consumer.class }, rate = 0.4)
@Group(contains = { Producer.class }, rate = 0.5)
@Group(contains = { Storage.class }, rate = 0.1)
@Runtime(RuntimeType.THREADED)
public class ProducerConsumerConfiguration extends Configuration {

}
