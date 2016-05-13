package machine2.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine2.x.model.User;
import machine2.x.model.User.Init;
import machine2.x.model.User.Ready;

public class XUserSMDiagram extends StateMachineDiagram<User>{
	@Column({Init.class, Ready.class})
	class L extends Layout{}
}
