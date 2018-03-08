package hu.elte.txtuml.examples.performance_test.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class B extends ModelClass {
	public class init extends Initial {
	}

	public class B1 extends State {
	}

	public class B2 extends State {
	}

	public class B3 extends State {
	}

	public class B4 extends State {
	}

	public class finalstate extends State {
		@Override
		public void entry() {
			A a = assoc(AB.a.class).one();
			if (a != null) {
				Action.unlink(AB.a.class, a, AB.b.class, B.this);
				a.childTerminated();
			}
			Action.delete(B.this);
		}
	}

	@From(B.init.class)
	@To(B.B1.class)
	public class init_B1 extends Transition {
	}

	@From(B.B1.class)
	@To(B.B2.class)
	@Trigger(Forward.class)
	public class B1_B2 extends Transition {
	}

	@From(B.B2.class)
	@To(B.B3.class)
	@Trigger(Forward.class)
	public class B2_B3 extends Transition {
	}

	@From(B.B3.class)
	@To(B.B4.class)
	@Trigger(Forward.class)
	public class B3_B4 extends Transition {
	}

	@From(B.B4.class)
	@To(B.finalstate.class)
	@Trigger(Forward.class)
	public class B4_finalState extends Transition {
	}
}