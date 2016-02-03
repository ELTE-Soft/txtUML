package hu.elte.txtuml.xtxtuml.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ XtxtUMLAssociationValidatorTest.class, XtxtUMLCompilerTest.class, XtxtUMLParserTest.class })
public class UnitTests {
}
