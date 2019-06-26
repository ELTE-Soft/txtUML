package monitoring.x.tests;

import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertState;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor;
import static hu.elte.txtuml.api.model.seqdiag.Sequence.assertSend;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
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

public class XMonitoringSequenceDiagram extends SequenceDiagram {

	@Position(1)
	Lifeline<Alert> alert;

	@Position(2)
	Lifeline<ResourceMonitor> monitor;

	@Position(3)
	Lifeline<Aggregator> aggregator;

	@Override
	public void initialize() {
		ResourceMonitor m = Action.create(ResourceMonitor.class);
		Aggregator aggr = Action.create(Aggregator.class);
		Alert a = Action.create(Alert.class, 3);

		Action.link(ToAggregator.rmonitor.class, m, ToAggregator.aggregator.class, aggr);
		Action.link(ToAlert.rmonitor.class, m, ToAlert.alert.class, a);

		monitor = Sequence.createLifeline(m);
		aggregator = Sequence.createLifeline(aggr);
		alert = Sequence.createLifeline(a);

		Action.start(m);
		Action.start(aggr);
		Action.start(a);
	}

	@Override
	public void run() {
		fromActor(new Read(), monitor);
		assertState(monitor, ResourceMonitor.OpenForRead.class);
		assertSend(monitor, new OK(), alert);

		for (int i = 0; i < 4; ++i) {
			fromActor(new Write(), monitor);
			assertSend(monitor, new WriteError(), aggregator);
			assertSend(monitor, new Error(), alert);
		}
		assertState(alert, Alert.Critical.class);

		fromActor(new Close(), monitor);
		assertState(monitor, ResourceMonitor.Closed.class);
		assertSend(monitor, new OK(), alert);

		fromActor(new Write(), monitor);
		assertState(monitor, ResourceMonitor.OpenForWrite.class);
		assertSend(monitor, new OK(), alert);

		fromActor(new Write(), monitor);
		assertSend(monitor, new OK(), alert);

		for (int i = 0; i < 4; ++i) {
			fromActor(new Read(), monitor);
			assertSend(monitor, new ReadError(), aggregator);
			assertSend(monitor, new Error(), alert);
		}
		assertState(alert, Alert.Critical.class);

		fromActor(new Write(), monitor);
		assertSend(monitor, new OK(), alert);
	}

}
