package hu.elte.txtuml.diagnostics;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.diagnostics.session.InstanceRegisterTest;
import hu.elte.txtuml.diagnostics.session.RuntimeSessionTrackerTest;
import hu.elte.txtuml.diagnostics.session.UniqueInstanceTest;

@RunWith(Suite.class)
@SuiteClasses({
	RuntimeSessionTrackerTest.class,
	UniqueInstanceTest.class,
	InstanceRegisterTest.class
	})
public class UnitTests {

}
