package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceDiagramNormalValid;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class NormalSeqTests {

	@Test
	public void NormalExecStrategyIsValid() {
		SequenceDiagramNormalValid diag = new SequenceDiagramNormalValid();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setInteraction(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}
}
