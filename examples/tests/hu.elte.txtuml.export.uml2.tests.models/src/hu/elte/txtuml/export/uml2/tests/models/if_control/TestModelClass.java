package hu.elte.txtuml.export.uml2.tests.models.if_control;

import hu.elte.txtuml.api.model.ModelClass;

public class TestModelClass extends ModelClass {

	public void testIfElse(boolean test) {
		int res;
		if (test) {
			res = 1;
		} else {
			res = 2;
		}
	}
	
	public void testIf(boolean test) {
		int res;
		if (test) {
			res = 1;
		}
	}
	
	public void testNestedIfs(boolean test1, boolean test2) {
		int res;
		if (test1) {
			res = 1;
		} else if (test2) {
			res = 2;
		} else {
			res = 3;
		}
	}
	
}
