package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class LifelineWrapper<T extends ModelClass> extends AbstractWrapper<T> {

	InteractionWrapper parent;
	int position;
	String fieldName;
	
	public LifelineWrapper(InteractionWrapper parent, T instance,int position,String fieldName) {
		super(instance);
		this.position = position;
		this.fieldName = fieldName;
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}
	
	public void delete()
	{
		hu.elte.txtuml.api.model.Action.delete(this.getWrapped());
	}
	
	public void start()
	{
		hu.elte.txtuml.api.model.Action.start(this.getWrapped());
	}
	
	public void send(Signal sig,LifelineWrapper<?> wrapper)
	{
		ImprintedListener listener = ( (RuntimeContext)Thread.currentThread() ).getTraceListener();
		listener.addToPattern(this.getWrapped(),sig,this.getWrapped());
		//hu.elte.txtuml.api.model.Action.send(sig, wrapper.getWrapped());
	}
	
	protected <R extends ModelClass> void 
	link(Class<? extends AssociationEnd<T, ?>> leftEnd,Class<? extends AssociationEnd<R, ?>> rightEnd,LifelineWrapper<R> rightObj)
	{
		hu.elte.txtuml.api.model.Action.link(leftEnd, this.getWrapped(),rightEnd , rightObj.getWrapped());
	}
	
	protected <R extends ModelClass> void 
	unlink(Class<? extends AssociationEnd<T, ?>> leftEnd,Class<? extends AssociationEnd<R, ?>> rightEnd,LifelineWrapper<R> rightObj)
	{
		hu.elte.txtuml.api.model.Action.unlink(leftEnd, this.getWrapped(),rightEnd , rightObj.getWrapped());
	}
}
