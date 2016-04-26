package hu.elte.txtuml.export.uml2.tests.models.create_and_destroy;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	
	public TestClass(int i, Integer j) {
	}

	public void testCreate() {
		TestClass cls = Action.create(TestClass.class, 1, 2);
	}
	
	public void testDestroy(TestClass cls) {
		Action.delete(cls);
	}
	
}
