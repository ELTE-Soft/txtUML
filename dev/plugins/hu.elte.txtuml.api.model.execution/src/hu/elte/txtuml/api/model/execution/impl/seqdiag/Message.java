package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.Optional;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.util.WrapperImpl;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.utils.BasedOnFields;

@SequenceDiagramRelated
public class Message extends WrapperImpl<Signal> {
	private final ModelClass target;
	private final ModelClass sender;
	private final Optional<Boolean> fromActor;

	public static Message fromObject(ModelClass sender, Signal signal, ModelClass target) {
		return new Message(sender, signal, target, false);
	}

	public static Message fromActor(Signal signal, ModelClass target) {
		return new Message(null, signal, target, true);
	}

	public static Message fromUnknown(Signal signal, ModelClass target) {
		return new Message(null, signal, target, null);
	}

	protected Message(ModelClass sender, Signal signal, ModelClass target, Boolean fromActor) {
		super(signal);
		this.target = target;
		this.sender = sender;
		this.fromActor = Optional.ofNullable(fromActor);
	}

	public ModelClass getTarget() {
		return target;
	}

	public ModelClass getSenderOrNull() {
		return sender;
	}

	public Optional<Boolean> isFromActor() {
		return fromActor;
	}

	public boolean matches(Message other) {
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
		String from = fromActor.map(is -> is ? "actor" : ("" + sender)).orElse("unknown");
		builder.append("\n from: ").append(from);
		builder.append("\n to: ").append(target);
		builder.append("\n signal: ").append(getWrapped());
		return builder.toString();
	}
}
