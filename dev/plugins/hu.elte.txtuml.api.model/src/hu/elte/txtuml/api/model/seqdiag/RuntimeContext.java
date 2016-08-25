package hu.elte.txtuml.api.model.seqdiag;

public interface RuntimeContext {
	public Runtime getRuntime();

	public InteractionWrapper getInteractionWrapper();

	public BaseSequenceDiagramExecutor getExecutor();

	public static RuntimeContext getCurrentExecutorThread() {
		Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		for (Thread thread : threads) {
			if (thread instanceof RuntimeContext) {
				return (RuntimeContext) thread;
			}
		}

		return null;
	};
}
