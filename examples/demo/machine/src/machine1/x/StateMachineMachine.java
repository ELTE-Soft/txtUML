package machine1.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine1.x.model.Machine;
import machine1.x.model.Machine.Init;
import machine1.x.model.Machine.Off;
import machine1.x.model.Machine.On;

class XMachineSM extends StateMachineDiagram<Machine>{
	@Column({Init.class, On.class})
	@Row({On.class, Off.class})
	class L extends Layout{}
}
