package hu.elte.txtuml.api.model.execution.testmodel.seqdiag;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class BToC extends Association {
	public class BSide extends End<One<B>> {
	}

	public class CSide extends End<One<C>> {
	}
}
