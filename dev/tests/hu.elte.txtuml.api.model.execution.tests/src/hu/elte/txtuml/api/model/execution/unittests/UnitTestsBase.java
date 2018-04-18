package hu.elte.txtuml.api.model.execution.unittests;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.execution.CheckLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.B;
import hu.elte.txtuml.api.model.execution.util.ModelExecutionAsserter;
import hu.elte.txtuml.utils.InstanceCreator;

public class UnitTestsBase extends ModelExecutionAsserter {

	protected ModelExecutor executor;

	protected A a;
	protected B b;

	@Before
	public void settings() {
		executor = ModelExecutor.create().setCheckLevel(CheckLevel.OPTIONAL);
		configureFor(executor);
	}

	public static void transition(TraceListener x, ModelClass cls, Transition t) {
		transition(x, cls, cls, t);
	}

	public static void transition(TraceListener x, ModelClass cls, Object enclosing, Transition t) {
		Class<? extends Vertex> from = t.getClass().getAnnotation(From.class).value();
		Class<? extends Vertex> to = t.getClass().getAnnotation(To.class).value();

		x.leavingVertex(cls, InstanceCreator.create(from, enclosing));
		x.usingTransition(cls, t);
		x.enteringVertex(cls, InstanceCreator.create(to, enclosing));
	}

	public static void assertListsEqual(List<?> expecteds, List<?> actuals) {
		Assert.assertEquals(expecteds.size(), actuals.size());
		Iterator<?> it1 = expecteds.iterator();
		Iterator<?> it2 = actuals.iterator();
		while (it1.hasNext()) {
			Assert.assertEquals(it1.next(), it2.next());
		}
	}

	protected void createAAndB() {
		a = new A();
		b = new B();
	}

	protected void createAndStartB() {
		b = new B();
		Action.start(b);
	}

}
