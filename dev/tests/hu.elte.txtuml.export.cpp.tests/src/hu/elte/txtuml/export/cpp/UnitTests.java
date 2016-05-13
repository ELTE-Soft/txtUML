package hu.elte.txtuml.export.cpp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.cpp.wizardz.CompileTests;

@RunWith(Suite.class)
@SuiteClasses({
	CompileTests.class
	})
public class UnitTests {
}

