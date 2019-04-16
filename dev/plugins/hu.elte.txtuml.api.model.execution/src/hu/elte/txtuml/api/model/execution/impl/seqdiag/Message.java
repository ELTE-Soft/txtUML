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
		// this: actual, other: expected
		if (expected == this) {
			return true;
		}
		if (expected == null) {
			return false;
		}

		if (fromActor.isPresent() && !fromActor.equals(expected.fromActor)) {
			return false;
		}

		if (sender != null && expected.sender != null && expected.sender.hasParticipant()
				&& sender.getParticipant().get() != expected.sender.getParticipant().get()) {
			return false;
		}

		if (target != null && expected.target != null && expected.target.hasParticipant()
				&& target.getParticipant().get() != expected.target.getParticipant().get()) {
			return false;
		}

		boolean areSignalsEqual;
		try {
			areSignalsEqual = BasedOnFields.equal(getWrapped(), expected.getWrapped());
		} catch (IllegalAccessException e) {
			return false;
		}

		if (areSignalsEqual) {

			/* checks if neither of the objects are bound */
			if (sender != null && expected.sender != null && !expected.sender.hasParticipant()
					&& !expected.target.hasParticipant()
					&& sender.getParticipantType().equals(expected.sender.getParticipantType())
					&& target.getParticipantType().equals(expected.target.getParticipantType())) {
				expected.sender.bindTo(sender.getParticipant().get());
				expected.target.bindTo(target.getParticipant().get());
			}
			/*
			 * checks if only one of the objects is bound, and also checks which
			 * one then check up on whether the type of the objects match each
			 * other
			 */
			if (sender != null && expected.sender != null
					&& (expected.sender.hasParticipant() && !expected.target.hasParticipant())
					|| !expected.sender.hasParticipant() && expected.target.hasParticipant()) {

				if (expected.sender.hasParticipant() && !expected.target.hasParticipant()) {
					if (!target.getParticipantType().equals(expected.target.getParticipantType())) {
						areSignalsEqual = false;
					} else {
						expected.target.bindTo(target.getParticipant().get());
					}
				}

				if (!expected.sender.hasParticipant() && expected.target.hasParticipant()) {
					if (!sender.getParticipantType().equals(expected.sender.getParticipantType())) {
						areSignalsEqual = false;
					} else {
						expected.sender.bindTo(sender.getParticipant().get());
					}
				}
			}

			/*
			 * checks whether both objects are bound if (sender != null &&
			 * expected.sender != null && expected.sender.hasParticipant() &&
			 * sender.getParticipantType().equals(expected.sender.
			 * getParticipantType()) && target != null && expected.target !=
			 * null && expected.target.hasParticipant() &&
			 * target.getParticipantType().equals(expected.target.
			 * getParticipantType())) { } put this section in comment because in
			 * this case nothing have to be done at all it only checks
			 */
		}
		/*
		 * exception handling is the only thing (suppose so) remained unsolved
		 * but i think that the logic befits tried to simplify logic but could
		 * not really do changes except commenting out one of the branches
		 * comments now available TEST (How to?)
		 */

		return areSignalsEqual;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String from = fromActor.map(is -> is ? "actor" : ("" + sender.getParticipant().orElse(null))).orElse("unknown");
		builder.append("\n from: ").append(from);
		builder.append("\n to: ")
				.append(target.getParticipant().isPresent() ? target.getParticipant().get() : "unbound");
		builder.append("\n signal: ").append(getWrapped());
		return builder.toString();
	}
}
