package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.Optional;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.MessageParticipant;
import hu.elte.txtuml.utils.BasedOnFields;

@SequenceDiagramRelated
public class Message<T extends ModelClass, U extends ModelClass> extends AbstractMessage {

	private final MessageParticipant<T> sender;
	private final MessageParticipant<U> target;
	private final Optional<Boolean> fromActor;

	protected Message(MessageParticipant<T> sender, Signal signal, MessageParticipant<U> target, Boolean fromActor) {
		super(signal);
		this.target = target;
		this.sender = sender;
		this.fromActor = Optional.ofNullable(fromActor);
	}

	public MessageParticipant<U> getTarget() {
		return target;
	}

	public MessageParticipant<T> getSenderOrNull() {
		return sender;
	}

	public Optional<Boolean> isFromActor() {
		return fromActor;
	}

	public <T2 extends ModelClass, U2 extends ModelClass> boolean matches(Message<T2, U2> expected) {
		if (other == this) {
			return true;
		}
		if (other == null) {
			return false;
		}

		if (fromActor.isPresent() && other.fromActor.isPresent()) {
			if (!fromActor.get().equals(other.fromActor.get())) {
				return false;
			}
			if (sender != other.sender) { // both is null if from actor
				return false;
			}
		}
		if (target != other.target) {
			return false;
		}

		try {
			return BasedOnFields.equal(getWrapped(), other.getWrapped());
		} catch (IllegalAccessException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String from = fromActor.map(is -> is ? "actor" : ("" + sender.getParticipant().orElse(null))).orElse("unknown");
		builder.append("\n from: ").append(from);
		builder.append("\n to: ")
				.append(target.getParticipant().isPresent() ? target.getParticipant().get() : "unbinded");
		builder.append("\n signal: ").append(getWrapped());
		return builder.toString();
	}
}
