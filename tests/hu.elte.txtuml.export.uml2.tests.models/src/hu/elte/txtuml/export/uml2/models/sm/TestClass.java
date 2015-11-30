package hu.elte.txtuml.export.uml2.models.sm;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class S1 extends State {
	}

	@From(Init.class)
	@To(S1.class)
	public class Init_S1 extends Transition {
	}

	@From(S1.class)
	@To(S1.class)
	@Trigger(TestSignal.class)
	public class S1_S1 extends Transition {
	}
}