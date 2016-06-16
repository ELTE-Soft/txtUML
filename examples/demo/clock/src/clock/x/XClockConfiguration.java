 package clock.x;
 
 import hu.elte.txtuml.api.deployment.Configuration;
 import hu.elte.txtuml.api.deployment.Group;
 import clock.x.model.Display;
 import clock.x.model.Hand;
 import clock.x.model.Pendulum;
 
 @Group(contains = {Display.class, Hand.class, Pendulum.class}, max = 3, gradient = 1)
 public class XClockConfiguration extends Configuration{
 
 }
