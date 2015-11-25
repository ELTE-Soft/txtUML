package hu.elte.txtuml.examples.machine.xmodel3;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.xmodel3.Machine;
import hu.elte.txtuml.examples.machine.xmodel3.User;

class XMachine3Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
