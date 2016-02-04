package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.examples.machine.model1.Machine;
import hu.elte.txtuml.examples.machine.model1.User;
import hu.elte.txtuml.export.cpp.description.Configuration;
import hu.elte.txtuml.export.cpp.description.Group;
import hu.elte.txtuml.export.cpp.description.Multithreading;

@Group(contains = {Machine.class, User.class})
@Multithreading(false)
public class Machine1Platform extends Configuration {}
