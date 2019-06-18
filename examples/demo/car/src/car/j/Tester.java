package car.j;

import car.j.model.Car;
import car.j.model.Gearbox;
import car.j.model.associations.GearboxCar;
import car.j.model.datatypes.GearType;
import car.j.model.signals.ChangeGear;
import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LogLevel;

public class Tester implements Execution {

	@Override
	public void configure(Settings s) {
		s.logLevel = LogLevel.TRACE;
	}

	Gearbox g;
	Car c;

	@Override
	public void initialization() {
		g = Action.create(Gearbox.class);
		c = Action.create(Car.class);
		Action.link(GearboxCar.g.class, g, GearboxCar.c.class, c);
		Action.start(g);
		Action.start(c);
	}

	@Override
	public void during() {
		for (int i = 0; i < 3; ++i) {
			API.log("");
			API.send(new ChangeGear(new GearType(i)), g);
		}

		for (int i = 2; i >= -1; --i) {
			API.log("");
			API.send(new ChangeGear(new GearType(i)), g);
		}

		API.log("");
		API.send(new ChangeGear(new GearType(0)), g);
	}

	public static void main(String[] args) {
		new Tester().run();
	}
}
