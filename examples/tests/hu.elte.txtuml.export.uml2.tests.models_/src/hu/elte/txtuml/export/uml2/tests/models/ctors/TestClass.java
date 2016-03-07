package hu.elte.txtuml.export.uml2.tests.models.ctors;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	class Init extends Initial {
	}
	
	class CtorCall extends State {
		@Override
		public void entry() {
			Action.create(OtherClass.class, 1);
		}
	}

	@From(Init.class)
	@To(CtorCall.class)
	class InitCtorCall extends Transition {
	}
}
