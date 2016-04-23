package hu.elte.txtuml.export.uml2.tests.models.for_control;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test(int limit) {
		int sum = 0;
		for (int i = 0; i < limit; ++i) {
			sum += i;
		}
	}
}