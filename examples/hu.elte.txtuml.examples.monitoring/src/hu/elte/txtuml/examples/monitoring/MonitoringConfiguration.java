package hu.elte.txtuml.examples.monitoring;

import hu.elte.txtuml.api.platform.Configuration;
import hu.elte.txtuml.api.platform.Group;
import hu.elte.txtuml.api.platform.Multithreading;

import hu.elte.txtuml.examples.monitoring.model.Aggregator;
import hu.elte.txtuml.examples.monitoring.model.Alert;
import hu.elte.txtuml.examples.monitoring.model.ResourceMonitor;

@Group(contains = {ResourceMonitor.class} )
@Group(contains = {Alert.class, Aggregator.class})
@Multithreading(false)
public class MonitoringConfiguration extends Configuration{

}
