package monitoring.x;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Alert;
import monitoring.x.model.ResourceMonitor;

<<<<<<< HEAD:examples/demo/hu.elte.txtuml.examples.monitoring/src/hu/elte/txtuml/examples/monitoring/MonitoringDiagram.java
public class MonitoringDiagram extends ClassDiagram {
=======
public class ClassDiagram extends Diagram {
>>>>>>> master:examples/demo/monitoring/src/monitoring/x/ClassDiagram.java
	@North(val = ResourceMonitor.class, from = {Aggregator.class, Alert.class})
	@Row({Aggregator.class, Alert.class})
	class MonitoringLayout extends Layout {}
}
