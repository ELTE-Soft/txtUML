package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;

public class SEQFragmentWrapper extends CombinedFragmentWrapper {

	public SEQFragmentWrapper(InteractionWrapper parent, String fragmentName) {
		super(parent, CombinedFragmentType.SEQ, fragmentName, false);
	}

	@Override
	public boolean checkMessageSendToPattern(BaseMessageWrapper message) {
		BaseFragmentWrapper fragment = containedFragments.peek();
		if (fragment instanceof BaseCombinedFragmentWrapper) {
			return ((BaseCombinedFragmentWrapper) fragment).checkMessageSendToPattern(message);
		} else {
			BaseMessageWrapper required = (BaseMessageWrapper) fragment;
			if (!required.equals(message)) {
				return false;
			}
			containedFragments.remove();
			return true;
		}
	}
}
