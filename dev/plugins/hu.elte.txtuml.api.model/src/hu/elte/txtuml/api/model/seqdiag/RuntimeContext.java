package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.seqdiag.Runtime;

public interface RuntimeContext {
	public Runtime getRuntime();

	public static RuntimeContext getCurrentExecutorThread() {
		Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		for (Thread thread : threads) {
			if (thread instanceof RuntimeContext) {

				RuntimeContext context = (RuntimeContext) thread;

				if (context.getRuntime().getExecutor().checkThread(thread)) {
					return context;
				}
			}
		}

		return null;
	};
}
