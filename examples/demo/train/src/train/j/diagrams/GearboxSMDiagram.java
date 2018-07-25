package train.j.diagrams;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import train.j.model.Gearbox;
import train.j.model.Gearbox.Backwards;
import train.j.model.Gearbox.Forwards;
import train.j.model.Gearbox.Init;
import train.j.model.Gearbox.Neutral;

public class GearboxSMDiagram extends StateMachineDiagram<Gearbox> {
	
	@Row({Init.class, Neutral.class})
    @Row({Forwards.class, Backwards.class})
    @Below(val = Forwards.class, from = Init.class)
    class GearLay extends Layout {}
}
