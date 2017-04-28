package micro.diagrams;

import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Spacing;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.api.layout.TopMost;
import micro.model.Lamp;
import micro.model.Magnetron;
import micro.model.MicrowaveOven;
import micro.model.MicrowaveOven.Cooking;
import micro.model.MicrowaveOven.FoodInOut;
import micro.model.MicrowaveOven.Interrupted;
import micro.model.MicrowaveOven.Ready;

class MicrowaveClasses extends ClassDiagram {
	@Spacing(0.5)
	@TopMost(MicrowaveOven.class)
	@Row({Lamp.class, Magnetron.class})
	class MicrowaveLayout extends Layout {}
}

class MicrowaveStates extends StateMachineDiagram<MicrowaveOven> {
	@Row({MicrowaveOven.Init.class, Ready.class, FoodInOut.class})
	@Below(from = Ready.class, val = Cooking.class)
	@Below(from = FoodInOut.class, val = Interrupted.class)
	class L extends Layout {}
}

class LampStates extends StateMachineDiagram<Lamp> {
	@Row({Lamp.Init.class, Lamp.Off.class, Lamp.On.class})
	class L extends Layout {}
}

class MagnetronStates extends StateMachineDiagram<Magnetron> {
	@Row({Magnetron.Init.class, Magnetron.Off.class, Magnetron.On.class})
	class L extends Layout {}
}
