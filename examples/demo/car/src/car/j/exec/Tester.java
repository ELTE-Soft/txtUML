package car.j.exec;

import car.x.model.Car;
import car.x.model.ChangeGear;
import car.x.model.GearType;
import car.x.model.Gearbox;
import car.x.model.GearboxCar;
import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LogLevel;

public class Tester implements Execution {

	// Configure the logging
	@Override
	public void configure(Settings s) {
		s.logLevel = LogLevel.TRACE;
	}

	// For the execution, one Car and one Gearbox is needed
	Gearbox g;
	Car c;

	// Create car and gearbox, link them via the GearboxCar associacion
	@Override
	public void initialization() {
		g = Action.create(Gearbox.class);
		c = Action.create(Car.class);
		Action.link(GearboxCar.g.class, g, GearboxCar.c.class, c);
		Action.start(g);
		Action.start(c);
	}

	// During the execution
	@Override
	public void during() {
		for (int i = 0; i < 3; ++i) {
			API.log("");
			// Shift up
			API.send(new ChangeGear(new GearType(i)), g);
		}

		for (int i = 2; i >= -1; --i) {
			API.log("");
			// Shift down
			API.send(new ChangeGear(new GearType(i)), g);
		}

		API.log("");
		API.send(new ChangeGear(new GearType(0)), g);
	}

	public static void main(String[] args) {
		// Start the execution
		new Tester().run();
	}
}
