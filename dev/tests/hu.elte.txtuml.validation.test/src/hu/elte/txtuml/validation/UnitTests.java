package hu.elte.txtuml.validation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ModelTest.class, SequenceDiagramTest.class })
public class UnitTests {
}
