package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;

public class TestGeneralizationModel extends Model {
		class A extends ModelClass {
			int a;
		}
		
		class B extends A {}
}
