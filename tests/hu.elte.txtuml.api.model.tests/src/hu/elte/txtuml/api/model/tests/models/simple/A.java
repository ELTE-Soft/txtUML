package hu.elte.txtuml.api.model.tests.models.simple;

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
	public class Initialize extends Transition {
	}

	@From(S1.class)
	@To(S2.class)
	@Trigger(Sig.class)
	public class S1_S2 extends Transition {
	}

	@From(S2.class)
	@To(S1.class)
	@Trigger(Sig.class)
	public class S2_S1 extends Transition {
	}
}
