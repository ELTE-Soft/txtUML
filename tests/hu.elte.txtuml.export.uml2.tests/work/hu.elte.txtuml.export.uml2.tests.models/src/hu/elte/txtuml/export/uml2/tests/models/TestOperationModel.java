package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;

public class TestOperationModel extends Model {
		class TestClass extends ModelClass {
			int op1(boolean b, String c) {
				return 0;
			}
			
			void op2() {}
		}
}
