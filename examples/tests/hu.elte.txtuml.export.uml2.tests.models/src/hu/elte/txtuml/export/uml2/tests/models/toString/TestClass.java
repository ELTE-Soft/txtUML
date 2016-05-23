package hu.elte.txtuml.export.uml2.tests.models.toString;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	
	public void testToString(TestClass sut) {
		sut.toString();
	}
	
	public void testPrimitiveToString() {
		Integer.toString(3);
	}
	
	public void testAuto() {
		String a = "a" + 3;
	}
}