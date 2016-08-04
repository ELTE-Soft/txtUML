package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.Field;

public class Runtime {
	
	public InteractionWrapper getInteractionWrapper(Interaction interaction)
	{
		return new InteractionWrapper(interaction);
	}
	
	public LifelineWrapper getLifelineWrapper(Field lifeline,InteractionWrapper parent)
	{
		Lifeline<?> data = null;
		try {
			data = (Lifeline<?>)lifeline.get(parent.getWrapped());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(data != null)
			return new LifelineWrapper(parent,data);
		else
			return null;
	}
	
	public CombinedFragmentWrapper getCombinedFragmentWrapper(CombinedFragmentType type,InteractionWrapper parent, String name)
	{
		return new CombinedFragmentWrapper(parent, type, name);
	}
}
