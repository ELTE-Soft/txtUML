package producer_consumer.j.tests;

import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

public class ProducerConsumerSequenceDiagram extends ProducerConsumerSequenceDiagramBase {

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		Sequence.par(() -> produce(p1), () -> produce(p2), () -> consume(c1), () -> consume(c2), () -> consume(c3));
	}

}
