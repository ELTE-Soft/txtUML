package hu.elte.txtuml.examples.monitoring;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.examples.monitoring.model.Aggregator;
import hu.elte.txtuml.examples.monitoring.model.Alert;
import hu.elte.txtuml.examples.monitoring.model.ResourceMonitor;

public class MonitoringDiagram extends ClassDiagram {
	@North(val = ResourceMonitor.class, from = {Aggregator.class, Alert.class})
	@Row({Aggregator.class, Alert.class})
	class MonitoringLayout extends Layout {}
}
