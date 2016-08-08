package hu.elte.txtuml.api.model.seqdiag;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class InteractionWrapper extends AbstractWrapper<Interaction> {
	
	private HashMap<String,CombinedFragmentWrapper> fragments;
	private ArrayList<LifelineWrapper<?>> lifelines;
	private Runtime runtime;
	
	public InteractionWrapper(Interaction interaction)
	{
		super(interaction);
		fragments = new HashMap<String,CombinedFragmentWrapper>();
		lifelines = new ArrayList<LifelineWrapper<?>>();
		
		runtime = ( (RuntimeContext)Thread.currentThread() ).getRuntime();
	}
	
	public void finalize()
	{
		getLifelines();
		getCombinedFragments();
	}
	
	private void getCombinedFragments()
	{
		Method[] methods = this.wrapped.getClass().getMethods();
		for(Method combinedFragment : methods)
		{
			Annotation[] annotations = combinedFragment.getDeclaredAnnotations();
			//System.out.println(combinedFragment.getName());
			//System.out.println(annotations.length);
		}
	}
	
	private void getLifelines()
	{
		Field[] fields = this.wrapped.getClass().getDeclaredFields();
		for(Field lifeline : fields)
		{
			this.lifelines.add(runtime.getLifelineWrapper(lifeline, this));
		}
	}
	
	LifelineWrapper<?> findLifeline(Lifeline<?> lf)
	{
		for(LifelineWrapper<?> wrapper : this.lifelines)
		{
			if(wrapper.getWrapped().equals(lf))
			{
				return wrapper;
			}
		}
		
		return null;
	}
}
