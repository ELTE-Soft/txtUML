package hu.elte.txtuml.export.uml2.tests.models.equalities;

import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {
	
	public void testEquality() {
		boolean b = "Fdf" == "Str";
	}
	
	public void testInequality() {
		boolean b = "Fdf" != "Str";
	}
	
}
