package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TransitionMethodNotCorrect extends ModelClass {

	public class Init extends Initial {
	}

	public class St extends State {
	}

	@From(Init.class)
	@To(St.class)
	public class Tr extends Transition {
		public void effect() {
		}

		public boolean guard() {
			return true;
		}

		public void foo() {
		}

		public boolean guard(Object x) {
			return false;
		}
	}

}
