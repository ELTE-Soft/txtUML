package hu.elte.txtuml.api.model.execution;

/**
 * Thrown by certain methods of the {@link ModelExecutor} interface which cannot
 * be called after that specific model executor has been started. Indicates that
 * the model executor did start before the call of that method.
 */
@SuppressWarnings("serial")
public class LockedModelExecutorException extends RuntimeException {

	public LockedModelExecutorException() {
		super("The accessed model executor is locked. This method cannot be called.");
	}

}