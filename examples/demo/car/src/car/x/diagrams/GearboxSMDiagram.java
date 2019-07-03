package car.x.diagrams;

import car.x.model.Gearbox;
import car.x.model.Gearbox.First;
import car.x.model.Gearbox.Init;
import car.x.model.Gearbox.N;
import car.x.model.Gearbox.R;
import car.x.model.Gearbox.Second;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class GearboxSMDiagram extends StateMachineDiagram<Gearbox> {

	@Row({ R.class })
	@Row({ Init.class, N.class })
	@Row({ First.class, Second.class })
	@Below(val = First.class, from = Init.class)
	@Below(val = Init.class, from = R.class)
	class GearLay extends Layout {
	}
}
