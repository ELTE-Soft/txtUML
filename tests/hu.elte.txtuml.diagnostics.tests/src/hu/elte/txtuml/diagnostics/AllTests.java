package hu.elte.txtuml.diagnostics;

import hu.elte.txtuml.diagnostics.session.InstanceRegisterTest;
import hu.elte.txtuml.diagnostics.session.RuntimeSessionTrackerTest;
import hu.elte.txtuml.diagnostics.session.UniqueInstanceTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	InstanceRegisterTest.class,
	PluginLogWrapperTest.class,
	RuntimeSessionTrackerTest.class,
	UniqueInstanceTest.class
	})
public class AllTests {

}
