package hu.elte.txtuml.export.uml2.tests.models.while_control;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class WhileControl extends State {
		@Override
		public void entry() {
			int x = 42;
			while (x > 0) {
				--x;
			}
		}
	}

	@From(Init.class)
	@To(WhileControl.class)
	public class InitForControl extends Transition {
	}
}