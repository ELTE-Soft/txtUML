package hu.elte.txtuml.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.utils.diagrams.LayoutTransformerTest;

@RunWith(Suite.class)
@SuiteClasses({ InstanceCreatorTests.class, LayoutTransformerTest.class })
public class UnitTests {

}
