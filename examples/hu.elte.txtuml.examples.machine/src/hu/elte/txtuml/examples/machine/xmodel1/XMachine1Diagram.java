package hu.elte.txtuml.examples.machine.xmodel1;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.xmodel1.Machine;
import hu.elte.txtuml.examples.machine.xmodel1.User;

class XMachine1Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
