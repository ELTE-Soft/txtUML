package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.xmodel3.Machine;
import hu.elte.txtuml.examples.machine.xmodel3.User;

class XMachine3ClassDiagram extends ClassDiagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
