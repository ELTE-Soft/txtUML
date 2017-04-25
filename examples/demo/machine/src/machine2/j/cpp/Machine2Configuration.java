package machine2.j.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import machine2.j.model.Machine;
import machine2.j.model.User;

@Group(contains = { Machine.class, User.class })
public class Machine2Configuration extends Configuration {

}
