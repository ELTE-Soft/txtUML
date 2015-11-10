package hu.elte.txtuml.api.model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class ModelExecutorThreadTest {

	@Test
	public void testShutdownNow() throws Exception {
		runTestWithTimeout(() -> {

			ModelExecutorThread thread = new ModelExecutorThread();

			Region reg = new Region() {
				@Override
				void process(Signal signal) {
					thread.send(this, new Signal());
				}
			};

			thread.start();
			// the execution will never terminate by itself
			thread.send(reg, new Signal());

			thread.shutdownImmediately();

			thread.join();

		});
	}

	@FunctionalInterface
	public interface TestRunnable {
		void run() throws Exception;
	}

	void runTestWithTimeout(TestRunnable test) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit(() -> {
			test.run();
			return "OK";
		});
		try {
			future.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw e;
		}
	}
	
	

}
