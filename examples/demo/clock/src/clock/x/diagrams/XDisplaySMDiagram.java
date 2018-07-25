package clock.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import clock.x.model.Display;
import clock.x.model.Display.Accepting;
import clock.x.model.Display.Init;


public class XDisplaySMDiagram extends StateMachineDiagram<Display> {
	@Column({Init.class, Accepting.class})
	class L extends Layout{}
}
