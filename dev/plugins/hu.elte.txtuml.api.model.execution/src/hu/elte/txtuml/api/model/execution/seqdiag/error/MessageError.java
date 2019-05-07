package hu.elte.txtuml.api.model.execution.seqdiag.error;

import java.util.Optional;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.MessageParticipant;

/**
 * General message sending related error.
 */
@SequenceDiagramRelated
public class MessageError<T extends ModelClass, U extends ModelClass> extends SequenceDiagramProblem {

	private final Message<T, U> message;

	public MessageError(Message<T, U> message, String concreteError, ErrorLevel level) {
		super(createErrorMessage(message, concreteError), level);
		this.message = message;
	}

	public Optional<Boolean> isFromActor() {
		return message.isFromActor();
	}

	public MessageParticipant<T> getSender() {
		return message.getSenderOrNull();
	}

	public Signal getSignal() {
		return message.getWrapped();
	}

	public MessageParticipant<U> getTarget() {
		return message.getTarget();
	}

	private static <T extends ModelClass, U extends ModelClass> String createErrorMessage(Message<T, U> message,
			String concreteError) {
		StringBuilder builder = new StringBuilder("The model diverged from the specified behavior.\n");
		builder.append(concreteError);
		builder.append(message);
		return builder.append("\n").toString();
	}

}
