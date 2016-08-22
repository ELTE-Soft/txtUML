package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class MoonLander extends ModelClass {

	public MoonLander(World world) {
		Action.link(LanderWorld.world.class, world, LanderWorld.lander.class, this);
	}
		
	public class Init extends Initial {
	}
	
	@From(Init.class)
	@To(FreeFall.class)
	public class InitToFF extends Transition {
	}
	
	public class FreeFall extends State {
		
		@Override
		public void entry() {
			World world = assoc(LanderWorld.world.class).selectAny();
			Action.send(new ControlSignal(0), world);
		}
	}
	
	@Trigger(ControlCycleSignal.class)
	@From(FreeFall.class)
	@To(FreeFall.class)
	public class RemainFF extends Transition {
		@Override
		public boolean guard() {
			ControlCycleSignal signal = getTrigger(ControlCycleSignal.class);
			return signal.h >= 500 && signal.v >= -20;
		}
	}
	
	@Trigger(ControlCycleSignal.class)
	@From(FreeFall.class)
	@To(Controlled.class)
	public class FFtoControlled extends Transition {
		@Override
		public boolean guard() {
			ControlCycleSignal signal = getTrigger(ControlCycleSignal.class);
			return signal.h < 500 || signal.v < -20;
		}
	}

	public class Controlled extends State {
		
		@Override
		public void entry() {
			World world = assoc(LanderWorld.world.class).selectAny();
			// better because of rounding in generated code
			Action.send(new ControlSignal(world.v*world.h*world.h/500000 - (-world.v*world.h/1000) - world.v/2), world);
		}
	}
	
	@Trigger(ControlCycleSignal.class)
	@From(Controlled.class)
	@To(Controlled.class)
	public class StillControlled extends Transition {
		@Override
		public boolean guard() {
			ControlCycleSignal signal = getTrigger(ControlCycleSignal.class);
			return signal.h >= 1;
		}
	}
	
	@Trigger(ControlCycleSignal.class)
	@From(Controlled.class)
	@To(SuccessfulLanding.class)
	public class ControlledToLanding extends Transition {
		@Override
		public boolean guard() {
			ControlCycleSignal signal = getTrigger(ControlCycleSignal.class);
			return signal.h < 1 && signal.v >= -10;
		}
	}

	
	public class SuccessfulLanding extends State {
		
		@Override
		public void entry() {
			World world = assoc(LanderWorld.world.class).selectAny();
			Action.send(new ControlSignal(0), world);
		}
	}
	
	@Trigger(ControlCycleSignal.class)
	@From(Controlled.class)
	@To(Crashed.class)
	public class ControlledToCrashed extends Transition {
		@Override
		public boolean guard() {
			ControlCycleSignal signal = getTrigger(ControlCycleSignal.class);
			return signal.h < 1 && signal.v < -10;
		}
	}
	
	public class Crashed extends State {
		
		@Override
		public void entry() {
			World world = assoc(LanderWorld.world.class).selectAny();
			Action.send(new ControlSignal(0), world);
		}
	}
	
}
