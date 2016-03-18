package monitoring.x;

import java.io.IOException;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelExecutor;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Alert;
import monitoring.x.model.Close;
import monitoring.x.model.PrintReport;
import monitoring.x.model.Read;
import monitoring.x.model.ResourceMonitor;
import monitoring.x.model.ToAggregator;
import monitoring.x.model.ToAlert;
import monitoring.x.model.Write;

public class Tester {
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
