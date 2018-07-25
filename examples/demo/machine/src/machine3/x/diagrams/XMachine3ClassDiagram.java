package machine3.x.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;
import machine3.x.model.Machine;
import machine3.x.model.User;

public class XMachine3ClassDiagram extends ClassDiagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
