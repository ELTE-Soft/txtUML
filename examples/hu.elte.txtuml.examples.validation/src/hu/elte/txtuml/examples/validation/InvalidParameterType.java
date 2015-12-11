package hu.elte.txtuml.examples.validation;
import hu.elte.txtuml.api.model.ModelClass;

public class InvalidParameterType extends ModelClass {

	public void f(Object o) {
	}

	public Object g() {
	}

	public void h(A a) {
	}

	public A k() {
	}

}

class A extends ModelClass {
}