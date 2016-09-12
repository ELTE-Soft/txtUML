package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import java.util.LinkedList;
import java.util.Queue;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.AbstractWrapper;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.execstrats.ExecutionStrategy;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public abstract class CombinedFragmentWrapper extends AbstractWrapper<String>
		implements BaseCombinedFragmentWrapper, BaseFragmentWrapper {

	InteractionWrapper parentInteraction;
	BaseCombinedFragmentWrapper parent;
	CombinedFragmentType type;
	Queue<BaseFragmentWrapper> containedFragments;
	protected BaseFragmentWrapper fragmentInProgress;
	protected boolean overlapWarning;
	protected ExecutionStrategy strategy = null;

	public CombinedFragmentWrapper(BaseCombinedFragmentWrapper parent, InteractionWrapper parentInteraction,
			CombinedFragmentType type, String fragmentName, boolean overlapWarning) {
		super(fragmentName);

		this.parentInteraction = parentInteraction;
		this.type = type;
		this.containedFragments = new LinkedList<BaseFragmentWrapper>();
		this.overlapWarning = overlapWarning;
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	public static <T extends CombinedFragmentWrapper> T createWrapper(BaseCombinedFragmentWrapper parent,
			InteractionWrapper parentInteraction, CombinedFragmentType type, String fragmentName) {
		switch (type) {
		case STRICT:
			return (T) new StrictFragmentWrapper(parent, parentInteraction, fragmentName);
		case LOOP:
		case ALT:
			//return (T) new EmptyFragmentWrapper(parent, parentInteraction, fragmentName);
		case SEQ:
			return (T) new SEQFragmentWrapper(parent, parentInteraction, fragmentName);
		}

		return null;
	}

	public void openFragment(CombinedFragmentType type, String fragmentName) {
		if (fragmentInProgress != null) {
			((CombinedFragmentWrapper) this.fragmentInProgress).openFragment(type, fragmentName);
		} else {
			fragmentInProgress = CombinedFragmentWrapper.createWrapper(this, parentInteraction, type, fragmentName);
		}
	}

	public void closeFragment() {
		if (((CombinedFragmentWrapper) fragmentInProgress).hasOpenFragment()) {
			((CombinedFragmentWrapper) fragmentInProgress).closeFragment();
		} else {
			containedFragments.add(this.fragmentInProgress);
			fragmentInProgress = null;
		}
	}

	protected boolean hasOpenFragment() {
		return this.fragmentInProgress != null;
	}

	@Override
	public String getStringRepresentation() {
		return "CombinedFragment: Type:" + this.type + " name:" + this.getWrapped();
	}

	@Override
	public hu.elte.txtuml.api.model.seqdiag.Runtime getRuntime() {
		return RuntimeContext.getCurrentExecutorThread().getRuntime();
	}

	@Override
	public Queue<BaseFragmentWrapper> getFragments(BaseFragmentWrapper wrapper) {
		return this.containedFragments;
	}

	public int size() {
		int retVal = 0;

		for (BaseFragmentWrapper containedFragment : this.containedFragments) {
			retVal += containedFragment.size();
		}
		return retVal;
	}

	@Override
	public void add(BaseFragmentWrapper fragment) {
		if (hasOpenFragment()) {
			((BaseCombinedFragmentWrapper) fragmentInProgress).add(fragment);
		} else {
			containedFragments.add(fragment);
		}
	}

	@Override
	public boolean hasOverlapWarning() {
		return overlapWarning;
	}

	public BaseCombinedFragmentWrapper getParent() {
		return parent;
	}

	@Override
	public boolean checkMessageSendToPattern(BaseMessageWrapper message) {
		return strategy.checkMessageSendToPattern(containedFragments, message);
	}

	@Override
	public boolean containsMessage(BaseMessageWrapper message) {
		return strategy.containsMessage(containedFragments, message);
	}
}
