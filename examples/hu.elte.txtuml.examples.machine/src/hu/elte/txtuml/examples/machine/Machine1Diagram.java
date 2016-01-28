package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.examples.machine.model1.Machine;
import hu.elte.txtuml.examples.machine.model1.User;

class Machine1ClassDiagram extends ClassDiagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
