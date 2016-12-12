package nuclearpower.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import nuclearpower.model.Consumer;
import nuclearpower.model.Consumer.Init;
import nuclearpower.model.Consumer.Off;
import nuclearpower.model.Consumer.On;

public class ConsumerSMDiagram extends StateMachineDiagram<Consumer>{
	
	@Row({Init.class, Off.class, On.class})
	class L extends Layout{}
}
