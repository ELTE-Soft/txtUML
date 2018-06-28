package clock.j.diagrams;

import clock.j.model.classes.Pendulum;
import clock.j.model.classes.Pendulum.Init;
import clock.j.model.classes.Pendulum.Working;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class PendulumSMDiagram extends StateMachineDiagram<Pendulum> {

	@Column({Init.class, Working.class})
	class L extends Layout{}
}
