package hu.elte.txtuml.api.model.execution.testmodel.seqdiag;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class AToB extends Association {
	public class ASide extends End<One<A>> {
	}

	public class BSide extends End<One<B>> {
	}
}
