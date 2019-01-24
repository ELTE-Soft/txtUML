package monitoring.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Alert;
import monitoring.x.model.ResourceMonitor;

@Group(contains = { ResourceMonitor.class }, rate = 0.5)
@Group(contains = { Alert.class, Aggregator.class }, rate = 0.5)
@Runtime(RuntimeType.THREADED)
public class XMonitoringConfiguration extends Configuration {

}
