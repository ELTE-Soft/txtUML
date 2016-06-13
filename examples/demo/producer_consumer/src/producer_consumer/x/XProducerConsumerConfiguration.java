package producer_consumer.x;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import producer_consumer.x.model.Consumer;
import producer_consumer.x.model.Producer;
import producer_consumer.x.model.Storage;

@Group(contains = { Consumer.class })
@Group(contains = { Producer.class }, max = 10, gradient = 0.5)
@Group(contains = { Storage.class }, constant = 2)
public class XProducerConsumerConfiguration extends Configuration {

}
