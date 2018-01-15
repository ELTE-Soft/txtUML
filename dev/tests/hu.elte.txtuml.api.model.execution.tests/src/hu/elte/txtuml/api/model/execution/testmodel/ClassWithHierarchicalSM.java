package hu.elte.txtuml.api.model.execution.testmodel;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig1;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig2;

public class ClassWithHierarchicalSM extends ClassWithOwnLog {

	public class Init extends Initial {
	}

	public class S1 extends State {
	}

	public class CS1 extends CompositeState {

		@Override
		public void entry() {
			Action.log("entry of CS1");
			addToOwnLog("entry of CS1");
		}

		@Override
		public void exit() {
			Action.log("exit of CS1");
			addToOwnLog("exit of CS1");
		}

		public class Init extends Initial {
		}

		public class S2 extends State {
		}

		public class CS2 extends CompositeState {

			@Override
			public void entry() {
				Action.log("entry of CS1.CS2");
				addToOwnLog("entry of CS1.CS2");
			}

			@Override
			public void exit() {
				Action.log("exit of CS1.CS2");
				addToOwnLog("exit of CS1.CS2");
			}

			public class Init extends Initial {
			}

			public class S3 extends State {

				@Override
				public void entry() {
					Action.log("entry of CS1.CS2.S3");
					addToOwnLog("entry of CS1.CS2.S3");
				}

				@Override
				public void exit() {
					Action.log("exit of CS1.CS2.S3");
					addToOwnLog("exit of CS1.CS2.S3");
				}

			}

			@From(Init.class)
			@To(S3.class)
			public class Initialize extends Transition {
			}

		}

		@From(Init.class)
		@To(S2.class)
		public class Initialize extends Transition {
		}

		@From(S2.class)
		@To(CS2.class)
		@Trigger(Sig0.class)
		public class S2_CS2 extends Transition {
		}

		@From(CS2.class)
		@To(S2.class)
		@Trigger(Sig2.class)
		public class CS2_S2 extends Transition {
		}

	}

	@From(Init.class)
	@To(S1.class)
	public class Initialize extends Transition {
	}

	@From(S1.class)
	@To(CS1.class)
	@Trigger(Sig0.class)
	public class S1_CS1 extends Transition {
	}

	@From(CS1.class)
	@To(S1.class)
	@Trigger(Sig1.class)
	public class CS1_S1 extends Transition {
	}
}
