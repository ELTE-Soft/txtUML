package machine3.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import machine3.x.model.User;
import machine3.x.model.User.Init;
import machine3.x.model.User.NotWorking;
import machine3.x.model.User.WhereToGo;;

public class XUserSMDiagram extends StateMachineDiagram<User>{
	@Column({Init.class, NotWorking.class})
	@Row({NotWorking.class, WhereToGo.class})
	class L extends Layout{}
}
