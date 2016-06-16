package clock.j;

import clock.j.model.classes.Hand;
import clock.j.model.classes.Hand.Init;
import clock.j.model.classes.Hand.Working;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class HandSMDiagram extends StateMachineDiagram<Hand> {
	@Column({Init.class, Working.class})
	class L extends Layout{}
}
