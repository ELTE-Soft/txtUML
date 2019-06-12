package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.util.WrapperImpl;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.MessageParticipant;

public abstract class AbstractMessage extends WrapperImpl<Signal> {

	public AbstractMessage(Signal signal) {
		super(signal);
	}

	public static <T extends ModelClass, U extends ModelClass> Message<T, U> fromObject(Lifeline<T> sender,
			Signal signal, Lifeline<U> target) {
		return new Message<T, U>((MessageParticipant<T>) sender, signal, (MessageParticipant<U>) target, false);
	}

	public static <T extends ModelClass, U extends ModelClass> Message<T, U> fromActor(Signal signal,
			Lifeline<U> target) {
		return new Message<T, U>(null, signal, (MessageParticipant<U>) target, true);
	}

	public static <T extends ModelClass, U extends ModelClass> Message<T, U> fromUnknown(Signal signal,
			Lifeline<U> target) {
		return new Message<T, U>(null, signal, (MessageParticipant<U>) target, null);
	}

	public static <T extends ModelClass, U extends ModelClass> Message<T, U> from(MessageParticipant<T> sender,
			Signal signal, MessageParticipant<U> target) {
		return new Message<T, U>(sender, signal, target, false);
	}

}
