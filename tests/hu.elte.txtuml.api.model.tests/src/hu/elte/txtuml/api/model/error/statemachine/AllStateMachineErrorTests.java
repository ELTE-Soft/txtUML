package hu.elte.txtuml.api.model.error.statemachine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OverlappingGuardsTest.class, NoTransitionsFromChoiceTest.class,
		OverlappingGuardsFromChoiceTest.class,
		TwoOrMoreElseFromChoiceTest.class })
public class AllStateMachineErrorTests {
}
