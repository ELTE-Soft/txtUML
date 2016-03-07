package hu.elte.txtuml.export.uml2.tests.models.log;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class S1 extends State {
		@Override
		public void entry() {
			Action.log("Hello");
		}
	}

	@From(Init.class)
	@To(S1.class)
	public class Init_S1 extends Transition {
	}
}