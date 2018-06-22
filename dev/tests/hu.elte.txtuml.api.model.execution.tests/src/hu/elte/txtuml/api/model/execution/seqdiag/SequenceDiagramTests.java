package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@RunWith(Suite.class)
@SuiteClasses({ StrictSeqTests.class, LenientTests.class, StateAssertTests.class, FragmentsTests.class })
@SequenceDiagramRelated
public class SequenceDiagramTests {

}
