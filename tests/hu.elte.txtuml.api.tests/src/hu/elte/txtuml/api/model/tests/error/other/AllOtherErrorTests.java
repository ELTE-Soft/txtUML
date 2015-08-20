package hu.elte.txtuml.api.model.tests.error.other;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ChangingLockedExecutionTimeMultiplierTest.class,
		UnlinkingNonExistingAssociationTest.class,
		ModelObjectCreationFailureTest.class })
public class AllOtherErrorTests {
}
