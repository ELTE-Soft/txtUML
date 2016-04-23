package hu.elte.txtuml.export.uml2.tests.models.create_and_destroy;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test() {
		TestClass cls = Action.create(TestClass.class, 1, 2);
		Action.delete(cls);
	}
	
}
