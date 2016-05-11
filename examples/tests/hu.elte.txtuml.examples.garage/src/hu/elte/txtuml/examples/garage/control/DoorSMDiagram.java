package hu.elte.txtuml.examples.garage.control;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.garage.control.model.Door;
import hu.elte.txtuml.examples.garage.control.model.Door.Disabled;
import hu.elte.txtuml.examples.garage.control.model.Door.Enabled;
import hu.elte.txtuml.examples.garage.control.model.Door.InitDoor;

public class DoorSMDiagram extends StateMachineDiagram<Door> {
	@Row({InitDoor.class, Enabled.class, Disabled.class})
	class L extends Layout{}
}
