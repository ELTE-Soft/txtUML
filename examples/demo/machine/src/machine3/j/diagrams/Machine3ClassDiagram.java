package machine3.j.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;
import machine3.j.model.Machine;
import machine3.j.model.User;

public class Machine3ClassDiagram extends ClassDiagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
