package hu.elte.txtuml.api.model;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import hu.elte.txtuml.api.model.util.MutableBoolean;

/**
 * Unit tests for ModelExecutorThread. Deadlock is considered a test failure so
 * the tests should be performed using a timeout.
 */
public class ModelExecutorThreadTest {

	@Test(timeout = 1000)
	public void testSend() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();
		ModelClass mock = Mockito.mock(ModelClass.class);

		thread.start();

		Signal signal = new Signal();
		thread.send(mock, signal);

		verify(mock).process(signal);

		thread.shutdown();
		thread.join();
	}

	@Test(timeout = 1000)
	public void testCheckLowerBoundOfMultiplicity() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();
		ModelClass mockClass = Mockito.mock(ModelClass.class);

		Signal signal = new Signal();
		thread.send(mockClass, signal);
		// what the class is does not matter since it is handled in
		// ModelClass.checkLowerBound
		thread.checkLowerBoundOfMultiplicity(mockClass, null);
		
		thread.start();

		InOrder inOrder = Mockito.inOrder(mockClass);
		inOrder.verify(mockClass).checkLowerBound(Matchers.any());
		inOrder.verify(mockClass).process(signal);

		thread.shutdown();
		thread.join();
	}

	@Test(timeout = 1000)
	public void testShutdownNow() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();

		Region reg = new Region() {
			@Override
			void process(Signal signal) {
				thread.send(this, new Signal());
			}
		};

		thread.start();
		// the execution will never terminate by itself because of the looping
		thread.send(reg, new Signal());

		thread.shutdownImmediately();

		thread.join();
	}

	@Test(timeout = 1000)
	public void testAddToShutdownQueue() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();

		MutableBoolean actionPerformed = new MutableBoolean();
		thread.addToShutdownQueue(() -> actionPerformed.value = true);

		Assert.assertEquals(false, actionPerformed.value);

		thread.start();
		thread.shutdown();
		thread.join();

		Assert.assertEquals(true, actionPerformed.value);
	}

	@Test(timeout = 1000)
	public void testShutdown_multipleMessages() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();

		MutableBoolean actionPerformed = new MutableBoolean();
		thread.addToShutdownQueue(() -> actionPerformed.value = true);

		ModelClass mock = Mockito.mock(ModelClass.class);

		for (int i = 0; i < 10; ++i) {
			thread.send(mock, new Signal());
		}

		Assert.assertEquals(false, actionPerformed.value);

		thread.start();
		thread.shutdown();
		thread.join();

		Assert.assertEquals(true, actionPerformed.value);
		verify(mock, times(10)).process(Matchers.any());
	}

	@Test(timeout = 1000)
	public void testShutdown_noMessages() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();

		MutableBoolean actionPerformed = new MutableBoolean();
		thread.addToShutdownQueue(() -> actionPerformed.value = true);

		thread.start();

		Assert.assertEquals(false, actionPerformed.value);

		thread.shutdown();
		thread.join();

		Assert.assertEquals(true, actionPerformed.value);
	}

}
