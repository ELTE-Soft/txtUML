package hu.elte.txtuml.xd.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.xd.tests.parser.XDiagramDefinitionParsingTest;

@RunWith(Suite.class)
@SuiteClasses({ XDiagramDefinitionParsingTest.class })
public class UnitTests {
}
