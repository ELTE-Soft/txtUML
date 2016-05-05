package clock.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import clock.x.model.Hand;
import clock.x.model.Hand.Working;
import clock.x.model.Hand.Init;


class XHandSM extends StateMachineDiagram<Hand> {
	@Column({Init.class, Working.class})
	class L extends Layout{}
}
