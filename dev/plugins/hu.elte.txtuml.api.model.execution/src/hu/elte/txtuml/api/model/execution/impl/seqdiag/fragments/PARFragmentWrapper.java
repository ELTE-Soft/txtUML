package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats.ExecutionStrategyLenient;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

/*public class PARFragmentWrapper extends CombinedFragmentWrapper {

	public PARFragmentWrapper(BaseCombinedFragmentWrapper parent, InteractionWrapper parentInteraction,
			String fragmentName) {
		super(parent, parentInteraction, CombinedFragmentType.PAR, fragmentName, parent.hasOverlapWarning());

		strategy = ((CombinedFragmentWrapper)this.parent).getExecutionStrategy();
	}

	@Override
	public void add(BaseFragmentWrapper fragment) {
		if ((fragment instanceof BaseMessageWrapper) && hasOpenFragment()) {
			((BaseCombinedFragmentWrapper) fragmentInProgress).add(fragment);
		} else if (fragment instanceof BaseCombinedFragmentWrapper) {
			containedFragments.add(fragment);
		} else {
			throw new RuntimeException("Parallel fragments can't have Action methods as direct children");
		}
	}
}*/
