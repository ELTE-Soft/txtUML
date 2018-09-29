package airlock;


import airlock.model.Airlock;
import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;

@Runtime(RuntimeType.SINGLE)
@Group(contains = { Airlock.class })
public class AirlockConfiguration extends Configuration {
}
