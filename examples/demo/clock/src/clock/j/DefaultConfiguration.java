package clock.j;

import clock.j.model.classes.Display;
import clock.j.model.classes.Hand;
import clock.j.model.classes.Pendulum;
import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;

@Group(contains = {Display.class, Hand.class, Pendulum.class}, max = 3, gradient = 1)
public class DefaultConfiguration extends Configuration{

}
