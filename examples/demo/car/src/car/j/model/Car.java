package car.j.model;

import car.j.model.signals.ChangeSpeed;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Car extends ModelClass {
	public class Init extends StateMachine.Initial {
	}

	public class Backwards extends StateMachine.State {
	}

	public class Stopped extends StateMachine.State {
	}

	@From(Car.Init.class)
	@To(Car.Stopped.class)
	public class Initialize extends StateMachine.Transition {
	}

	public class Forwards extends StateMachine.CompositeState {
		public class FInit extends StateMachine.Initial {
		}

		public class Slow extends StateMachine.State {
		}

		public class Fast extends StateMachine.State {
		}

		@From(Car.Forwards.FInit.class)
		@To(Car.Forwards.Slow.class)
		public class FInitialize extends StateMachine.Transition {
		}

		@From(Car.Forwards.Slow.class)
		@To(Car.Forwards.Fast.class)
		@Trigger(ChangeSpeed.class)
		public class FasterSpeed extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return (getTrigger(ChangeSpeed.class).speedType.speed == 1);
			}
		}

		@From(Car.Forwards.Fast.class)
		@To(Car.Forwards.Slow.class)
		@Trigger(ChangeSpeed.class)
		public class SlowerSpeed extends StateMachine.Transition {
			@Override
			public boolean guard() {
				return (getTrigger(ChangeSpeed.class).speedType.speed == (-1));
			}
		}
	}

	@From(Car.Stopped.class)
	@To(Car.Forwards.class)
	@Trigger(ChangeSpeed.class)
	public class StartForwards extends StateMachine.Transition {
		@Override
		public boolean guard() {
			return (getTrigger(ChangeSpeed.class).speedType.speed == 1);
		}
	}

	@From(Car.Forwards.class)
	@To(Car.Stopped.class)
	@Trigger(ChangeSpeed.class)
	public class StoppingForward extends StateMachine.Transition {
		@Override
		public boolean guard() {
			return (getTrigger(ChangeSpeed.class).speedType.speed == 0);
		}
	}

	@From(Car.Backwards.class)
	@To(Car.Stopped.class)
	@Trigger(ChangeSpeed.class)
	public class StoppingBackward extends StateMachine.Transition {
		@Override
		public boolean guard() {
			return (getTrigger(ChangeSpeed.class).speedType.speed == 0);
		}
	}

	@From(Car.Stopped.class)
	@To(Car.Backwards.class)
	@Trigger(ChangeSpeed.class)
	public class StartBackwards extends StateMachine.Transition {
		@Override
		public boolean guard() {
			return (getTrigger(ChangeSpeed.class).speedType.speed == (-1));
		}
	}
}
