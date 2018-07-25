package machine3.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine3.x.model.Machine;
import machine3.x.model.Machine.Init;
import machine3.x.model.Machine.Off;
import machine3.x.model.Machine.On;

public class XMachineSMDiagram extends StateMachineDiagram<Machine>{
	@Column({Init.class, On.class})
	@Row({On.class, Off.class})
	class L extends Layout{}
}
