package clock.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import clock.x.model.Display;
import clock.x.model.Display.Accepting;
import clock.x.model.Display.Init;


class XDisplaySM extends StateMachineDiagram<Display> {
	@Column({Init.class, Accepting.class})
	class L extends Layout{}
}
