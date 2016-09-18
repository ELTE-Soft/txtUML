package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({StrictSeqTests.class, NormalSeqTests.class,FragmentsTest.class})
public class SequenceDiagramTests {

}
