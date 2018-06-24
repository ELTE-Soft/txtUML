package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class TransitionOutsideOfClass extends ModelClass {

	public class Init extends Initial {
	}

	public class St extends State {
	}

}

class OtherClass extends ModelClass {

	@From(TransitionOutsideOfClass.Init.class)
	@To(TransitionOutsideOfClass.St.class)
	public class Tr extends Transition {
	}
	
}