package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

public class SEQFragmentWrapper extends CombinedFragmentWrapper {

	public SEQFragmentWrapper(BaseCombinedFragmentWrapper parent, InteractionWrapper parentInteraction,
			String fragmentName) {
		super(parent, parentInteraction, CombinedFragmentType.SEQ, fragmentName, false);
	}
}
