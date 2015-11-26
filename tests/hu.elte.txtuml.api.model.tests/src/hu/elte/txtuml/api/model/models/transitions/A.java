package hu.elte.txtuml.api.model.models.transitions;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class A extends ModelClass {

	public boolean value = true;

	public class Init extends Initial {
	}

	public class S extends State {
		@Override
		public void entry() {
			Action.log("entry");
		}

		@Override
		public void exit() {
			Action.log("exit");
		}
	}

	@From(Init.class)
	@To(S.class)
	public class Initialize extends Transition {
	}

	@From(S.class)
	@To(S.class)
	@Trigger(Sig1.class)
	public class T1 extends Transition {
		@Override
		public void effect() {
			Action.log("T1");
		}
	}

	@From(S.class)
	@To(S.class)
	@Trigger(Sig2.class)
	public class T2 extends Transition {
		@Override
		public void effect() {
			Action.log("T2");
		}
	}

	@From(S.class)
	@To(S.class)
	@Trigger(Sig3.class)
	public class T3 extends Transition {
		@Override
		public boolean guard() {
			return value;
		}

		@Override
		public void effect() {
			Action.log("T3");
			value = false;
		}
	}

	@From(S.class)
	@To(S.class)
	@Trigger(Sig3.class)
	public class T4 extends Transition {
		@Override
		public boolean guard() {
			return !value;
		}

		@Override
		public void effect() {
			Action.log("T4");
			value = true;
		}
	}

}
