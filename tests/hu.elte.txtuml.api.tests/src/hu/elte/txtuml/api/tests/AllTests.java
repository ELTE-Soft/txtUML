package hu.elte.txtuml.api.tests;

import hu.elte.txtuml.api.tests.assocends.AllAssociationEndTests;
import hu.elte.txtuml.api.tests.error.deletion.AllDeletionErrorTests;
import hu.elte.txtuml.api.tests.error.multiplicity.AllAssociationEndErrorTests;
import hu.elte.txtuml.api.tests.error.other.AllOtherErrorTests;
import hu.elte.txtuml.api.tests.error.statemachine.AllStateMachineErrorTests;
import hu.elte.txtuml.api.tests.shutdown.AllShutdownTests;
import hu.elte.txtuml.api.tests.statemachine.AllStateMachineTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllShutdownTests.class, AllAssociationEndTests.class,
		AllStateMachineTests.class, AllDeletionErrorTests.class,
		AllStateMachineErrorTests.class, AllAssociationEndErrorTests.class,
		AllOtherErrorTests.class })
public class AllTests {
}
