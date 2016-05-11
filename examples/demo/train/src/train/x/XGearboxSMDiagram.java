package train.x;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import train.x.model.Gearbox;
import train.x.model.Gearbox.Backwards;
import train.x.model.Gearbox.Forwards;
import train.x.model.Gearbox.Init;
import train.x.model.Gearbox.Neutral;

public class XGearboxSMDiagram extends StateMachineDiagram<Gearbox> {
	
	@Row({Init.class, Neutral.class})
    @Row({Forwards.class, Backwards.class})
    @Below(val = Forwards.class, from = Init.class)
    class GearLay extends Layout {}
}
