package machine1.j.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;
import machine1.j.model.Machine;
import machine1.j.model.User;

public class Machine1ClassDiagram extends ClassDiagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
