package monitoring.x;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Alert;
import monitoring.x.model.ResourceMonitor;

public class ClassDiagram extends Diagram {
	@North(val = ResourceMonitor.class, from = {Aggregator.class, Alert.class})
	@Row({Aggregator.class, Alert.class})
	class MonitoringLayout extends Layout {}
}
