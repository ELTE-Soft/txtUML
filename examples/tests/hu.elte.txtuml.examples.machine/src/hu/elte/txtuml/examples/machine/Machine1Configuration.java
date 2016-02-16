package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Multithreading;
import hu.elte.txtuml.examples.machine.model1.Machine;
import hu.elte.txtuml.examples.machine.model1.User;

@Group(contains = { Machine.class, User.class })
public class Machine1Configuration extends Configuration {
}
