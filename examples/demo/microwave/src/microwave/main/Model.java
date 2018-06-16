package microwave.main;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import microwave.model.ClockOfOven;
import microwave.model.Door;
import microwave.model.DoorOfOven;
import microwave.model.MicrowaveOven;
import microwave.model.OpenOrClose;
import microwave.model.SimpleClock;
import microwave.model.Start;
import microwave.model.Stop;

public class Model implements Execution {

	private SimpleClock clock;
	private Door door;

	@Override
	public void initialization() {
		MicrowaveOven oven = Action.create(MicrowaveOven.class);
		Action.start(oven);

		clock = Action.create(SimpleClock.class);
		Action.start(clock);
		Action.link(ClockOfOven.clock.class, clock, ClockOfOven.oven.class, oven);

		door = Action.create(Door.class);
		Action.start(door);
		Action.link(DoorOfOven.door.class, door, DoorOfOven.oven.class, oven);
	}
	
	public void openOrCloseDoor() {
		API.send(new OpenOrClose(), door);
	}
	
	public void startClock() {
		API.send(new Start(), clock);
	}

	public void stopClock() {
		API.send(new Stop(), clock);
	}

}
