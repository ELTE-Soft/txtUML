package hu.elte.txtuml.export.javascript;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.javascript.json.model.cd.ClassDiagramTests;
import hu.elte.txtuml.export.javascript.json.model.smd.SMDiagramTests;

@RunWith(Suite.class)
@SuiteClasses({ ClassDiagramTests.class, SMDiagramTests.class })
public class UnitTests {
}
