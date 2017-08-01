package hu.eltesoft.moonlander;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import hu.eltesoft.moonlander.model.MoonLander;

@Runtime(RuntimeType.THREADED)
@Group(contains = { MoonLander.class })
public class MoonLanderConfiguration extends Configuration {
}
