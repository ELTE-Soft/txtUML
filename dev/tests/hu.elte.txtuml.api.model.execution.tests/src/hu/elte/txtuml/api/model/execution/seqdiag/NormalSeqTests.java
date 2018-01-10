package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceDiagramNormalValid;

public class NormalSeqTests {

	@Test
	public void NormalExecStrategyIsValid() {
		SequenceDiagramNormalValid diag = new SequenceDiagramNormalValid();

		SequenceDiagramExecutor executor = new SequenceDiagramExecutor();

		try {
			executor.setInteraction(diag);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		executor.run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}
}
