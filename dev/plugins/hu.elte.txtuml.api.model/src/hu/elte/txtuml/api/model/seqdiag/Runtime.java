package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Consumer;

import hu.elte.txtuml.api.model.ModelClass;

public class Runtime {

	private InteractionWrapper currentInteraction;
	private ArrayList<FragmentListener> frListeners;

	public Runtime(ArrayList<FragmentListener> fragmentListeners) {
		frListeners = new ArrayList<FragmentListener>(fragmentListeners);
	}

	public InteractionWrapper getInteractionWrapper(Interaction interaction) {
		return new InteractionWrapper(interaction);

	}

	@SuppressWarnings("unchecked")
	public <T extends ModelClass> LifelineWrapper<T> getLifelineWrapper(Field lifeline, InteractionWrapper parent) {
		T data = null;
		try {
			data = (T) lifeline.get(parent.getWrapped());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new LifelineWrapper<T>(parent, data, lifeline.getDeclaredAnnotation(Position.class).value(),
				lifeline.getName());
	}

	public CombinedFragmentWrapper getCombinedFragmentWrapper(CombinedFragmentType type, InteractionWrapper parent,
			String name) {
		return new CombinedFragmentWrapper(parent, type, name);
	}

	public void setCurrentInteraction(InteractionWrapper interaction) {
		this.currentInteraction = interaction;
	}

	public InteractionWrapper getCurrentInteraction() {
		return this.currentInteraction;
	}

	public void fragment(Consumer<FragmentListener> funct) {
		frListeners.forEach(funct);
	}
}
