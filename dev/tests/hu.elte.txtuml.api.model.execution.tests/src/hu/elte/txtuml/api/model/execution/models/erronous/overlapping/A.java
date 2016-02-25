package hu.elte.txtuml.api.model.execution.models.erronous.overlapping;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class A extends ModelClass {

	public class Init extends Initial {
	}

	public class S1 extends State {
	}

	public class S2 extends State {
	}

	@From(Init.class)
	@To(S1.class)
	public class T0 extends Transition {
	}

	@From(S1.class)
	@To(S2.class)
	@Trigger(Sig.class)
	public class T1 extends Transition {
		@Override
		public boolean guard() {
			return true;
		}
	}

	@From(S1.class)
	@To(S2.class)
	@Trigger(Sig.class)
	public class T2 extends Transition {
		@Override
		public boolean guard() {
			return true;
		}
	}
}
