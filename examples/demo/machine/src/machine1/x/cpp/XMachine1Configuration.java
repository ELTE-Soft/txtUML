package machine1.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import machine1.x.model.Machine;
import machine1.x.model.User;

@Group(contains = { Machine.class, User.class })
@Runtime(RuntimeType.THREADED)
public class XMachine1Configuration extends Configuration {

}
