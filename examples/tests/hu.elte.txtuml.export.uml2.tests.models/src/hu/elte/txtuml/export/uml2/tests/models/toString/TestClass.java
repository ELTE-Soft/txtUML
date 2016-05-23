package hu.elte.txtuml.export.uml2.tests.models.toString;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	
	public void testToString(TestClass sut) {
		String a = sut.toString();
	}
	
	public void testPrimitiveToString() {
		String a = Integer.toString(3);
	}
	
	public void testAuto() {
		String a = "a" + 3;
	}
}