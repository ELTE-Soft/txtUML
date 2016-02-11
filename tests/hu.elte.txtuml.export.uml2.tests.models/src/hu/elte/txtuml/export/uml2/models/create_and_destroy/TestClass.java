package hu.elte.txtuml.export.uml2.models.create_and_destroy;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	class Init extends Initial {
	}

	class ObjectCreate extends State {
		@Override
		public void entry() {
			TestClass cls = Action.create(TestClass.class, 1, 2);
			Action.delete(cls);
		}
	}

	@From(Init.class)
	@To(ObjectCreate.class)
	class InitObjectCreate extends Transition {
	}
}
