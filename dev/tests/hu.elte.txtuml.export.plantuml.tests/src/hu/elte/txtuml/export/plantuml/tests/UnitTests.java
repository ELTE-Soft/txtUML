package hu.elte.txtuml.export.plantuml.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FileManagementTests.class, BasicSeqDiagElementTests.class, SequenceDiagramFragmentsTests.class })
public class UnitTests {
}
