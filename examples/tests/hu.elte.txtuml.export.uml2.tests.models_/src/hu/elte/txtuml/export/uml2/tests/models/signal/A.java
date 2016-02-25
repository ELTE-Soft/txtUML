package hu.elte.txtuml.export.uml2.tests.models.signal;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class A extends ModelClass {

	public class Init extends Initial {
	}

	public class TestSendState extends State {
		@Override
		public void entry() {
			Sig sig = new Sig();
			sig.val = 1;
			sig.b = true;
			sig.param = "test";
			A inst = Action.create(A.class);

			Action.send(inst, sig);
		}
	}

	@From(Init.class)
	@To(TestSendState.class)
	public class InitToTestSendState extends Transition {
	}
}