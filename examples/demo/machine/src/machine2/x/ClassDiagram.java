package machine2.x;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Right;
import machine2.x.model.Machine;
import machine2.x.model.User;

public class ClassDiagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {
	}
}
