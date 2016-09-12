package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats;

import java.util.Queue;

import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;

public interface ExecutionStrategy {
	public boolean containsMessage(Queue<BaseFragmentWrapper> containedFragments,BaseMessageWrapper message);

	public boolean checkMessageSendToPattern(Queue<BaseFragmentWrapper> containedFragments,BaseMessageWrapper message);
}
