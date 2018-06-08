package hu.elte.txtuml.examples.feeder;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import hu.elte.txtuml.examples.feeder.model.Source;

@Group(contains = { Source.class })
@Runtime(RuntimeType.SINGLE)
public class SourceConfiguration extends Configuration {
}
