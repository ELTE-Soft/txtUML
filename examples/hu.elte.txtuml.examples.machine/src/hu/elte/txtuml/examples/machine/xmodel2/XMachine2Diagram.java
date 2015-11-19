package hu.elte.txtuml.examples.machine.xmodel2;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.xmodel2.Machine;
import hu.elte.txtuml.examples.machine.xmodel2.User;

class XMachine2Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {
	}
}
