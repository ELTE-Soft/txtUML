package machine1.x;

import hu.elte.txtuml.api.deployment.Group;
import machine1.x.model.Machine;
import machine1.x.model.User;

@Group(contains = { Machine.class, User.class })
public class XMachine1Configuration {

}
