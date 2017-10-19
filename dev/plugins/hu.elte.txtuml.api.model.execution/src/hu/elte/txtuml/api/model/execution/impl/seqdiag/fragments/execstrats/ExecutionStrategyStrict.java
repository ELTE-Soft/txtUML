package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats;

import java.util.Queue;

import hu.elte.txtuml.api.model.error.seqdiag.PatternMismatchError;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;

public class ExecutionStrategyStrict implements ExecutionStrategy {

	@Override
	public boolean containsMessage(Queue<BaseFragmentWrapper> containedFragments, BaseMessageWrapper message) {
		BaseFragmentWrapper wrapper = containedFragments.peek();
		if (wrapper instanceof BaseCombinedFragmentWrapper) {
			return ((BaseCombinedFragmentWrapper) wrapper).containsMessage(message);
		} else {
			return wrapper.equals(message);
		}
	}

	@Override
	public boolean checkMessageSendToPattern(Queue<BaseFragmentWrapper> containedFragments,
			BaseMessageWrapper message) {
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
