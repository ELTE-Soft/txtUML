package hu.elte.txtuml.export.uml2.tests.models.start;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	
	public void test(TestClass inst) {
		Action.start(inst);
	}
}