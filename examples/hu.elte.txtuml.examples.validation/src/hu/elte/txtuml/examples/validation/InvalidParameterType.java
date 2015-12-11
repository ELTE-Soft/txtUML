package hu.elte.txtuml.examples.validation;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.examples.validation.helpers.A;

public class InvalidParameterType extends ModelClass {

	public void f(Object o) {
	}

	public Object g() {
		return null;
	}

	public void h(A a) {
	}

	public A k() {
		return null;
	}

}

