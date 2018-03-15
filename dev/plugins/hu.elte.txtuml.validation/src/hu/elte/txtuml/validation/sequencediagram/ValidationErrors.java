package hu.elte.txtuml.validation.sequencediagram;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.common.MessageLoader;
import hu.elte.txtuml.validation.common.SourceInfo;
import hu.elte.txtuml.validation.common.ValidationProblem;

/**
 * An enumeration to create different problems.
 * <p>
 * Messages are loaded from {@link Messages#LOADER}, after converting names to
 * camel case and appending the "_message" string to them.
 */
public enum ValidationErrors {

	// general problems

	INVALID_SUPERCLASS, SEND_EXPECTED, INVALID_POSITION, INVALID_LIFELINE_DECLARATION;

	private static final String SUFFIX = "_message";

	protected final String message = Messages.LOADER
			.getAndRemoveMessage(MessageLoader.convertToCamelCase(name(), SUFFIX));

	public ValidationProblem create(SourceInfo sourceInfo, ASTNode node) {
		return create(sourceInfo, node, message);
	}

	private ValidationProblem create(SourceInfo sourceInfo, ASTNode node, String message) {
		return new ValidationProblem(sourceInfo, node) {

			@Override
			public int getID() {
				return ordinal();
			}

			@Override
			public String getMessage() {
				return message;
			}

			@Override
			public String getMarkerType() {
				return SequenceDiagramCompilationParticipant.MARKER_TYPE;
			}

			@Override
			public boolean isError() {
				return true;
			};

		};
	}

}
