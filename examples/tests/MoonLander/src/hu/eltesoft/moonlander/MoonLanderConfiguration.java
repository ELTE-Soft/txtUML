package hu.eltesoft.moonlander;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import hu.eltesoft.moonlander.model.MoonLander;

@Group(contains = { MoonLander.class })
@Runtime(RuntimeType.SINGLE)
public class MoonLanderConfiguration extends Configuration {
}
