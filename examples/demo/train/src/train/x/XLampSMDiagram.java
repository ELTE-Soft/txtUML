package train.x;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import train.x.model.Lamp;
import train.x.model.Lamp.Dark;
import train.x.model.Lamp.Init;
import train.x.model.Lamp.Light;

public class XLampSMDiagram extends StateMachineDiagram<Lamp>{
	
	@Above(from=Light.class, val = Init.class)
	@Left(from=Dark.class, val=Light.class)
	class L extends Layout{}
}
