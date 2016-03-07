package hu.elte.txtuml.export.uml2.tests.models.do_while_control;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class DoWhileControl extends State {
		@Override
		public void entry() {
			int x = 42;
			do {
				--x;
			} while (x > 0);
		}
	}

	@From(Init.class)
	@To(DoWhileControl.class)
	public class InitDoWhileControl extends Transition {
	}
}