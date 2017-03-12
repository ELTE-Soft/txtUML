package machine1.j;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import machine1.j.model.Machine;
import machine1.j.model.User;

@Group(contains = { Machine.class, User.class })
public class Machine1Configuration extends Configuration {

}
