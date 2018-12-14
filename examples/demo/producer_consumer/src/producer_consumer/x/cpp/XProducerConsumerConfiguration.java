package producer_consumer.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import producer_consumer.x.model.Consumer;
import producer_consumer.x.model.Producer;
import producer_consumer.x.model.Storage;

@Group(contains = { Consumer.class })
@Group(contains = { Producer.class }, rate = 0.5)
@Group(contains = { Storage.class }, rate = 0.2)
@Runtime(RuntimeType.THREADED)
public class XProducerConsumerConfiguration extends Configuration {

}
