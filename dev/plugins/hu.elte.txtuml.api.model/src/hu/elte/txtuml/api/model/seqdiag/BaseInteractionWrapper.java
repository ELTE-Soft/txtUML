package hu.elte.txtuml.api.model.seqdiag;

import java.util.List;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public interface BaseInteractionWrapper extends BaseWrapper<Interaction> {

	BaseFragmentWrapper getFragments();

	List<BaseLifelineWrapper<?>> getLifelines();

	void storeMessage(ModelClass from, Signal signal, ModelClass to);

	void storeFragment(CombinedFragmentType type, String fragmentName);

	void endFragment();

	void prepare();
}
