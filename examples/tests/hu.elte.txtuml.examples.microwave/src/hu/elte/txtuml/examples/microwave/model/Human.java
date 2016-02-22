package hu.elte.txtuml.examples.microwave.model;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class Human extends ModelClass {
	public class Init extends Initial {
	}

	public class Work extends State {
		@Override
		public void entry() {

		}
	}

	@From(Init.class)
	@To(Work.class)
	public class Initialize extends Transition {
	}

}