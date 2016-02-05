package hu.elte.txtuml.examples.clock;

import hu.elte.txtuml.api.platform.Configuration;
import hu.elte.txtuml.api.platform.Group;
import hu.elte.txtuml.api.platform.Multithreading;


import hu.elte.txtuml.examples.clock.model.classes.*;

@Group(contains = {Display.class, Hand.class, Pendulum.class}, max = 3, gradient = 1)
@Multithreading(false)
public class ClockConfiguration extends Configuration{

}
