package car.j.model.associations;

import car.j.model.Car;
import car.j.model.Gearbox;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class GearboxCar extends Association {
	public class g extends Association.End<One<Gearbox>> {
	}

	public class c extends Association.End<One<Car>> {
	}
}
