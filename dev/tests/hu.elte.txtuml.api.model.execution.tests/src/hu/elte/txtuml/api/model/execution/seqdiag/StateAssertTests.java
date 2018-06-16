package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceStateAssertErr;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceStateAssertValid;

public class StateAssertTests {

	@Test
	public void testValid() {
		SequenceStateAssertValid diag = new SequenceStateAssertValid();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}

	@Test
	public void testError() {
		SequenceStateAssertErr diag = new SequenceStateAssertErr();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(3, executor.getErrors().size());
	}

}
