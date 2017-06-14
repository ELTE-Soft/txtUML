package machine1.x;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Right;
import machine1.x.model.Machine;
import machine1.x.model.User;

public class XMachine1ClassDiagram extends ClassDiagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
