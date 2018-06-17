package hu.elte.txtuml.validation.sequencediagram;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.common.MessageLoader;
import hu.elte.txtuml.validation.common.SourceInfo;
import hu.elte.txtuml.validation.common.IValidationErrorType;

/**
 * An enumeration to create different problems.
 * <p>
 * Messages are loaded from {@link Messages#LOADER}, after converting names to
 * camel case and appending the "_message" string to them.
 */
public enum SequenceErrors implements IValidationErrorType {

	// general problems

	INVALID_SUPERCLASS, SEND_EXPECTED, INVALID_POSITION, INVALID_LIFELINE_DECLARATION, INVALID_ACTION_CALL;

	private static final String SUFFIX = "_message";

	protected final String message = Messages.LOADER
			.getAndRemoveMessage(MessageLoader.convertToCamelCase(name(), SUFFIX));

	public SequenceValidationError create(SourceInfo sourceInfo, ASTNode node) {
		return create(sourceInfo, node, message);
	}

	private SequenceValidationError create(SourceInfo sourceInfo, ASTNode node, String message) {
		return new SequenceValidationError(sourceInfo, node) {

			@Override
			public int getID() {
				return ordinal();
			}

			@Override
			public String getMessage() {
				return message;
			}

			@Override
			public IValidationErrorType getType() {
				return SequenceErrors.this;
			}

			@Override
			public int getSourceEnd() {
				return super.getSourceEnd() + 1;
			}

		};
	}

}
