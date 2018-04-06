package hu.elte.txtuml.api.model.execution.seqdiag.error;

import hu.elte.txtuml.api.model.execution.impl.seqdiag.Message;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
@SuppressWarnings("serial")
public class InvalidMessageError extends MessageError {

	public InvalidMessageError(Message message) {
		super(message, "It sent but is should not have:");
	private static final long serialVersionUID = -1655026442976006796L;

	public InvalidMessageError(Message message, ErrorLevel level) {
		super(message, "Invalid message sent: ", level);
	}

	public InvalidMessageError(Message message, String concreteError) {
		super(message, concreteError);
	public InvalidMessageError(Message message, String concreteError, ErrorLevel level) {
		super(message, concreteError, level);
	}

}
