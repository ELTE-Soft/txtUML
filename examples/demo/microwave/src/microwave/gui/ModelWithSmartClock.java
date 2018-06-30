package microwave.gui;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import microwave.model.ChangeTime;
import microwave.model.ClockOfOven;
import microwave.model.Door;
import microwave.model.DoorOfOven;
import microwave.model.MicrowaveOven;
import microwave.model.OpenOrClose;
import microwave.model.SmartClock;
import microwave.model.Start;
import microwave.model.Stop;

public class ModelWithSmartClock implements Execution {

	private SmartClock clock;
	private Door door;

	@Override
	public void initialization() {
		MicrowaveOven oven = Action.create(MicrowaveOven.class);
		Action.start(oven);

		clock = Action.create(SmartClock.class);
		Action.start(clock);
		Action.link(ClockOfOven.clock.class, clock, ClockOfOven.oven.class, oven);

		door = Action.create(Door.class);
		Action.start(door);
		Action.link(DoorOfOven.door.class, door, DoorOfOven.oven.class, oven);
	}

	public void changeTime(int newValue) {
		API.send(new ChangeTime(newValue), clock);
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
