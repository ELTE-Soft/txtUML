package hu.elte.txtuml.xtxtuml.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.xtxtuml.tests.compiler.XtxtUMLCompilerTests;
import hu.elte.txtuml.xtxtuml.tests.parser.XtxtUMLParserTests;
import hu.elte.txtuml.xtxtuml.tests.scoping.XtxtUMLScopingTests;
import hu.elte.txtuml.xtxtuml.tests.typesystem.XtxtUMLTypeComputerTests;
import hu.elte.txtuml.xtxtuml.tests.validation.XtxtUMLValidationTests;

@RunWith(Suite.class)
@SuiteClasses({ XtxtUMLValidationTests.class, XtxtUMLCompilerTests.class, XtxtUMLParserTests.class,
		XtxtUMLTypeComputerTests.class, XtxtUMLScopingTests.class })
public class UnitTests {
}
