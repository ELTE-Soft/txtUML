package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.CombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseInteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseLifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.Runtime;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public class InteractionWrapper extends AbstractWrapper<Interaction> implements BaseInteractionWrapper {

	private BaseFragmentWrapper fragments;
	private ArrayList<BaseLifelineWrapper<?>> lifelines;
	private BaseMessageWrapper lastMessage = null;

	public InteractionWrapper(Interaction interaction) {
		super(interaction);
		lifelines = new ArrayList<BaseLifelineWrapper<?>>();
		this.fragments = CombinedFragmentWrapper.createWrapper(this, this.getRuntime().getExecutionMode(), "ROOT");
	}

	public void prepare() {
		parseLifelines();
	}

	private void parseLifelines() {
		Field[] fields = this.wrapped.getClass().getFields();
		for (Field lifeline : fields) {
			if (ModelClass.class.isAssignableFrom(lifeline.getType())) {
				this.lifelines.add((LifelineWrapper<ModelClass>) getRuntime().createLifelineWrapper(lifeline));
			}
		}
	}

	BaseLifelineWrapper<?> findLifeline(ModelClass lf) {
		for (BaseLifelineWrapper<?> wrapper : this.lifelines) {
			if (wrapper.getWrapped().equals(lf)) {
				return wrapper;
			}
		}

		return null;
	}

	public void storeMessage(ModelClass from, Signal sig, ModelClass to, boolean isAPI) {
		if (!(this.fragments instanceof BaseCombinedFragmentWrapper)) {
			throw new RuntimeException("Couldn't add fragment, uppermost fragment of the tree is invalid!");
		}

		BaseFragmentWrapper wrapper = new MessageWrapper(from, sig, to, isAPI);
		lastMessage = (BaseMessageWrapper) wrapper;
		((BaseCombinedFragmentWrapper) this.fragments).add(wrapper);

	}

	public void storeFragment(CombinedFragmentType type, String fragmentName) {
		((CombinedFragmentWrapper) this.fragments).openFragment(type, fragmentName);
	}

	public void endFragment() {
		((CombinedFragmentWrapper) this.fragments).closeFragment();
	}

	@Override
	public List<BaseLifelineWrapper<?>> getLifelines() {
		return lifelines;
	}

	@Override
	public BaseFragmentWrapper getFragments() {
		return fragments;
	}

	@Override
	public String getStringRepresentation() {
		return "Interaction:" + this.getClass().getName();
	}

	@Override
	public Runtime getRuntime() {
		return RuntimeContext.getCurrentExecutorThread().getRuntime();
	}

	public BaseMessageWrapper getLastMessage() {
		return this.lastMessage;
	}
}
