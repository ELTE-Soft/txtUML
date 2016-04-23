package hu.elte.txtuml.export.uml2.tests.models.behavport_connect;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Port;
import hu.elte.txtuml.export.uml2.tests.models.behavport_connect.B.B_Port;

public class A extends ModelClass {
	
	public static void test(A inst1, B inst2) {
		Action.connect(A_B_Connector.A_End.class, inst1.port(A_Port.class), A_B_Connector.B_End.class, inst2.port(B_Port.class));
	}
	
	@BehaviorPort
	class A_Port extends Port<IB, IA> {}
}

interface IA extends Interface {
}