package hu.elte.txtuml.export.uml2.tests.models.do_while_control;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test() {
		int x = 42;
		do {
			--x;
		} while (x > 0);
	}
}