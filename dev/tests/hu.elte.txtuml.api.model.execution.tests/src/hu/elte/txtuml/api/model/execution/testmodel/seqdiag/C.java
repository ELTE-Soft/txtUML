package hu.elte.txtuml.api.model.execution.testmodel.seqdiag;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class C extends ModelClass {

	public class Init extends Initial {

	}

	@From(Init.class)
	@To(StateA.class)
	public class IToA extends Transition {

	}

	public class StateA extends State {

	}

	@From(StateA.class)
	@To(StateB.class)
	@Trigger(TestSig.class)
	public class StateAToStateB extends Transition {

	}

	public class StateB extends State {
		@Override
		public void entry() {
			Action.send(new TestSig(), assoc(BToC.BSide.class).selectAny());
		}
	}

	@From(StateB.class)
	@To(StateA.class)
	@Trigger(TestSig.class)
	public class StateBToStateA extends Transition {

	}
}
