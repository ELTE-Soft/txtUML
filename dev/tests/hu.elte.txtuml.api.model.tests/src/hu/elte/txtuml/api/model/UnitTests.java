package hu.elte.txtuml.api.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.assocends.AllAssociationEndTests;
import hu.elte.txtuml.api.model.error.deletion.AllDeletionErrorTests;
import hu.elte.txtuml.api.model.error.multiplicity.AllAssociationEndErrorTests;
import hu.elte.txtuml.api.model.error.other.AllOtherErrorTests;
import hu.elte.txtuml.api.model.error.statemachine.AllStateMachineErrorTests;
import hu.elte.txtuml.api.model.statemachine.AllStateMachineTests;

@RunWith(Suite.class)
@SuiteClasses({ AllPackagePrivateTests.class, AllAssociationEndTests.class,
		AllStateMachineTests.class, AllDeletionErrorTests.class,
		AllStateMachineErrorTests.class, AllAssociationEndErrorTests.class,
		AllOtherErrorTests.class })
public class UnitTests {
}
