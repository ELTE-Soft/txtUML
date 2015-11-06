package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.machine.Machine1Model.*;

class Machine1Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
