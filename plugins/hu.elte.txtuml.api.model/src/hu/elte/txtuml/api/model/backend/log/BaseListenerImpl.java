package hu.elte.txtuml.api.model.backend.log;


class BaseListenerImpl {

	private final ExecutorLog log;
	
	public BaseListenerImpl(ExecutorLog log) {
		this.log = log;
	}
	
	protected final void out(String message) {
		log.out(message);
	}
	
	protected final void err(String message) {
		log.err(message);		
	}
	
	protected final void warn(String message) {
		log.warn(message);
	}
	
}
