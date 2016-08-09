package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class MessageWrapper {
	public ModelClass sender;
	public Signal signal;
	public ModelClass receiver;
	
	public MessageWrapper(ModelClass sender,Signal signal,ModelClass receiver)
	{
		this.sender = sender;
		this.signal = signal;
		this.receiver = receiver;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof MessageWrapper))
		{
			return false;
		}
		else
		{
			MessageWrapper otherWrapper = (MessageWrapper)other;
			
			if(!otherWrapper.sender.equals(sender))
			{
				return false;
			}
			else if(!otherWrapper.receiver.equals(receiver))
			{
				return false;
			}
			else if(!otherWrapper.signal.equals(signal))
			{
				return false;
			}
			
			return true;
		}
	}
}
