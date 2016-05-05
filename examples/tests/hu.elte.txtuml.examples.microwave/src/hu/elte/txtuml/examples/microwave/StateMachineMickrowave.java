package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.microwave.model.Microwave;
import hu.elte.txtuml.examples.microwave.model.Human.Init;
import hu.elte.txtuml.examples.microwave.model.Microwave.Closed;
import hu.elte.txtuml.examples.microwave.model.Microwave.Heating;
import hu.elte.txtuml.examples.microwave.model.Microwave.Opened;

class MickrowaveSM extends StateMachineDiagram<Microwave>{
	@Column({Init.class, Opened.class, Closed.class})
	@East(from=Opened.class, val=Heating.class)
	class L extends Layout{}
}
