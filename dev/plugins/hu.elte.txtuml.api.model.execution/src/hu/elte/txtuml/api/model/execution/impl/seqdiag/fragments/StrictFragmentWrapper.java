package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats.ExecutionStrategyStrict;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

public class StrictFragmentWrapper extends CombinedFragmentWrapper {

	public StrictFragmentWrapper(BaseCombinedFragmentWrapper parent, InteractionWrapper parentInteraction,
			String fragmentName) {
		super(parent, parentInteraction, CombinedFragmentType.STRICT, fragmentName, true);
		
		strategy = new ExecutionStrategyStrict();
	}
}
