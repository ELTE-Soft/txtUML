package hu.elte.txtuml.export.uml2.tests.models.while_control;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test(int i) {
		while (i > 0) {
			--i;
		}
	}
	
}