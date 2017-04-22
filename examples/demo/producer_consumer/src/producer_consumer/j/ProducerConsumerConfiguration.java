package producer_consumer.j;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import producer_consumer.j.model.Consumer;
import producer_consumer.j.model.Producer;
import producer_consumer.j.model.Storage;

@Group(contains = { Consumer.class })
@Group(contains = { Producer.class }, max = 10, gradient = 0.5)
@Group(contains = { Storage.class }, constant = 2, max = 2)
public class ProducerConsumerConfiguration extends Configuration {

}
