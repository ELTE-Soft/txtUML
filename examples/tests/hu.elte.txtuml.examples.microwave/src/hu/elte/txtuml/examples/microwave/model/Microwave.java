package hu.elte.txtuml.examples.microwave.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.Timer;
import hu.elte.txtuml.examples.microwave.model.associations.Usage;
import hu.elte.txtuml.examples.microwave.model.signals.Close;
import hu.elte.txtuml.examples.microwave.model.signals.Ding;
import hu.elte.txtuml.examples.microwave.model.signals.Finish;
import hu.elte.txtuml.examples.microwave.model.signals.Get;
import hu.elte.txtuml.examples.microwave.model.signals.Open;
import hu.elte.txtuml.examples.microwave.model.signals.Put;
import hu.elte.txtuml.examples.microwave.model.signals.SetIntensity;
import hu.elte.txtuml.examples.microwave.model.signals.SetTime;
import hu.elte.txtuml.examples.microwave.model.signals.Start;
import hu.elte.txtuml.examples.microwave.model.signals.Stop;
import hu.elte.txtuml.examples.microwave.model.signals.TimedOut;

public class Microwave extends ModelClass {
	int intensity = 1;
	int time = 0;

	public class Init extends Initial {
	}

	public class Opened extends CompositeState {
		@Override
		public void entry() {
			Action.log("Microwave: enterring idle opened. Resetting values.");
			intensity = 1;
			time = 0;
		}

		public class Init extends Initial {
		}

		public class HasContent extends State {
			@Override
			public void entry() {
				Action.log("Microwave: enterring full opened state.");
			}
		}

		public class HasNoContent extends State {
			@Override
			public void entry() {
				Action.log("Microwave: enterring empty opened state.");
			}
		}

		@From(Init.class)
		@To(HasContent.class)
		public class Initialize1 extends Transition {
		}

		@From(Init.class)
		@To(HasNoContent.class)
		public class Initialize2 extends Transition {
		}

		@From(HasContent.class)
		@To(HasNoContent.class)
		@Trigger(Get.class)
		public class GetContent extends Transition {
			@Override
			public void effect() {
				Action.log("Microwave: content removed.");
			}
		}

		@From(HasNoContent.class)
		@To(HasContent.class)
		@Trigger(Put.class)
		public class PutContent extends Transition {
			@Override
			public void effect() {
				Action.log("Microwave: content put in.");
			}
		}
	}

	public class Closed extends CompositeState {
		@Override
		public void entry() {
			Action.log("Microwave: enterring idle closed.");
		}

		public class Init extends Initial {
		}

		public class HasContent extends State {
			@Override
			public void entry() {
				Action.log("Microwave: enterring full closed state.");
			}
		}

		public class HasNoContent extends State {
			@Override
			public void entry() {
				Action.log("Microwave: enterring empty closed state.");
			}
		}

		@From(Init.class)
		@To(HasContent.class)
		public class Initialize1 extends Transition {
		}

		@From(Init.class)
		@To(HasNoContent.class)
		public class Initialize2 extends Transition {
		}

		@From(HasContent.class)
		@To(HasContent.class)
		@Trigger(SetIntensity.class)
		public class AdjustIntensity extends Transition {
			@Override
			public void effect() {
				intensity = getTrigger(SetIntensity.class).value;
				Action.log("Microwave: intensity set.");
			}
		}

		@From(HasContent.class)
		@To(HasContent.class)
		@Trigger(SetTime.class)
		public class AdjustTime extends Transition {
			@Override
			public void effect() {
				time = getTrigger(SetTime.class).value;
				Action.log("Microwave: time set.");
			}
		}
	}

	@From(Opened.class)
	@To(Closed.class)
	@Trigger(Close.class)
	public class CloseDoor extends Transition {
	}

	@From(Closed.class)
	@To(Opened.class)
	@Trigger(Open.class)
	public class OpenDoor extends Transition {
	}

	public class Heating extends CompositeState {
		@Override
		public void entry() {
			Action.log("Microwave: enterring heating state.");
		}

		public class Init extends Initial {
		}

		public class Working extends State {
			@Override
			public void entry() {
				--time;
				Action.log("Microwave: remaining time: " + time + " second(s).");

				Timer.start(Microwave.this, new TimedOut(), 1000);
			}
		}

		public class Finished extends State {
			@Override
			public void entry() {
				Action.log("Microwave: DING!");
				Action.send(new Ding(), Microwave.this);
			}
		}

		@From(Init.class)
		@To(Working.class)
		public class Initialize extends Transition {
		}

		@From(Working.class)
		@To(Working.class)
		@Trigger(TimedOut.class)
		public class Loop extends Transition {
			@Override
			public boolean guard() {
				return time > 0;
			}

			@Override
			public void effect() {

			}
		}

		@From(Working.class)
		@To(Finished.class)
		@Trigger(TimedOut.class)
		public class LoopQuit extends Transition {
			
			@Override
			public void effect() {
				Action.send(new Finish(), Microwave.this);
			}
			
			@Override
			public boolean guard() {
				return time == 0;
			}
		}

	}

	@From(Init.class)
	@To(Closed.class)
	public class Initialize extends Transition {
		@Override
		public void effect() {
			Action.log("\tMicrowave: initializing...");
		}
	}

	@From(Closed.class)
	@To(Heating.class)
	@Trigger(Start.class)
	public class StartHeating extends Transition {
		@Override
		public void effect() {
			Action.log("Microwave: start heating...");
		}
	}

	@From(Heating.class)
	@To(Closed.class)
	@Trigger(Stop.class)
	public class StopHeating1 extends Transition {
		@Override
		public void effect() {
			intensity = 1;
			time = 0;
		}
	}

	@From(Heating.class)
	@To(Opened.class)
	@Trigger(Open.class)
	public class StopHeating2 extends Transition {
		@Override
		public void effect() {
			intensity = 1;
			time = 0;
		}
	}

	@From(Heating.class)
	@To(Closed.class)
	@Trigger(Finish.class)
	public class Finishing extends Transition {
		@Override
		public void effect() {
			Human h = Microwave.this.assoc(Usage.userOfMicrowave.class).selectAny();
			Action.send(new Ding(), h);
		}
	}

}