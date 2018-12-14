package train.j.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import train.j.model.Engine;
import train.j.model.Gearbox;

@Group(contains = { Engine.class, Gearbox.class }, rate = 1.0)
@Runtime(RuntimeType.THREADED)
public class TrainConfiguration extends Configuration {

}
