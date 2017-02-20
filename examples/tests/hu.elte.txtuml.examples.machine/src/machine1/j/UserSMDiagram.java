package machine1.j;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine1.j.model.User;
import machine1.j.model.User.Init;
import machine1.j.model.User.Ready;

public class UserSMDiagram  extends StateMachineDiagram<User>{
	@Column({Init.class, Ready.class})
	class L extends Layout{}
}
