package hu.elte.txtuml.export.uml2.tests.models.ports;

import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;

class TestClass extends ModelClass {
	
	@BehaviorPort
	class BehavPort extends Port<Iface, Interface.Empty> {
	}
	
	class AssemblyPort extends Port<Iface, Interface.Empty> {
	}
	
	class MyInPort extends InPort<Iface> {
	}
	
	class MyOutPort extends InPort<Iface> {
	}
}

interface Iface extends Interface {
}



