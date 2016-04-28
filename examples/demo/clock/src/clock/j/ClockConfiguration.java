 package clock.j;
 
 import hu.elte.txtuml.api.deployment.Configuration;
 import hu.elte.txtuml.api.deployment.Group;
 import clock.j.model.classes.Display;
 import clock.j.model.classes.Hand;
 import clock.j.model.classes.Pendulum;
 
 @Group(contains = {Display.class, Hand.class, Pendulum.class}, max = 3, gradient = 1)
 public class ClockConfiguration extends Configuration{
 
 }
