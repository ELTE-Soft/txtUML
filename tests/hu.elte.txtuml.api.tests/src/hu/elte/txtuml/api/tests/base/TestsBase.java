package hu.elte.txtuml.api.tests.base;

import hu.elte.txtuml.api.ModelExecutor;
import hu.elte.txtuml.api.tests.util.MemorizingPrintStream;

import org.junit.Before;

public class TestsBase {

	protected MemorizingPrintStream executorStream = new MemorizingPrintStream();
	protected MemorizingPrintStream executorErrorStream = new MemorizingPrintStream();
	protected MemorizingPrintStream userStream = new MemorizingPrintStream();
	protected MemorizingPrintStream userErrorStream = new MemorizingPrintStream();

	@Before
	public void settings() {
		ModelExecutor.Settings.setUserOutStream(userStream);
		ModelExecutor.Settings.setUserErrorStream(userErrorStream);
		ModelExecutor.Settings.setExecutorOutStream(executorStream);
		ModelExecutor.Settings.setExecutorErrorStream(executorErrorStream);

		ModelExecutor.Settings.setDynamicChecks(true);
		ModelExecutor.Settings.setExecutorLog(true);
	}

	public static void stopModelExecution() {
		stopModelExecution(() -> ModelExecutor.shutdown());
	}
	
	public static void stopModelExecution(Runnable shutdownProcess) {
		Thread currentThread = Thread.currentThread();

		ModelExecutor.addToShutdownQueue(() -> {
			synchronized (currentThread) {
				currentThread.notify();
			}
		});

		shutdownProcess.run();
		
		synchronized (currentThread) {
			try {
				currentThread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
