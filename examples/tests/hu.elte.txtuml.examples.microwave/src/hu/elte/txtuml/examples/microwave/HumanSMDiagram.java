package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.microwave.model.Human;
import hu.elte.txtuml.examples.microwave.model.Human.Init;
import hu.elte.txtuml.examples.microwave.model.Human.Work;

public class HumanSMDiagram extends StateMachineDiagram<Human>{
	@Column({Init.class, Work.class})
	class L extends Layout{}
}
