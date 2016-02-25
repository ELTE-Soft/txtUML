package hu.elte.txtuml.api.model.execution.base;

import org.junit.Before;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelElement;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Region;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.execution.util.ModelExecutionAsserter;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.report.ModelExecutionEventsListener;
import hu.elte.txtuml.utils.InstanceCreator;

public class TestsBase {

	protected final ModelExecutionAsserter executionAsserter = new ModelExecutionAsserter();

	@Before
	public void settings() {
		ModelExecutor.Settings.setDynamicChecks(true);
	}

	public static void transition(ModelExecutionEventsListener x,
			ModelClass cls, Transition t) {
		transition(x, cls, cls, t);
	}

	public static void transition(ModelExecutionEventsListener x,
			Region r, ModelElement enclosing, Transition t) {
		Class<? extends Vertex> from = t.getClass().getAnnotation(From.class)
				.value();
		Class<? extends Vertex> to = t.getClass().getAnnotation(To.class)
				.value();
		
		x.leavingVertex(r, InstanceCreator.create(from, enclosing));
		x.usingTransition(r, t);
		x.enteringVertex(r, InstanceCreator.create(to, enclosing));
	}

	public static void stopModelExecution() {
		ModelExecutor.shutdown();
		ModelExecutor.awaitTermination();
	}

}
