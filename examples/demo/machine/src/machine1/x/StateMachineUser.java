package machine1.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine1.x.model.User;
import machine1.x.model.User.Init;
import machine1.x.model.User.Ready;

class XUserSM extends StateMachineDiagram<User>{
	@Column({Init.class, Ready.class})
	class L extends Layout{}
}
