package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.error.seqdiag.ModelRuntimeException;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.Runtime;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;

public class MessageWrapper implements BaseMessageWrapper, BaseFragmentWrapper {
	private ModelClass sender;
	private Signal signal;
	private ModelClass receiver;
	private boolean isAPI;

	public MessageWrapper(ModelClass sender, Signal signal, ModelClass receiver) {
		this(sender, signal, receiver, false);
	}

	public MessageWrapper(ModelClass sender, Signal signal, ModelClass receiver, boolean isAPI) {
		this.sender = sender;
		this.signal = signal;
		this.receiver = receiver;
		this.isAPI = isAPI;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MessageWrapper)) {
			return false;
		} else {

			MessageWrapper otherWrapper = (MessageWrapper) other;

			if ((otherWrapper.isAPI && !this.isAPI) || (!otherWrapper.isAPI && this.isAPI)) {
				return false;
			} else if (!otherWrapper.isAPI && !this.isAPI) {
				if (sender != null && otherWrapper.sender != null && !otherWrapper.sender.runtimeInfo().getIdentifier()
						.equals(sender.runtimeInfo().getIdentifier())) {
					return false;
				}

				if (!otherWrapper.receiver.runtimeInfo().getIdentifier()
						.equals(receiver.runtimeInfo().getIdentifier())) {
					return false;
				}

				if (!signalsEqual(otherWrapper.signal)) {
					return false;
				}
			} else {
				if (!otherWrapper.receiver.runtimeInfo().getIdentifier()
						.equals(receiver.runtimeInfo().getIdentifier())) {
					return false;
				}

				if (!signalsEqual(otherWrapper.signal)) {
					return false;
				}
			}

			return true;
		}
	}

	private boolean signalsEqual(Signal otherSignal) {
		Class<? extends Signal> signalClass = signal.getClass();
		if (!signalClass.isInstance(otherSignal)) {
			return false;
		} else {
			Field[] fieldList = signalClass.getDeclaredFields();
			for (Field attribute : fieldList) {
				try {
					if (!(attribute.get(signal).equals(attribute.get(otherSignal)))) {
						return false;
					}
				} catch (Exception e) {
					throw new ModelRuntimeException(e.getMessage());
				}
			}
			return true;
		}
	}

	@Override
	public Signal getSignal() {
		return this.signal;
	}

	@Override
	public ModelClass getSender() {
		return this.sender;
	}

	@Override
	public ModelClass getReceiver() {
		return this.receiver;
	}

	@Override
	public String getStringRepresentation() {
		return "Message: " + signal.toString();
	}

	@Override
	public Runtime getRuntime() {
		return RuntimeContext.getCurrentExecutorThread().getRuntime();
	}

	@Override
	public int size() {
		return 1;
	}
}
