package train.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import train.x.model.Engine;
import train.x.model.Gearbox;

@Group(contains = { Engine.class, Gearbox.class }, rate = 1.0)
@Runtime(RuntimeType.THREADED)
public class XTrainConfiguration extends Configuration {

}
