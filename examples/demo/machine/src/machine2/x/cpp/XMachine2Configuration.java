package machine2.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import machine2.x.model.Machine;
import machine2.x.model.User;

@Group(contains = { Machine.class, User.class })
public class XMachine2Configuration extends Configuration {

}
