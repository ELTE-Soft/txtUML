package microwave.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import microwave.model.MicrowaveOven;
import microwave.model.MicrowaveOven.Cooking;
import microwave.model.MicrowaveOven.Ready;

public class OvenSMDiagram extends StateMachineDiagram<MicrowaveOven> {
	@Row({MicrowaveOven.Init.class, Ready.class, Cooking.class})
	class L extends Layout {}
}
