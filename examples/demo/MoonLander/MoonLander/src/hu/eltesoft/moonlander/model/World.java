package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class World extends ModelClass {
	
	public boolean ready = false;
	public double h;
	public double v;
	public double u;
	
	public void step() {
		ready = false;
		MoonLander lander = assoc(LanderWorld.lander.class).selectAny();
		Action.send(new ControlCycleSignal(h, v), lander);
	}
	
	public class Init extends Initial {
	}
	
	@From(Init.class)
	@To(Calculating.class)
	public class InitToFF extends Transition {
	}
	
	public class Calculating extends State {
	}
	
	@From(Calculating.class)
	@To(Calculating.class)
	@Trigger(ControlSignal.class)
	public class Calculate extends Transition {
		@Override
		public void effect() {
			ControlSignal trigger = getTrigger(ControlSignal.class);
			u = trigger.u;
			ready = true;
		}
	}

}
