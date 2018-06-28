package producer_consumer.x.tests;

import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

public class XProducerSequenceDiagram extends XProducerConsumerSequenceDiagramBase {

	@Override
	@ExecutionMode(ExecMode.LENIENT)
	public void run() {
		Sequence.par(() -> produce(p1), () -> produce(p2));
	}

}
