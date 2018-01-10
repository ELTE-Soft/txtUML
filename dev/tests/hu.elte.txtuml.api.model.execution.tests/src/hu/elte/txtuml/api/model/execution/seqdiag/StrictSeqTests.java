package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingStrictErr1;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingStrictErr2;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingStrictValid;
import org.junit.Assert;

public class StrictSeqTests {

	@Test
	public void testValidPasses() {
		SequenceMessageSendingStrictValid diag = new SequenceMessageSendingStrictValid();

		SequenceDiagramExecutor executor = new SequenceDiagramExecutor();

		try {
			executor.setInteraction(diag);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		executor.run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}

	@Test
	public void testFirstMessageError() {
		SequenceMessageSendingStrictErr1 diag = new SequenceMessageSendingStrictErr1();

		SequenceDiagramExecutor executor = new SequenceDiagramExecutor();

		try {
			executor.setInteraction(diag);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		executor.run();

		Assert.assertEquals(2, executor.getErrors().size());
	}

	@Test
	public void testLastMessageError() {
		SequenceMessageSendingStrictErr2 diag = new SequenceMessageSendingStrictErr2();

		SequenceDiagramExecutor executor = new SequenceDiagramExecutor();

		try {
			executor.setInteraction(diag);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		executor.run();

		Assert.assertEquals(2, executor.getErrors().size());
	}
}
