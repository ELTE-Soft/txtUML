package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.utils.InstanceCreator;

public class Lifeline<T extends ModelClass> /*extends Descriptor<LifelineWrapper>*/ {
	
	protected ModelClass instance;
	
	public Lifeline(Class<T> classToInstaniate,Object... params)
	{
		this.instance = InstanceCreator.create(classToInstaniate, params);
	}
	
	public Lifeline(ModelClass instance){
		this.instance = instance;
	}
	
	public ModelClass getInstance()
	{		
		return instance;
	}
}
