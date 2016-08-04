package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class InteractionWrapper extends AbstractWrapper<Interaction> {
	
	private ArrayList<CombinedFragmentWrapper> fragments;
	private ArrayList<LifelineWrapper> lifelines;
	private Runtime runtime;
	
	public InteractionWrapper(Interaction interaction)
	{
		super(interaction);
		fragments = new ArrayList<CombinedFragmentWrapper>();
		lifelines = new ArrayList<LifelineWrapper>();
		
		runtime = ( (RuntimeContext)Thread.currentThread() ).getRuntime();
	}
	
	public void finalize()
	{
		getLifelines();
		getCombinedFragments();
	}
	
	private void getCombinedFragments()
	{
		
	}
	
	private void getLifelines()
	{
		Field[] fields = this.wrapped.getClass().getDeclaredFields();
		for(Field lifeline : fields)
		{
			this.lifelines.add(runtime.getLifelineWrapper(lifeline, this));
		}
	}
	
	LifelineWrapper findLifeline(Lifeline<?> lf)
	{
		for(LifelineWrapper wrapper : this.lifelines)
		{
			if(wrapper.getWrapped().equals(lf))
			{
				return wrapper;
			}
		}
		
		return null;
	}
}
