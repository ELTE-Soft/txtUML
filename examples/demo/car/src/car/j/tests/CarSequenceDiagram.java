package car.j.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.send;

import car.j.model.Car;
import car.j.model.Gearbox;
import car.j.model.associations.GearboxCar;
import car.j.model.datatypes.GearType;
import car.j.model.datatypes.SpeedType;
import car.j.model.signals.ChangeGear;
import car.j.model.signals.ChangeSpeed;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

public class CarSequenceDiagram extends SequenceDiagram {

	@Position(1)
	Lifeline<Car> car;

	@Position(2)
	Lifeline<Gearbox> gearbox;

	@Override
	public void initialize() {
		Gearbox gearboxInstance = Action.create(Gearbox.class);
		Car carInstance = Action.create(Car.class);
		
		Action.link(GearboxCar.g.class, gearboxInstance, GearboxCar.c.class, carInstance);

		gearbox = Sequence.createLifeline(gearboxInstance);
		car = Sequence.createLifeline(carInstance);

		Action.start(carInstance);
		Action.start(gearboxInstance);
	}

	@Override
	@ExecutionMode(ExecMode.STRICT)
	public void run() {
		fromActor(new ChangeGear(new GearType(1)), gearbox);
		assertState(gearbox, Gearbox.First.class);
		send(gearbox, new ChangeSpeed(new SpeedType(1)), car);
		assertState(car, Car.Forwards.Slow.class);
		fromActor(new ChangeGear(new GearType(2)), gearbox);
		assertState(gearbox, Gearbox.Second.class);
		send(gearbox, new ChangeSpeed(new SpeedType(1)), car);
		assertState(car, Car.Forwards.Fast.class);

		// Slowing down
		fromActor(new ChangeGear(new GearType(1)), gearbox);
		assertState(gearbox, Gearbox.First.class);
		send(gearbox, new ChangeSpeed(new SpeedType(-1)), car);
		assertState(car, Car.Forwards.Slow.class);

		// Stopping
		fromActor(new ChangeGear(new GearType(0)), gearbox);
		assertState(gearbox, Gearbox.N.class);
		send(gearbox, new ChangeSpeed(new SpeedType(0)), car);
		assertState(car, Car.Stopped.class);

		// Going backwards
		fromActor(new ChangeGear(new GearType(-1)), gearbox);
		assertState(gearbox, Gearbox.R.class);
		send(gearbox, new ChangeSpeed(new SpeedType(-1)), car);
		assertState(car, Car.Backwards.class);

		// Stopping
		fromActor(new ChangeGear(new GearType(0)), gearbox);
		assertState(gearbox, Gearbox.N.class);
		send(gearbox, new ChangeSpeed(new SpeedType(0)), car);
		assertState(car, Car.Stopped.class);

	}
}
