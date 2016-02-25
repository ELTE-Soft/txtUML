package hu.elte.txtuml.api.model.execution.statemachine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TriggerTest.class, EntryExitEffectTest.class, GuardTest.class,
		CompositeStateTest.class, CompositeStateEntryExitTest.class, ChoiceTest.class })
public class AllStateMachineTests {
}
