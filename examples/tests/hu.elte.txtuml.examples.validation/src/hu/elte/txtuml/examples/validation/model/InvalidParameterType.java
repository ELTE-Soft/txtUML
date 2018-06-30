package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.examples.validation.model.helpers.A;
import hu.elte.txtuml.examples.validation.model.helpers.ExternalClass;
import hu.elte.txtuml.examples.validation.model.helpers.MyDataType;

public class InvalidParameterType extends ModelClass {

	public void f(Object o) {
	}

	public Object g() {
		return null;
	}

	public void h(A a, ExternalClass external, MyDataType datatype) {
	}

	public A k() {
		return null;
	}

	public ExternalClass m() {
		return null;
	}

	public MyDataType n() {
		return null;
	}

}
