package hu.elte.txtuml.api.model.seqdiag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class InteractionWrapper extends AbstractWrapper<Interaction> {
	
	private HashMap<String,CombinedFragmentWrapper> fragments;
	private ArrayList<MessageWrapper> messages;
	private ArrayList<LifelineWrapper<?>> lifelines;
	private Runtime runtime;
	
	public InteractionWrapper(Interaction interaction)
	{
		super(interaction);
		fragments = new HashMap<String,CombinedFragmentWrapper>();
		lifelines = new ArrayList<LifelineWrapper<?>>();
		messages = new ArrayList<MessageWrapper>();
		
		runtime = ( (RuntimeContext)Thread.currentThread() ).getRuntime();
	}
	
	public ArrayList<LifelineWrapper<?>> getLifelines()
	{
		return this.lifelines;
	}
	
	public ArrayList<MessageWrapper> getMessages()
	{
		return this.messages;
	}
	
	public void prepare()
	{
		parseLifelines();
		parseCombinedFragments();
	}
	
	private void parseCombinedFragments()
	{
		Method[] methods = this.wrapped.getClass().getMethods();
		for(Method combinedFragment : methods)
		{
			Annotation[] annotations = combinedFragment.getDeclaredAnnotations();
			//System.out.println(combinedFragment.getName());
			//System.out.println(annotations.length);
		}
	}
	
	private void parseLifelines()
	{
		Field[] fields = this.wrapped.getClass().getDeclaredFields();
		for(Field lifeline : fields)
		{
			this.lifelines.add(runtime.getLifelineWrapper(lifeline, this));
		}
	}
	
	LifelineWrapper<?> findLifeline(ModelClass lf)
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
	
	public void storeMessage(ModelClass from,Signal sig,ModelClass to)
	{
		this.messages.add(new MessageWrapper(from,sig,to));
	}
}
