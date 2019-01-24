package hu.eltesoft.moonlander;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.eltesoft.moonlander.model.MoonLander;
import hu.eltesoft.moonlander.model.World;

@Group(contains = { MoonLander.class, World.class }, rate = 1.0)
public class MoonLanderConfiguration extends Configuration {

}
