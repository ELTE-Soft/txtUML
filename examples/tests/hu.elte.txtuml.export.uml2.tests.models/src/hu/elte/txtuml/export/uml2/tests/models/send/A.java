package hu.elte.txtuml.export.uml2.tests.models.send;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class A extends ModelClass {

	public void test(A a) {
		Action.send(new Sig(), a.assoc(A_B.B_end.class).one());
	}
	
}