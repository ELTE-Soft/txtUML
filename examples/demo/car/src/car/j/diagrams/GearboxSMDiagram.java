package car.j.diagrams;

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

	// These objects will be shown in 3 rows
	// The First state will be placed below the Init
	// The Init will be placed below the R
	@Row({ R.class })
	@Row({ Init.class, N.class })
	@Row({ First.class, Second.class })
	@Below(val = First.class, from = Init.class)
	@Below(val = Init.class, from = R.class)
	class GearLay extends Layout {
	}
}
