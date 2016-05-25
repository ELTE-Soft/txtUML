package hu.elte.txtuml.export.uml2.tests.models.for_control;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	int sum;
	
	public void test(int limit) {
		for (int i = 0; i < limit; ++i) {
			sum += i;
		}
	}
}