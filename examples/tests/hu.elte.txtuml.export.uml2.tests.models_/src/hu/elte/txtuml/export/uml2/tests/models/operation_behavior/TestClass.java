package hu.elte.txtuml.export.uml2.tests.models.operation_behavior;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	int a;

	void op() {
		a = 5;
	}
}
