package hu.elte.txtuml.export.uml2.tests.models.behavport_connect;

import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;

public class B extends ModelClass {

	@BehaviorPort
	class B_Port extends Port<IA, IB> {
	}

}

interface IB extends Interface {
}