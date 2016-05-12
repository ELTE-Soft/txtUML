package hu.elte.txtuml.export.uml2.tests.models.signal;

import hu.elte.txtuml.api.model.ModelClass;

public class A extends ModelClass {

	public static void test(A inst) {
		
		Sig sig = new Sig(1, true, "test");
		sig.param = "test2";
	}
}