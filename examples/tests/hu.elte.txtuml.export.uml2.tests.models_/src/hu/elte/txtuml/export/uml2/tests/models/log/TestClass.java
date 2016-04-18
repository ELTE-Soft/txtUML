package hu.elte.txtuml.export.uml2.tests.models.log;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test() {
		Action.log("Hello");
	}
	
}