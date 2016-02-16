package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.model3.Machine;
import hu.elte.txtuml.examples.machine.model3.User;

class Machine3Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
