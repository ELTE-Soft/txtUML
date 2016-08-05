package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.utils.InstanceCreator;

public class Lifeline<T extends ModelClass> /*extends Descriptor<LifelineWrapper>*/ {
	
	protected T instance;
	
	public Lifeline(Class<T> classToInstaniate,Object... params)
	{
		this.instance = InstanceCreator.create(classToInstaniate, params);
	}
	
	public Lifeline(T instance){
		this.instance = instance;
	}
	
	public T getInstance()
	{		
		return instance;
	}
}
