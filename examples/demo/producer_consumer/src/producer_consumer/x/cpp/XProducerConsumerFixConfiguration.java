package producer_consumer.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import producer_consumer.x.model.Consumer;
import producer_consumer.x.model.Producer;
import producer_consumer.x.model.Storage;

@Group(contains = { Consumer.class })
@Group(contains = { Producer.class }, max = 10)
@Group(contains = { Storage.class }, constant = 2, max = 2)
@Runtime(RuntimeType.THREADED)
public class XProducerConsumerFixConfiguration extends Configuration {

}
