package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class MoonLander extends ModelClass {

	public void add() {
		
	}
	
	public MoonLander(FMUEnvironment world) {
		add();
		Action.link(LanderWorld.world.class, world, LanderWorld.lander.class, this);
	}
		
	public class Init extends Initial {
	}
	
	@From(Init.class)
	@To(FreeFall.class)
	public class InitToFF extends Transition {
	}
	
	public class FreeFall extends State {
	}
	
	@Trigger(InputSignal.class)
	@From(FreeFall.class)
	@To(FreeFall.class)
	public class RemainFF extends Transition {
		@Override
		public boolean guard() {
			InputSignal signal = getTrigger(InputSignal.class);
			return signal.h >= 500 && signal.v >= -20;
		}
		
		@Override
		public void effect() {
			FMUEnvironment world = assoc(LanderWorld.world.class).one();
			Action.send(new OutputSignal(0), world);
		}
	}
	
	@Trigger(InputSignal.class)
	@From(FreeFall.class)
	@To(Controlled.class)
	public class FFtoControlled extends Transition {
		@Override
		public boolean guard() {
			InputSignal signal = getTrigger(InputSignal.class);
			return signal.h < 500 || signal.v < -20;
		}
		
		@Override
		public void effect() {
			InputSignal signal = getTrigger(InputSignal.class);
			FMUEnvironment world = assoc(LanderWorld.world.class).one();
			// better because of rounding in generated code
			Action.send(new OutputSignal(signal.v*signal.h*signal.h/500000 - (-signal.v*signal.h/1000) - signal.v/2), world);
		}
	}

	public class Controlled extends State {
	}
	
	@Trigger(InputSignal.class)
	@From(Controlled.class)
	@To(Controlled.class)
	public class StillControlled extends Transition {
		@Override
		public boolean guard() {
			InputSignal signal = getTrigger(InputSignal.class);
			return signal.h >= 1;
		}
		
		@Override
		public void effect() {
			InputSignal signal = getTrigger(InputSignal.class);
			FMUEnvironment world = assoc(LanderWorld.world.class).one();
			// better because of rounding in generated code
			Action.send(new OutputSignal(signal.v*signal.h*signal.h/500000 - (-signal.v*signal.h/1000) - signal.v/2), world);
		}
	}
	
	@Trigger(InputSignal.class)
	@From(Controlled.class)
	@To(SuccessfulLanding.class)
	public class ControlledToLanding extends Transition {
		@Override
		public boolean guard() {
			InputSignal signal = getTrigger(InputSignal.class);
			return signal.h < 1 && signal.v >= -10;
		}
		
		@Override
		public void effect() {
			FMUEnvironment world = assoc(LanderWorld.world.class).one();
			Action.send(new OutputSignal(0), world);
		}
	}

	
	public class SuccessfulLanding extends State {
	}
	
	@Trigger(InputSignal.class)
	@From(Controlled.class)
	@To(Crashed.class)
	public class ControlledToCrashed extends Transition {
		@Override
		public boolean guard() {
			InputSignal signal = getTrigger(InputSignal.class);
			return signal.h < 1 && signal.v < -10;
		}
		
		@Override
		public void effect() {
			FMUEnvironment world = assoc(LanderWorld.world.class).one();
			Action.send(new OutputSignal(0), world);
		}
	}
	
	public class Crashed extends State {
	}
	
}