package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class LifelineWrapper<T extends ModelClass> extends AbstractWrapper<Lifeline<T>> {

	InteractionWrapper parent;
	
	public LifelineWrapper(InteractionWrapper parent, Lifeline<T> lifeline) {
		super(lifeline);
	}
	
	public void delete()
	{
		hu.elte.txtuml.api.model.Action.delete(this.getWrapped().getInstance());
	}
	
	public void start()
	{
		hu.elte.txtuml.api.model.Action.start(this.getWrapped().getInstance());
	}
	
	public void send(Signal sig,LifelineWrapper<?> wrapper)
	{
		hu.elte.txtuml.api.model.Action.send(sig, wrapper.getWrapped().getInstance());
	}
	
	protected <R extends ModelClass> void 
	link(Class<? extends AssociationEnd<T, ?>> leftEnd,Class<? extends AssociationEnd<R, ?>> rightEnd,LifelineWrapper<R> rightObj)
	{
		hu.elte.txtuml.api.model.Action.link(leftEnd, this.getWrapped().getInstance(),rightEnd , rightObj.getWrapped().getInstance());
	}
	
	protected <R extends ModelClass> void 
	unlink(Class<? extends AssociationEnd<T, ?>> leftEnd,Class<? extends AssociationEnd<R, ?>> rightEnd,LifelineWrapper<R> rightObj)
	{
		hu.elte.txtuml.api.model.Action.unlink(leftEnd, this.getWrapped().getInstance(),rightEnd , rightObj.getWrapped().getInstance());
	}
}
