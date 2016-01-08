package hu.elte.txtuml.examples.validation;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;

public class InvalidClassInModelClass extends ModelClass {

	class A extends ModelClass {
	}

	class B {
	}

	class C extends Initial {
	}

	@From(C.class)
	@To(E.class)
	class D extends Transition {
	}

	class E extends State {
	}

}