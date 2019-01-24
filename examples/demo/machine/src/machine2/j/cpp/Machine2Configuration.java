package machine2.j.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import machine2.j.model.Machine;
import machine2.j.model.User;

@Group(contains = { Machine.class, User.class }, rate = 1.0)
@Runtime(RuntimeType.THREADED)
public class Machine2Configuration extends Configuration {

}
