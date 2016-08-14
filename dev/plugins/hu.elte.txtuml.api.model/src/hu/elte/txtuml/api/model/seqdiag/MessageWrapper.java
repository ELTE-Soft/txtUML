package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

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
			
			if(sender != null && otherWrapper.sender != null && !otherWrapper.sender.runtimeInfo().getIdentifier().equals( sender.runtimeInfo().getIdentifier() ) )
			{
				return false;
			}
			else if(!otherWrapper.receiver.runtimeInfo().getIdentifier().equals( receiver.runtimeInfo().getIdentifier() ) )
			{
				return false;
			}
			else if(!signalsEqual(otherWrapper.signal))
			{
				return false;
			}
			
			return true;
		}
	}
	
	private boolean signalsEqual(Signal otherSignal)
	{
		Class<? extends Signal> signalClass = signal.getClass();
		if(!signalClass.isInstance(otherSignal))
		{
			return false;
		}
		else
		{
			Field[] fieldList = signalClass.getDeclaredFields();
			for(Field attribute : fieldList)
			{
				try {
					if(! ( attribute.get( signal ).equals( attribute.get( otherSignal ) ) ) )
					{
						return false;
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}
}
