package clock.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import clock.x.model.Hand;
import clock.x.model.Hand.Working;
import clock.x.model.Hand.Init;


public class XHandSMDiagram extends StateMachineDiagram<Hand> {
	@Column({Init.class, Working.class})
	class L extends Layout{}
}
