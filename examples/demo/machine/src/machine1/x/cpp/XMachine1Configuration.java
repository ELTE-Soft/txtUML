package machine1.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import machine1.x.model.Machine;
import machine1.x.model.User;

@Group(contains = { Machine.class, User.class })
public class XMachine1Configuration extends Configuration {

}
