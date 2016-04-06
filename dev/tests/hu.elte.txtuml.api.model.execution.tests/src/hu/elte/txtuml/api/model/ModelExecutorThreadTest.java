package hu.elte.txtuml.api.model;

/**
 * Unit tests for ModelExecutorThread. Deadlock is considered a test failure so
 * the tests should be performed using a timeout.
 */
public class ModelExecutorThreadTest {
/* FIXME rewrite model executor thread tests
	@Test(timeout = 1000)
	public void testSend() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();
		ModelClass mock = Mockito.mock(ModelClass.class);

		thread.start();

		Signal signal = new Signal();
		thread.send(mock, null, signal);

		verify(mock).process(null, signal);

		thread.shutdown();
		thread.join();
	}

	@Test(timeout = 1000)
	public void testCheckLowerBoundOfMultiplicity() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();
		ModelClass mockClass = Mockito.mock(ModelClass.class);

		Signal signal = new Signal();
		thread.send(mockClass, null, signal);
		// what the class is does not matter since it is handled in
		// ModelClass.checkLowerBound
		thread.checkLowerBoundOfMultiplicity(mockClass, null);
		
		thread.start();

		InOrder inOrder = Mockito.inOrder(mockClass);
		inOrder.verify(mockClass).checkLowerBound(Matchers.any());
		inOrder.verify(mockClass).process(null, signal);

		thread.shutdown();
		thread.join();
	}

	@Test(timeout = 1000)
	public void testShutdownNow() throws Exception {
		ModelExecutorThread thread = new ModelExecutorThread();

		Region reg = new Region() {
			@Override
			void process(Port<?, ?> port, Signal signal) {
				thread.send(this, port, new Signal());
			}
		};

		thread.start();
		// the execution will never terminate by itself because of the looping
		thread.send(reg, null, new Signal());

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
			thread.send(mock, null, new Signal());
		}

		Assert.assertEquals(false, actionPerformed.value);

		thread.start();
		thread.shutdown();
		thread.join();

		Assert.assertEquals(true, actionPerformed.value);
		verify(mock, times(10)).process(Matchers.isNull(Port.class), Matchers.any());
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
*/
}
