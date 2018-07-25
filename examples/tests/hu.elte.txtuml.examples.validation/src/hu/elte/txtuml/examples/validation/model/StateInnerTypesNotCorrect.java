package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class StateInnerTypesNotCorrect extends ModelClass {

	public class Init extends Initial {
		class InnerInit extends Initial {
		}

		class InnerSt extends State {
		}

		@From(InnerInit.class)
		@To(InnerSt.class)
		public class InnerTr extends Transition {
		}
	}

	public class St extends CompositeState {
		class A {

		}

		class InnerInit extends Initial {
		}

		class InnerSt extends State {
		}

		@From(InnerInit.class)
		@To(InnerSt.class)
		public class InnerTr extends Transition {
		}

	}

	@From(Init.class)
	@To(St.class)
	public class Tr extends Transition {
	}

}
