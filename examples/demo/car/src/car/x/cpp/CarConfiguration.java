package car.x.cpp;

import car.x.model.Car;
import car.x.model.Gearbox;
import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;

@Group(contains = { Car.class, Gearbox.class }, max = 5, constant = 2)
@Runtime(RuntimeType.THREADED)
public class CarConfiguration extends Configuration {

}
