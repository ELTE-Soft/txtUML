package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceFragmentIF;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceFragmentLoop;

public class FragmentsTest {

	@Test
	public void testIFFragment() {
		/*
		 * IF Case
		 */
		SequenceFragmentIF diag = new SequenceFragmentIF();
		diag.condition = true;

		SequenceDiagramExecutor executor = new SequenceDiagramExecutor();

		try {
			executor.setInteraction(diag);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		executor.run();

		Assert.assertTrue(executor.getErrors().isEmpty());

		/*
		 * Else Case
		 */

		diag.condition = false;
		executor = new SequenceDiagramExecutor();

		try {
			executor.setInteraction(diag);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

		executor.run();

		Assert.assertFalse(executor.getErrors().isEmpty());
		Assert.assertEquals(1, executor.getErrors().size());
	}

	@Test
	public void testLoopFragment() {
		
		SequenceFragmentLoop diag = new SequenceFragmentLoop();

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
