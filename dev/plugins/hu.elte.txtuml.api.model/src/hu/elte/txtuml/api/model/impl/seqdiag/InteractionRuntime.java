package hu.elte.txtuml.api.model.impl.seqdiag;

import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.impl.Wrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.Interaction;

public interface InteractionRuntime extends Wrapper<Interaction>, ImplRelated {
	void storeActorMessage(Signal signal, ModelClass target);

	void storeMessage(Signal signal, ModelClass target, ModelClass sender);

	void storeFragment(CombinedFragmentType type, String fragmentName);

	void endFragment();
}
