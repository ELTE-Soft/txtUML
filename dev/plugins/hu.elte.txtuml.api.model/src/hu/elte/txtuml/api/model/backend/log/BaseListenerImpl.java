package hu.elte.txtuml.api.model.backend.log;

class BaseListenerImpl {

	private final ExecutorLog owner;
	
	public BaseListenerImpl(ExecutorLog owner) {
		this.owner = owner;
	}
	
	/**
	 * Prints a simple message.
	 */
	protected final void out(String message) {
		owner.out(message);
	}
	
	/**
	 * Prints an error message.
	 */
	protected final void err(String message) {
		owner.err(message);		
	}
	
	/**
	 * Prints a warning message.
	 */
	protected final void warn(String message) {
		owner.warn(message);
	}
	
}
