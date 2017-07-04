package machine2.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import machine2.x.model.Machine;
import machine2.x.model.User;

@Group(contains = { Machine.class, User.class })
@Runtime() // Threaded is the default
public class XMachine2Configuration extends Configuration {

}
