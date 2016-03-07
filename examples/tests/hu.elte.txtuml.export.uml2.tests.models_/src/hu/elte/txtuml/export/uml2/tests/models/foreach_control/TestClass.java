package hu.elte.txtuml.export.uml2.tests.models.foreach_control;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TestClass extends ModelClass {
	public class Init extends Initial {
	}

	public class ForEachControl extends State {
		@Override
		public void entry() {
			Collection<Integer> coll = null;

			for (Integer i : coll) {
				Action.create(TestClass.class, i);
			}
		}
	}

	@From(Init.class)
	@To(ForEachControl.class)
	public class InitForEachControl extends Transition {
	}
}