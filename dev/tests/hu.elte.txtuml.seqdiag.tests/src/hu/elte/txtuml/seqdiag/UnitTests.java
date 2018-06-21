package hu.elte.txtuml.seqdiag;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.seqdiag.tests.BasicSeqDiagElementTests;
import hu.elte.txtuml.seqdiag.tests.FileManagementTests;
import hu.elte.txtuml.seqdiag.tests.SequenceDiagramFragmentsTests;

@RunWith(Suite.class)
@SuiteClasses({ FileManagementTests.class, BasicSeqDiagElementTests.class, SequenceDiagramFragmentsTests.class })
public class UnitTests {
}
