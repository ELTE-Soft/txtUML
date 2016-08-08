package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.model.ModelClass;

public class Runtime {
	
	private InteractionWrapper currentInteraction;
	
	public InteractionWrapper getInteractionWrapper(Interaction interaction)
	{
		return new InteractionWrapper(interaction);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ModelClass> LifelineWrapper<T> getLifelineWrapper(Field lifeline,InteractionWrapper parent)
	{
		Lifeline<T> data = null;
		try {
			data = (Lifeline<T>)lifeline.get(parent.getWrapped());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(data != null)
			return new LifelineWrapper<T>(parent,data);
		else
			return null;
	}
	
	public CombinedFragmentWrapper getCombinedFragmentWrapper(CombinedFragmentType type,InteractionWrapper parent, String name)
	{
		return new CombinedFragmentWrapper(parent, type, name);
	}
	
	public void setCurrentInteraction(InteractionWrapper interaction)
	{
		this.currentInteraction = interaction;
	}
	
	public InteractionWrapper getCurrentInteraction()
	{
		return this.currentInteraction;
	}
}
