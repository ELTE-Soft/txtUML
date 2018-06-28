package clock.j.diagrams;

import clock.j.model.classes.Display;
import clock.j.model.classes.Display.Accepting;
import clock.j.model.classes.Display.Init;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class DisplaySMDiagram extends StateMachineDiagram<Display> {
	@Column({Init.class, Accepting.class})
	class L extends Layout{}
}
