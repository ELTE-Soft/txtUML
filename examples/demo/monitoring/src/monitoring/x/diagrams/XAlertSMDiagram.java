package monitoring.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import monitoring.x.model.Alert;
import monitoring.x.model.Alert.Critical;
import monitoring.x.model.Alert.Init;
import monitoring.x.model.Alert.Normal;

public class XAlertSMDiagram extends StateMachineDiagram<Alert>{
	@Column({Init.class, Normal.class, Critical.class})
	class L extends Layout{}
}
