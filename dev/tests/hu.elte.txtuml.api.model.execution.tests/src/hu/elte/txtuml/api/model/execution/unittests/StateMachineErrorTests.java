package hu.elte.txtuml.api.model.execution.unittests;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.testmodel.erronous.ClassWithMultipleElse;
import hu.elte.txtuml.api.model.execution.testmodel.erronous.ClassWithNoTransitionFromChoice;
import hu.elte.txtuml.api.model.execution.testmodel.erronous.ClassWithOverlappingGuards;
import hu.elte.txtuml.api.model.execution.testmodel.erronous.ClassWithOverlappingGuardsFromChoice;
import hu.elte.txtuml.api.model.execution.testmodel.signals.Sig0;

public class StateMachineErrorTests extends UnitTestsBase {

	private ClassWithOverlappingGuardsFromChoice overlappingFromChoice;
	private ClassWithOverlappingGuards overlapping;
	private ClassWithNoTransitionFromChoice noTransitionFromChoice;
	private ClassWithMultipleElse multipleElse;

	@Test
	public void testOverlappingGuardsFromChoice() {

		executor.run(() -> {
			overlappingFromChoice = new ClassWithOverlappingGuardsFromChoice();
			Action.start(overlappingFromChoice);
			Action.send(new Sig0(), overlappingFromChoice);
		});

		boolean msg1 = hasErrors(x -> x.guardsOfTransitionsAreOverlapping(overlappingFromChoice.new T1(),
				overlappingFromChoice.new T2(), overlappingFromChoice.new C()));
		boolean msg2 = hasErrors(x -> x.guardsOfTransitionsAreOverlapping(overlappingFromChoice.new T2(),
				overlappingFromChoice.new T1(), overlappingFromChoice.new C()));

		Assert.assertTrue(msg1 || msg2);

		assertNoWarnings();
	}

	@Test
	public void testOverlappingGuards() {

		executor.run(() -> {
			overlapping = new ClassWithOverlappingGuards();
			Action.start(overlapping);
			Action.send(new Sig0(), overlapping);
		});

		boolean msg1 = hasErrors(x -> x.guardsOfTransitionsAreOverlapping(overlapping.new T1(), overlapping.new T2(),
				overlapping.new S1()));
		boolean msg2 = hasErrors(x -> x.guardsOfTransitionsAreOverlapping(overlapping.new T2(), overlapping.new T1(),
				overlapping.new S1()));

		Assert.assertTrue(msg1 || msg2);

		assertNoWarnings();
	}

	@Test
	public void testNoTransitionsFromChoice() {

		executor.run(() -> {
			noTransitionFromChoice = new ClassWithNoTransitionFromChoice();
			Action.start(noTransitionFromChoice);
			Action.send(new Sig0(2), noTransitionFromChoice);
		});

		assertErrors(x -> x.noTransitionFromChoice(noTransitionFromChoice.new C()));
		assertNoWarnings();
	}

	@Test
	public void testhMultipleElse() {

		executor.run(() -> {
			multipleElse = new ClassWithMultipleElse();
			Action.start(multipleElse);
			Action.send(new Sig0(), multipleElse);
		});

		assertErrors(x -> x.moreThanOneElseTransitionsFromChoice(multipleElse.new C()));
		assertNoWarnings();
	}

}
