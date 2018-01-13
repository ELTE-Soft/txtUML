package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.Model;

/**
 * Thrown by certain methods of the {@link ModelExecutor} interface which cannot
 * be called after that specific model executor has been started. Indicates that
 * the model executor did start before the call of that method.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@SuppressWarnings("serial")
public class LockedModelExecutorException extends IllegalStateException {

	public LockedModelExecutorException() {
		super("The accessed model executor is locked. This method cannot be called.");
	}

	public LockedModelExecutorException(String message) {
		super(message);
	}

}