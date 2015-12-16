package hu.elte.txtuml.examples.validation;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.examples.validation.helpers.A;
import hu.elte.txtuml.examples.validation.helpers.ExternalIface;
import hu.elte.txtuml.examples.validation.helpers.MyDataType;

public class InvalidParameterType extends ModelClass {

	public void f(Object o) {
	}

	public Object g() {
		return null;
	}

	public void h(A a, ExternalIface external, MyDataType datatype) {
	}

	public A k() {
		return null;
	}
	
	public ExternalIface m() {
		return null;
	}
	
	public MyDataType n() {
		return null;
	}

}
