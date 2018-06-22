package producer_consumer.x.test;

import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

public class XConsumerSequenceDiagram extends XProducerConsumerSequenceDiagramBase {

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		Sequence.par(() -> consume(c1), () -> consume(c2), () -> consume(c3));
	}

}
