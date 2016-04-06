package hu.elte.txtuml.api.model.execution.error.other;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UnlinkingNonExistingAssociationTest.class, ModelObjectCreationFailureTest.class })
public class AllOtherErrorTests {
}
