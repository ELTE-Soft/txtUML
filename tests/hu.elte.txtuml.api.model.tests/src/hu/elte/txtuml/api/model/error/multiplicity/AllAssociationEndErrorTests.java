package hu.elte.txtuml.api.model.tests.error.multiplicity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UpperBoundOffendedTest.class,
		LowerBoundInitiallyOffendedTest.class,
		LowerBoundOffendedAfterUnlinkTest.class,
		LowerBoundTemporarilyOffendedTest.class })
public class AllAssociationEndErrorTests {
}
