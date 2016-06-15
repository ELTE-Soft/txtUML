package machine3.j;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine3.j.model.User;
import machine3.j.model.User.Init;
import machine3.j.model.User.NotWorking;
import machine3.j.model.User.WhereToGo;

public class UserSMDiagram  extends StateMachineDiagram<User>{
	@Column({Init.class, NotWorking.class})
	@Row({NotWorking.class, WhereToGo.class})
	class L extends Layout{}
}
