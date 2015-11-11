package hu.elte.txtuml.api.model.tests.shutdown;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ShutdownNowTest.class, ShutdownQueueTest.class,
		ShutdownWithZeroMessagesTest.class, ShutdownWithTenMessagesTest.class,
		TimerShutdownWithZeroTimedEventsTest.class,
		TimerShutdownWithTenTimedEventsTest.class })
public class AllShutdownTests {
}
