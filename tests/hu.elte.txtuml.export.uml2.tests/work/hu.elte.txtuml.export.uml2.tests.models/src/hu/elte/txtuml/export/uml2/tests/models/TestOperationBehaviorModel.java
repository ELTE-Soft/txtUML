package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;

public class TestOperationBehaviorModel extends Model {
	class TestClass extends ModelClass {
		int a;
		void op() {
			a = 5;
		}
	}
}
