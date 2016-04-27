package hu.elte.txtuml.export.uml2.tests.models.if_control;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void testIfElse(boolean test) {
		if (test) {
			Action.log("then");
		} else {
			Action.log("else");
		}
	}

	public void testIf(boolean test) {
		if (test) {
			Action.log("then");
		}
	}

	public void testInlineIf(boolean test) {
		if (test)
			Action.log("then");
	}

	public void testInlineIfElse(boolean test) {
		if (test)
			Action.log("then");
		else
			Action.log("else");
	}

	public void testNestedIfs(boolean test1, boolean test2) {
		if (test1) {
			Action.log("then");
		} else if (test2) {
			Action.log("elseif");
		} else {
			Action.log("else");
		}
	}

}
