package hu.elte.txtuml.export.uml2.tests.models.sm_actions;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class S1 extends State {
		
		@Override
		public void entry() {
			Action.log("entry S1");
		}
		
		@Override
		public void exit() {
			Action.log("exit S1");
		}
		
	}

	@From(Init.class)
	@To(S1.class)
	public class Init_S1 extends Transition {
		
		@Override
		public void effect() {
			Action.log("exit Init -> S1");
		}
		
	}

	@From(S1.class)
	@To(S1.class)
	@Trigger(TestSignal.class)
	public class S1_S1 extends Transition {
		
		@Override
		public boolean guard() {
			return false;
		}
	}
}