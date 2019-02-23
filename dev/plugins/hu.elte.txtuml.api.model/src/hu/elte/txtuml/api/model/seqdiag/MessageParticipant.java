package hu.elte.txtuml.api.model.seqdiag;

import java.util.Optional;

import hu.elte.txtuml.api.model.ModelClass;

public class MessageParticipant<T extends ModelClass> implements Lifeline<T>, Proxy<T> {

	private Optional<T> participant;
	private Class<T> cls;

	public static <T extends ModelClass> MessageParticipant<T> create(T instance) {
		return new MessageParticipant<>(instance);
	}

	public static <T extends ModelClass> MessageParticipant<T> createUnbound(Class<T> modelClass) {
		return new MessageParticipant<>(modelClass);
	}

	@SuppressWarnings("unchecked")
	public void bindTo(ModelClass instance) {
		if (instance != null && instance.getClass().equals(cls)) {
			participant = Optional.ofNullable((T) instance);
		}
	}

	public Optional<T> getParticipant() {
		return participant;
	}

	public boolean hasParticipant() {
		return participant.isPresent();
	}

	private MessageParticipant(T instance) {
		participant = Optional.ofNullable(instance);
	}

	private MessageParticipant(Class<T> modelClass) {
		cls = modelClass;
		participant = Optional.empty();
	}

}
