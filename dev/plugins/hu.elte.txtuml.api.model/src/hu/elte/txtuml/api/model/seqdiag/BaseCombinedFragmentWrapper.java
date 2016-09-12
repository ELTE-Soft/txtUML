package hu.elte.txtuml.api.model.seqdiag;

import java.util.Queue;

public interface BaseCombinedFragmentWrapper extends BaseWrapper<String> {
	Queue<BaseFragmentWrapper> getFragments(BaseFragmentWrapper wrapper);

	boolean checkMessageSendToPattern(BaseMessageWrapper message);

	public void add(BaseFragmentWrapper fragment);

	public boolean hasOverlapWarning();
	
	public boolean containsMessage(BaseMessageWrapper message);
	
	public BaseCombinedFragmentWrapper getParent();
}
