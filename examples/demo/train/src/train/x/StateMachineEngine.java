package train.x;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import train.x.model.Engine.Stopped;
import train.x.model.Engine.Init;
import train.x.model.Engine.Working;
import train.x.model.Engine;

class XEngineSM extends StateMachineDiagram<Engine>{
	
	@Above(from=Stopped.class, val = Init.class)
	@Left(from=Working.class, val=Stopped.class)
	class L extends Layout{}
}
