package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.tests.assocends.AllAssociationEndTests;
import hu.elte.txtuml.api.model.tests.error.deletion.AllDeletionErrorTests;
import hu.elte.txtuml.api.model.tests.error.multiplicity.AllAssociationEndErrorTests;
import hu.elte.txtuml.api.model.tests.error.other.AllOtherErrorTests;
import hu.elte.txtuml.api.model.tests.error.statemachine.AllStateMachineErrorTests;
import hu.elte.txtuml.api.model.tests.statemachine.AllStateMachineTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllAssociationEndTests.class, AllStateMachineTests.class,
		AllDeletionErrorTests.class, AllStateMachineErrorTests.class,
		AllAssociationEndErrorTests.class, AllOtherErrorTests.class,
		ModelExecutorThreadTest.class })
public class AllTests {
}
