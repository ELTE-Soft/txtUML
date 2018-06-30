package clock.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import clock.x.model.Pendulum.Working;
import clock.x.model.Pendulum;
import clock.x.model.Pendulum.Init;


public class XPendulumSMDiagram extends StateMachineDiagram<Pendulum> {
	@Column({Init.class, Working.class})
	class L extends Layout{}
}
