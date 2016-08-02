package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;

public class Lifeline<T extends ModelClass> /*extends Descriptor<LifelineWrapper>*/ {
	
	protected ModelClass instance;
	
	public Lifeline(ModelClass instance){
		this.instance = instance;
	}
	
	public ModelClass getInstance()
	{		
		return instance;
	}
}
