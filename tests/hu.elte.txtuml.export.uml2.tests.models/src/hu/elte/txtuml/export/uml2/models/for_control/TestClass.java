package hu.elte.txtuml.export.uml2.models.for_control;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class ForControl extends State {
		@Override
		public void entry() {
			int condInt = 5;

			TestClass[] cls = new TestClass[5];

			for (int i = 0; i < condInt; ++i) {
				cls[i] = Action.create(TestClass.class, i);
			}

			for (int i = 0; i < condInt; ++i) {
				Action.delete(cls[i]);
			}
		}
	}

	@From(Init.class)
	@To(ForControl.class)
	public class InitForControl extends Transition {
	}
}