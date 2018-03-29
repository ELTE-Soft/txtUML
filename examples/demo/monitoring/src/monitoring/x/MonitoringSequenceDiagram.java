package monitoring.x;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Position;
import hu.elte.txtuml.api.model.seqdiag.Sequence;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Alert;
import monitoring.x.model.Close;
import monitoring.x.model.Error;
import monitoring.x.model.OK;
import monitoring.x.model.Read;
import monitoring.x.model.ReadError;
import monitoring.x.model.ResourceMonitor;
import monitoring.x.model.ToAggregator;
import monitoring.x.model.ToAlert;
import monitoring.x.model.Write;
import monitoring.x.model.WriteError;

public class MonitoringSeq extends SequenceDiagram{
	
	@Position(1)
	Alert alert;
	
	@Position(2)
	ResourceMonitor monitor;
	
	@Position(3)
	Aggregator aggregator;
	
	@Override
	public void initialize() {
		monitor = Action.create(ResourceMonitor.class);
		aggregator = Action.create(Aggregator.class);
		alert = Action.create(Alert.class, 3);
		Action.link(ToAggregator.rmonitor.class, monitor, ToAggregator.aggregator.class, aggregator);
		Action.link(ToAlert.rmonitor.class, monitor, ToAlert.alert.class, alert);
		Action.start(monitor);
		Action.start(aggregator);
		Action.start(alert);
	}
	
	@Override
	public void run() {
		Sequence.fromActor(new Read(), monitor);
		Sequence.send(monitor, new OK(), alert);
		
		for (int i = 0; i < 4; ++i) {
			Sequence.fromActor(new Write(), monitor);
			Sequence.send(monitor, new WriteError(), aggregator);
			Sequence.send(monitor, new Error(), alert);
		}
		
		Sequence.fromActor(new Close(), monitor);
		Sequence.send(monitor, new OK(), alert);
		
		Sequence.fromActor(new Write(), monitor);
		Sequence.send(monitor, new OK(), alert);
		Sequence.fromActor(new Write(), monitor);
		Sequence.send(monitor, new OK(), alert);
		
		for (int i = 0; i < 4; ++i) {
			Sequence.fromActor(new Read(), monitor);
			Sequence.send(monitor, new ReadError(), aggregator);
			Sequence.send(monitor, new Error(), alert);
		}
		
		Sequence.fromActor(new Write(), monitor);
		Sequence.send(monitor, new OK(), alert);
	}

}
