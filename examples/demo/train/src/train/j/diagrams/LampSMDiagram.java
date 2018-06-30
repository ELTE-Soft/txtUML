package train.j.diagrams;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import train.j.model.Lamp;
import train.j.model.Lamp.Dark;
import train.j.model.Lamp.Init;
import train.j.model.Lamp.Light;

public class LampSMDiagram extends StateMachineDiagram<Lamp>{
	
	@Above(from=Light.class, val = Init.class)
	@Left(from=Dark.class, val=Light.class)
	class L extends Layout{}
}
