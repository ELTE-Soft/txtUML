package hu.elte.txtuml.api.model.tests.base;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;
import hu.elte.txtuml.api.model.tests.util.MemorizingPrintStream;
import hu.elte.txtuml.api.model.tests.util.ModelExecutionAsserter;
import hu.elte.txtuml.utils.InstanceCreator;

import org.junit.Before;

public class TestsBase {

	protected final MemorizingPrintStream userOutStream = new MemorizingPrintStream();
	protected final ModelExecutionAsserter executionAsserter = new ModelExecutionAsserter();
	
	@Before
	public void settings() {
		ModelExecutor.Settings.setUserOutStream(userOutStream);

		ModelExecutor.Settings.setDynamicChecks(true);
		ModelExecutor.Settings.setExecutorLog(true);
	}

	public static void transition(ModelExecutionEventsListener x, Region r, Transition t) {
		Class<? extends Vertex> from = t.getClass().getAnnotation(From.class).value();
		Class<? extends Vertex> to = t.getClass().getAnnotation(To.class).value();
		
		x.leavingVertex(r, InstanceCreator.createInstance(from));
		x.usingTransition(r, t);
		x.enteringVertex(r, InstanceCreator.createInstance(to));
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
