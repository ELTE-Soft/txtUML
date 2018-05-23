package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceAssertErr;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceAssertValid;

public class AssertTests {

	@Test
	public void testValid() {
		SequenceAssertValid diag = new SequenceAssertValid();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}

	@Test
	public void testError() {
		SequenceAssertErr diag = new SequenceAssertErr();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(3, executor.getErrors().size());
	}

}
