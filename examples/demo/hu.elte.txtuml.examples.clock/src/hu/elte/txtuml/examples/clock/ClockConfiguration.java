package hu.elte.txtuml.examples.clock;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Multithreading;
import hu.elte.txtuml.examples.clock.model.classes.Display;
import hu.elte.txtuml.examples.clock.model.classes.Hand;
import hu.elte.txtuml.examples.clock.model.classes.Pendulum;

@Group(contains = {Display.class, Hand.class, Pendulum.class}, max = 3, gradient = 1)
public class ClockConfiguration extends Configuration{

}
