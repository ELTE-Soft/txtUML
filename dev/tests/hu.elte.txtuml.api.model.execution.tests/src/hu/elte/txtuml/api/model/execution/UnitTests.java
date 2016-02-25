package hu.elte.txtuml.api.model.execution;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.execution.assocends.AllAssociationEndTests;
import hu.elte.txtuml.api.model.execution.error.deletion.AllDeletionErrorTests;
import hu.elte.txtuml.api.model.execution.error.multiplicity.AllAssociationEndErrorTests;
import hu.elte.txtuml.api.model.execution.error.other.AllOtherErrorTests;
import hu.elte.txtuml.api.model.execution.error.statemachine.AllStateMachineErrorTests;
import hu.elte.txtuml.api.model.execution.shutdown.ShutdownWithTenMessagesTest;
import hu.elte.txtuml.api.model.execution.statemachine.AllStateMachineTests;

@RunWith(Suite.class)
@SuiteClasses({ AllPackagePrivateTests.class, AllAssociationEndTests.class,
		AllStateMachineTests.class, AllDeletionErrorTests.class,
		AllStateMachineErrorTests.class, AllAssociationEndErrorTests.class,
		AllOtherErrorTests.class, ShutdownWithTenMessagesTest.class })
public class UnitTests {
}
