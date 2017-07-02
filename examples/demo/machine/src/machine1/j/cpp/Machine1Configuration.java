package machine1.j.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import machine1.j.model.Machine;
import machine1.j.model.User;

@Group(contains = { Machine.class, User.class })
@Runtime(RuntimeType.SINGLE)
public class Machine1Configuration extends Configuration {

}
