package hu.elte.txtuml.api.model.execution.unittests;

import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceBase;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.A;
import hu.elte.txtuml.api.model.execution.testmodel.seqdiag.TestSig;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Proxy;
import hu.elte.txtuml.api.model.seqdiag.Sequence;

@SequenceDiagramRelated
public class NotBoundErrorTest extends SequenceBase {
	Proxy<A> r1;

	@Override
	public void run() {

		r1 = Sequence.createProxy(A.class);
		Sequence.fromActor(new TestSig(), r1);

	}

}
