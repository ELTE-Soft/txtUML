package car.j.diagrams;

import car.j.model.Gearbox;
import car.j.model.Gearbox.First;
import car.j.model.Gearbox.Init;
import car.j.model.Gearbox.N;
import car.j.model.Gearbox.R;
import car.j.model.Gearbox.Second;
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
