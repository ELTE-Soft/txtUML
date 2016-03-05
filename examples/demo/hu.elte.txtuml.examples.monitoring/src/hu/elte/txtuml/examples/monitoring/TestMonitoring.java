package hu.elte.txtuml.examples.monitoring;

import java.io.IOException;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.examples.monitoring.model.Aggregator;
import hu.elte.txtuml.examples.monitoring.model.Alert;
import hu.elte.txtuml.examples.monitoring.model.Close;
import hu.elte.txtuml.examples.monitoring.model.PrintReport;
import hu.elte.txtuml.examples.monitoring.model.Read;
import hu.elte.txtuml.examples.monitoring.model.ResourceMonitor;
import hu.elte.txtuml.examples.monitoring.model.ToAggregator;
import hu.elte.txtuml.examples.monitoring.model.ToAlert;
import hu.elte.txtuml.examples.monitoring.model.Write;

public class TestMonitoring {
	public static void main(String[] args) {
		ModelExecutor.Settings.setExecutorLog(false);
		ResourceMonitor monitor = Action.create(ResourceMonitor.class);
		Aggregator aggregator = Action.create(Aggregator.class);
		Alert alert = Action.create(Alert.class, 3);
		Action.link(ToAggregator.rmonitor.class, monitor, ToAggregator.aggregator.class, aggregator);
		Action.link(ToAlert.rmonitor.class, monitor, ToAlert.alert.class, alert);
		Action.start(monitor);
		Action.start(aggregator);
		Action.start(alert);

		System.out.println("Testing monitoring example.");
		System.out.println("\tq - quit");
		System.out.println("\tr - read");
		System.out.println("\tw - write");
		System.out.println("\tc - close");
		System.out.println("\tx - print report");		
		
		try {
			char c = (char)System.in.read();
			while(c != 'q') {
				if(c == 'r') {
					Action.send(new Read(), monitor);
				} else if(c == 'w') {
					Action.send(new Write(), monitor);
				} else if(c == 'c') {
					Action.send(new Close(), monitor);
				} else if(c == 'x') {
					Action.send(new PrintReport(), aggregator);
				}
				c = (char)System.in.read();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelExecutor.shutdown();
	}
}
