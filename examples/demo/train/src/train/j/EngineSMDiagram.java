package train.j;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import train.j.model.Engine;
import train.j.model.Engine.Init;
import train.j.model.Engine.Stopped;
import train.j.model.Engine.Working;

public class EngineSMDiagram extends StateMachineDiagram<Engine>{
	
	@Above(from=Stopped.class, val = Init.class)
	@Left(from=Working.class, val=Stopped.class)
	class L extends Layout{}
}
