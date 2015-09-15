package hu.elte.txtuml.api.model.tests.error.deletion;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CannotBeDeletedTest.class, LinkingDeletedTest.class,
		StartingDeletedTest.class, UnlinkingDeletedTest.class,
		SignalArrivingToDeletedTest.class })
public class AllDeletionErrorTests {
}
