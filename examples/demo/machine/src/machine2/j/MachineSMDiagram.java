package machine2.j;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine2.j.model.Machine;
import machine2.j.model.Machine.Init;
import machine2.j.model.Machine.Off;
import machine2.j.model.Machine.On;

public class MachineSMDiagram extends StateMachineDiagram<Machine>{
	@Column({Init.class, On.class})
	@Row({On.class, Off.class})
	class L extends Layout{}
}
