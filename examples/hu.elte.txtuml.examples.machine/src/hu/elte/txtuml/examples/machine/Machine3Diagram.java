package hu.elte.txtuml.examples.machine;

import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.machine.Machine3Model.*;

class Machine3Diagram extends Diagram {
	@Right(from = Machine.class, val = User.class)
	class MachineLayout extends Layout {} 
}
