package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import hu.elte.txtuml.api.model.error.seqdiag.PatternMismatchError;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

public class StrictFragmentWrapper extends CombinedFragmentWrapper {

	public StrictFragmentWrapper(InteractionWrapper parent, String fragmentName) {
		super(parent, CombinedFragmentType.STRICT, fragmentName, true);
	}

	@Override
	public boolean checkMessageSendToPattern(BaseMessageWrapper message) {
		BaseFragmentWrapper fragment = containedFragments.peek();

		if (fragment instanceof BaseCombinedFragmentWrapper) {
			if (fragment.size() == 1) {
				containedFragments.remove();
			}

			return ((BaseCombinedFragmentWrapper) fragment).checkMessageSendToPattern(message);
		} else {
			BaseMessageWrapper required = (BaseMessageWrapper) fragment;
			containedFragments.remove();
			if (!required.equals(message)) {
				throw new PatternMismatchError(required.getSender(), message.getSender(), required.getSignal(),
						message.getSignal(), required.getReceiver(), message.getReceiver());
			}
			return true;
		}
	}

}
