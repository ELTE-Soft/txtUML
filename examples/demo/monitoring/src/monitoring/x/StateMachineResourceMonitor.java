package monitoring.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import monitoring.x.model.ResourceMonitor;
import monitoring.x.model.ResourceMonitor.Closed;
import monitoring.x.model.ResourceMonitor.Init;
import monitoring.x.model.ResourceMonitor.OpenForRead;
import monitoring.x.model.ResourceMonitor.OpenForWrite;

class XResourceMonitorSM extends StateMachineDiagram<ResourceMonitor>{
	@Column({Init.class, Closed.class})
	@Row({OpenForRead.class, OpenForWrite.class})
	@North(from=OpenForRead.class, val= Closed.class)
	class L extends Layout{}
}
