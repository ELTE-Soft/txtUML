package hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments;

import java.util.LinkedList;
import java.util.Queue;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.AbstractWrapper;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public abstract class CombinedFragmentWrapper extends AbstractWrapper<String>
		implements BaseCombinedFragmentWrapper, BaseFragmentWrapper {

	InteractionWrapper parent;
	CombinedFragmentType type;
	Queue<BaseFragmentWrapper> containedFragments;
	private BaseFragmentWrapper fragmentInProgress;
	private boolean overlapWarning;

	public CombinedFragmentWrapper(InteractionWrapper parent, CombinedFragmentType type, String fragmentName,
			boolean overlapWarning) {
		super(fragmentName);

		this.parent = parent;
		this.type = type;
		this.containedFragments = new LinkedList<BaseFragmentWrapper>();
		this.overlapWarning = overlapWarning;
	}

	@SuppressWarnings("unchecked")
	public static <T extends CombinedFragmentWrapper> T createWrapper(InteractionWrapper parent,
			CombinedFragmentType type, String fragmentName) {
		switch (type) {
		case STRICT:
			return (T) new StrictFragmentWrapper(parent, fragmentName);
		case PAR:
		case LOOP:
		case ALT:
		case SEQ:
			return (T) new SEQFragmentWrapper(parent, fragmentName);
		}

		return null;
	}

	public void openFragment(CombinedFragmentType type, String fragmentName) {
		if (fragmentInProgress != null) {
			((CombinedFragmentWrapper) this.fragmentInProgress).openFragment(type, fragmentName);
		} else {
			fragmentInProgress = CombinedFragmentWrapper.createWrapper(parent, type, fragmentName);
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

	private boolean hasOpenFragment() {
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
}
