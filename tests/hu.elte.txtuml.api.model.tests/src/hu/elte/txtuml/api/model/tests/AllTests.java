package hu.elte.txtuml.api.model.tests;

import hu.elte.txtuml.api.model.AllPackagePrivateTests;
import hu.elte.txtuml.api.model.tests.assocends.AllAssociationEndTests;
import hu.elte.txtuml.api.model.tests.error.deletion.AllDeletionErrorTests;
import hu.elte.txtuml.api.model.tests.error.multiplicity.AllAssociationEndErrorTests;
import hu.elte.txtuml.api.model.tests.error.other.AllOtherErrorTests;
import hu.elte.txtuml.api.model.tests.error.statemachine.AllStateMachineErrorTests;
import hu.elte.txtuml.api.model.tests.shutdown.AllShutdownTests;
import hu.elte.txtuml.api.model.tests.statemachine.AllStateMachineTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllPackagePrivateTests.class, AllShutdownTests.class,
		AllAssociationEndTests.class, AllStateMachineTests.class,
		AllDeletionErrorTests.class, AllStateMachineErrorTests.class,
		AllAssociationEndErrorTests.class, AllOtherErrorTests.class })
public class AllTests {
}
