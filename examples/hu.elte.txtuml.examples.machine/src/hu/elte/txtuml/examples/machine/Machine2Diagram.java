package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.model2.Machine;
import hu.elte.txtuml.examples.machine.model2.User;

class Machine2Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
