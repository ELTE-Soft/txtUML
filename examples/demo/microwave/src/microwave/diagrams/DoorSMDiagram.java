package microwave.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import microwave.model.Door;

public class DoorSMDiagram extends StateMachineDiagram<Door> {
	@Row({ Door.Init.class, Door.Closed.class, Door.Opened.class })
	class L extends Layout {
	}
}
