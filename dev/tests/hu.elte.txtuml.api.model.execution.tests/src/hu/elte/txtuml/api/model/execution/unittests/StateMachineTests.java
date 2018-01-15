package hu.elte.txtuml.api.model.execution.unittests;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.testmodel.ClassWithChoice;
import hu.elte.txtuml.api.model.execution.testmodel.ClassWithHierarchicalSM;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig1;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig2;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig3;

public class StateMachineTests extends UnitTestsBase {

	private ClassWithChoice choice;
	private ClassWithHierarchicalSM hierarchical;

	@Test
	public void testChoice() {
		executor.run(() -> {
			choice = Action.create(ClassWithChoice.class);
			Action.start(choice);
			Action.send(new Sig0(0), choice);
			Action.send(new Sig0(1), choice);
			Action.send(new Sig0(2), choice);
		});

		assertEvents(x -> {
			x.executionStarted();
			transition(x, choice, choice.new Initialize());
			x.processingSignal(choice, new Sig0(), Optional.empty());
			transition(x, choice, choice.new S1_C());
			transition(x, choice, choice.new T1());
			x.processingSignal(choice, new Sig0(), Optional.empty());
			transition(x, choice, choice.new S1_C());
			transition(x, choice, choice.new T2());
			x.processingSignal(choice, new Sig0(), Optional.empty());
			transition(x, choice, choice.new S1_C());
			transition(x, choice, choice.new T3());
			x.executionTerminated();
		});
		assertNoErrors();
		assertNoWarnings();

	}

	@Test
	public void testCompositeStateEntryExit() {
		executor.run(() -> {
			hierarchical = Action.create(ClassWithHierarchicalSM.class);
			Action.start(hierarchical);
			Action.send(new Sig0(), hierarchical);
			Action.send(new Sig0(), hierarchical);
			Action.send(new Sig1(), hierarchical);
		});

		/*
		 * The access to a field of 'b' is safe, there is no chance for memory
		 * consistency errors here, because the model executor has already
		 * terminated and a synchronization was made when awaiting its
		 * termination.
		 */
		assertListsEqual(Arrays.asList("entry of CS1", "entry of CS1.CS2", "entry of CS1.CS2.S3",
				"exit of CS1.CS2.S3", "exit of CS1.CS2", "exit of CS1"), hierarchical.ownLog);
		assertNoErrors();
		assertNoWarnings();
	}

	@Test
	public void testCompositeState() {
		executor.run(() -> {
			hierarchical = Action.create(ClassWithHierarchicalSM.class);
			Action.start(hierarchical);
			Action.send(new Sig0(), hierarchical);
			Action.send(new Sig0(), hierarchical);
			Action.send(new Sig1(), hierarchical);
		});

		ClassWithHierarchicalSM.CS1 cs1 = hierarchical.new CS1();
		ClassWithHierarchicalSM.CS1.CS2 cs2 = cs1.new CS2();

		assertEvents(x -> {
			x.executionStarted();
			transition(x, hierarchical, hierarchical.new Initialize());
			x.processingSignal(hierarchical, new Sig0(), Optional.empty());
			transition(x, hierarchical, hierarchical.new S1_CS1());
			x.enteringVertex(hierarchical, cs1.new Init());
			transition(x, hierarchical, cs1, cs1.new Initialize());
			x.processingSignal(hierarchical, new Sig0(), Optional.empty());
			transition(x, hierarchical, cs1, cs1.new S2_CS2());
			x.enteringVertex(hierarchical, cs2.new Init());
			transition(x, hierarchical, cs2, cs2.new Initialize());
			x.processingSignal(hierarchical, new Sig1(), Optional.empty());
			x.leavingVertex(hierarchical, cs2.new S3());
			x.leavingVertex(hierarchical, cs2);
			transition(x, hierarchical, hierarchical.new CS1_S1());
			x.executionTerminated();
		});
		assertNoErrors();
		assertNoWarnings();
	}

	@Test
	public void testEntryExit() {
		ModelExecutor executor = ModelExecutor.create();

		executor.run(() -> {
			createAndStartB();
			Action.send(new Sig1(), b);
			Action.send(new Sig2(), b);
		});

		/*
		 * The access to a field of 'b' is safe, there is no chance for memory
		 * consistency errors here, because the model executor has already
		 * terminated and a synchronization was made when awaiting its
		 * termination.
		 */
		assertListsEqual(Arrays.asList("entry", "exit", "entry", "exit", "entry"), b.ownLog);

		assertNoErrors();
		assertNoWarnings();
	}

	@Test
	public void testGuard() {
		executor.run(() -> {
			createAndStartB();
			Action.send(new Sig3(), b);
			Action.send(new Sig3(), b);
			Action.send(new Sig3(), b);
			Action.send(new Sig3(), b);
		});

		assertEvents(x -> {
			x.executionStarted();
			transition(x, b, b.new Initialize());
			x.processingSignal(b, new Sig3(), Optional.empty());
			transition(x, b, b.new T3());
			x.processingSignal(b, new Sig3(), Optional.empty());
			transition(x, b, b.new T4());
			x.processingSignal(b, new Sig3(), Optional.empty());
			transition(x, b, b.new T3());
			x.processingSignal(b, new Sig3(), Optional.empty());
			transition(x, b, b.new T4());
			x.executionTerminated();
		});

		assertNoErrors();
		assertNoWarnings();
	}

	@Test
	public void testTrigger() {
		executor.setLogLevel(LogLevel.TRACE).run(() -> {
			createAndStartB();
			Action.send(new Sig1(), b);
			Action.send(new Sig2(), b);
			Action.send(new Sig1(), b);
			Action.send(new Sig1(), b);
			Action.send(new Sig1(), b);
			Action.send(new Sig2(), b);
		});

		assertEvents(x -> {
			x.executionStarted();
			transition(x, b, b.new Initialize());
			x.processingSignal(b, new Sig1(), Optional.empty());
			transition(x, b, b.new T1());
			x.processingSignal(b, new Sig2(), Optional.empty());
			transition(x, b, b.new T2());
			x.processingSignal(b, new Sig1(), Optional.empty());
			transition(x, b, b.new T1());
			x.processingSignal(b, new Sig1(), Optional.empty());
			transition(x, b, b.new T1());
			x.processingSignal(b, new Sig1(), Optional.empty());
			transition(x, b, b.new T1());
			x.processingSignal(b, new Sig2(), Optional.empty());
			transition(x, b, b.new T2());
			x.executionTerminated();
		});

		assertNoErrors();
		assertNoWarnings();
	}

}
