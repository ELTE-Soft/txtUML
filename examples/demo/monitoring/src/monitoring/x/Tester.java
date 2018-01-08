package monitoring.x;

import java.io.IOException;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
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

	static ResourceMonitor monitor;
	static Aggregator aggregator;
	static Alert alert;

	static void init() {
		monitor = Action.create(ResourceMonitor.class);
		aggregator = Action.create(Aggregator.class);
		alert = Action.create(Alert.class, 3);
		Action.link(ToAggregator.rmonitor.class, monitor, ToAggregator.aggregator.class, aggregator);
		Action.link(ToAlert.rmonitor.class, monitor, ToAlert.alert.class, alert);
		Action.start(monitor);
		Action.start(aggregator);
		Action.start(alert);
	}

	public static void main(String[] args) {
		ModelExecutor executor = ModelExecutor.create().start(Tester::init);
		
		System.out.println("Testing monitoring example.");
		System.out.println("\tq - quit");
		System.out.println("\tr - read");
		System.out.println("\tw - write");
		System.out.println("\tc - close");
		System.out.println("\tx - print report");

		try {
			char c = (char) System.in.read();
			while (c != 'q') {
				if (c == 'r') {
					API.send(new Read(), monitor);
				} else if (c == 'w') {
					API.send(new Write(), monitor);
				} else if (c == 'c') {
					API.send(new Close(), monitor);
				} else if (c == 'x') {
					API.send(new PrintReport(), aggregator);
				}
				c = (char) System.in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		executor.shutdown();
	}
}
