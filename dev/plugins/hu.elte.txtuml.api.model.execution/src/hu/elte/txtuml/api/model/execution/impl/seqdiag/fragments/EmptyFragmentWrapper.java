package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats.ExecutionStrategySeq;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats.ExecutionStrategyStrict;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

/*public class EmptyFragmentWrapper extends CombinedFragmentWrapper {

	public EmptyFragmentWrapper(BaseCombinedFragmentWrapper parent, InteractionWrapper parentInteraction,
			String fragmentName) {
		super(parent, parentInteraction, CombinedFragmentType.EMPTY, fragmentName, parent.hasOverlapWarning());

		BaseCombinedFragmentWrapper firstS = getParent();
		while (firstS != null && strategy == null) {
			if (firstS instanceof SEQFragmentWrapper) {
				strategy = new ExecutionStrategySeq();
			} else if (firstS instanceof StrictFragmentWrapper) {
				strategy = new ExecutionStrategyStrict();
			} else {
				firstS = firstS.getParent();
			}
		}
	}
}*/
