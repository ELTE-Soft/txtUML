package hu.elte.txtuml.examples.feeder;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import hu.elte.txtuml.examples.feeder.model.Sink;

@Runtime(RuntimeType.SINGLE)
@Group(contains = { Sink.class })
public class SinkConfiguration extends Configuration {
}
