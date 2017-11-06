package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats;

import java.util.Iterator;
import java.util.Queue;

import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;

public class ExecutionStrategyLenient implements ExecutionStrategy {

	@Override
	public boolean containsMessage(Queue<BaseFragmentWrapper> containedFragments, BaseMessageWrapper message) {
		boolean containsFragment = false;
		Iterator<BaseFragmentWrapper> it = containedFragments.iterator();
		while (!containsFragment && it.hasNext()) {
			BaseFragmentWrapper fr = it.next();
			if (fr instanceof BaseCombinedFragmentWrapper) {
				containsFragment = ((BaseCombinedFragmentWrapper) fr).containsMessage(message);
			} else {
				containsFragment = message.equals(fr);
			}
		}

		return containsFragment;
	}

	@Override
	public boolean checkMessageSendToPattern(Queue<BaseFragmentWrapper> containedFragments,
			BaseMessageWrapper message) {
		boolean foundMessage = false;
		boolean result = false;
		Iterator<BaseFragmentWrapper> it = containedFragments.iterator();
		while (!foundMessage && it.hasNext()) {
			BaseFragmentWrapper fr = it.next();
			if (fr instanceof BaseCombinedFragmentWrapper) {
				result = ((BaseCombinedFragmentWrapper) fr).checkMessageSendToPattern(message);
				foundMessage = true;
			} else {
				if (message.equals(fr)) {
					it.remove();
				}
				result = true;
				foundMessage = true;
			}
		}

		return result;
	}

}
