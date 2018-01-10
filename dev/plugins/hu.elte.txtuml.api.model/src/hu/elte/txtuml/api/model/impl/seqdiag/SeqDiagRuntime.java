package hu.elte.txtuml.api.model.impl.seqdiag;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

public interface SeqDiagRuntime extends ImplRelated {
	InteractionRuntime getCurrentInteraction();

	void setFragmentMode(CombinedFragmentType mode);

	void fragmentModeEnded();
}
