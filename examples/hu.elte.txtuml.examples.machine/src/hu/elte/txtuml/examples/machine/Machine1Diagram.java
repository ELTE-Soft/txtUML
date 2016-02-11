package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.model1.Machine;
import hu.elte.txtuml.examples.machine.model1.User;

class Machine1Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
