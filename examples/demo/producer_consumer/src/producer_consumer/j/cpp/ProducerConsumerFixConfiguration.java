package producer_consumer.j.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Storage;

@Group(contains = { Consumer.class })
@Group(contains = { Producer.class }, max = 10)
@Group(contains = { Storage.class }, constant = 2, max = 2)
@Runtime(RuntimeType.THREADED)
public class ProducerConsumerFixConfiguration extends Configuration {

}
