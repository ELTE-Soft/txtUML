package hu.elte.txtuml.api.model.execution.seqdiag.error;

import java.util.Optional;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
@SuppressWarnings("serial")
public class MessageError extends RuntimeException {
	private final Message message;

	public MessageError(Message message, String concreteError) {
		super(createErrorMessage(message, concreteError));
		this.message = message;
	}

	public Optional<Boolean> isFromActor() {
		return message.isFromActor();
	}

	public ModelClass getSender() {
		return message.getSenderOrNull();
	}

	public Signal getSignal() {
		return message.getWrapped();
	}

	public ModelClass getTarget() {
		return message.getTarget();
	}

	private static String createErrorMessage(Message message, String concreteError) {
		StringBuilder builder = new StringBuilder("The model diverged from the Sequence-diagram specified behaviour.");
		builder.append("\n ").append(concreteError);
		builder.append(message);
		return builder.append("\n").toString();

	}

}
