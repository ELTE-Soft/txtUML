package hu.elte.txtuml.export.uml2.tests.models.if_then_control;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestModelClass extends ModelClass {
	public class Init extends Initial {
	}

	public class IfControl extends State {
		@Override
		public void entry() {
			int condInt = 1;

			TestModelClass inst = null;

			if (condInt == 1) {
				inst = Action.create(TestModelClass.class, 1);
			} else {
				inst = Action.create(TestModelClass.class, "testing");
			}

			Action.delete(inst);
		}
	}

	@From(Init.class)
	@To(IfControl.class)
	public class InitIfControl extends Transition {
	}
}
