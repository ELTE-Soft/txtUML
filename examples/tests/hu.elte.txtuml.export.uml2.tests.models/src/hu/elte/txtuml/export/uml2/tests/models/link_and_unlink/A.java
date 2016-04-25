package hu.elte.txtuml.export.uml2.tests.models.link_and_unlink;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class A extends ModelClass {
	public static void test(A inst1, B inst2) {
		Action.link(A_B.ThisEnd.class, inst1, A_B.OtherEnd.class, inst2);
		Action.unlink(A_B.ThisEnd.class, inst1, A_B.OtherEnd.class, inst2);
	}
}