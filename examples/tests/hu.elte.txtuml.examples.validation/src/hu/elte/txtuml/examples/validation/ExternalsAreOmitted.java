package hu.elte.txtuml.examples.validation;

import java.io.Serializable;
import java.util.List;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;

@SuppressWarnings("serial")
public class ExternalsAreOmitted extends ModelClass implements Serializable {

	@External
	transient Object x;

	@External
	Object a(List<?> list) {
		return null;
	}

	@External
	class A {

	}

	@ExternalBody
	void b(ModelClass c) {
		Object obj = new Object();
		obj.toString();
	}

}
