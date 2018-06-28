package producer_consumer.j.tests;

import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

public class ProducerSequenceDiagram extends ProducerConsumerSequenceDiagramBase {

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		Sequence.par(() -> produce(p1), () -> produce(p2));
	}

}
