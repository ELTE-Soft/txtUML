package hu.elte.txtuml.validation.model;

import java.text.MessageFormat;

import org.eclipse.jdt.core.dom.ASTNode;

import hu.elte.txtuml.validation.common.MessageLoader;
import hu.elte.txtuml.validation.common.SourceInfo;

/**
 * An enumeration to create different problems.
 * <p>
 * Messages are loaded from {@link Messages#LOADER}, after converting names to
 * camel case and appending the "_message" string to them.
 */
public enum ModelErrors {

	// general problems

	/**
	 * <b>Required message params:</b> String nodeString
	 */
	INVALID_CHILDREN_ELEMENT {
		public ModelValidationError create(SourceInfo sourceInfo, ASTNode node, Object... messageParams) {
			return create(sourceInfo, node,
					MessageFormat.format(message, messageParams[0], node.getClass().getSimpleName()));
		}
	},

	INVALID_TYPE_IN_MODEL, INVALID_MODIFIER, INVALID_TEMPLATE, INVALID_PARAMETER_TYPE,

	// association problems

	WRONG_COMPOSITION_ENDS, WRONG_NUMBER_OF_ASSOCIATION_ENDS, WRONG_TYPE_IN_ASSOCIATION,

	// data type problems

	INVALID_DATA_TYPE_FIELD, MUTABLE_DATA_TYPE_FIELD,

	// model class problems

	INVALID_ATTRIBUTE_TYPE, INVALID_MODEL_CLASS_ELEMENT,

	// signal problems

	INVALID_SIGNAL_CONTENT,

	// state problems

	STATE_METHOD_PARAMETERS, UNKNOWN_CLASS_IN_STATE, UNKNOWN_STATE_METHOD,

	// transition problems

	MISSING_TRANSITION_SOURCE, MISSING_TRANSITION_TARGET, MISSING_TRANSITION_TRIGGER, TRIGGER_ON_INITIAL_TRANSITION,

	TRANSITION_FROM_OUTSIDE, TRANSITION_TO_OUTSIDE,

	TRANSITION_METHOD_PARAMETERS, TRANSITION_METHOD_NON_VOID_RETURN,

	UNKNOWN_TRANSITION_METHOD;

	private static final String SUFFIX = "_message";

	protected final String message = Messages.LOADER
			.getAndRemoveMessage(MessageLoader.convertToCamelCase(name(), SUFFIX));

	/**
	 * If the message of the actual problem requires any other parameters, use
	 * {@link #create}.
	 */
	public ModelValidationError create(SourceInfo sourceInfo, ASTNode node) {
		return create(sourceInfo, node, message);
	}

	public ModelValidationError create(SourceInfo sourceInfo, ASTNode node, Object... messageParams) {
		return create(sourceInfo, node, message);
	}

	protected ModelValidationError create(SourceInfo sourceInfo, ASTNode node, String message) {
		return new ModelValidationError(sourceInfo, node) {

			@Override
			public int getID() {
				return ordinal();
			}

			@Override
			public ModelErrors getType() {
				return ModelErrors.this;
			}

			@Override
			public String getMessage() {
				return message;
			}

		};
	}
}
