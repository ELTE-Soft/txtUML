package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.Signal;

public class LifelineWrapper extends AbstractWrapper<Lifeline<?>> {

	InteractionWrapper parent;
	
	public LifelineWrapper(InteractionWrapper parent, Lifeline<?> lifeline) {
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
	
	public void send(Signal sig,LifelineWrapper wrapper)
	{
		hu.elte.txtuml.api.model.Action.send(sig, wrapper.getWrapped().getInstance());
	}

}
