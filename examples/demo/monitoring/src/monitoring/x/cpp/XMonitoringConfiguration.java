package monitoring.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Alert;
import monitoring.x.model.ResourceMonitor;

@Group(contains = { ResourceMonitor.class })
@Group(contains = { Alert.class, Aggregator.class })
public class XMonitoringConfiguration extends Configuration {

}
