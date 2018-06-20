package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceFragmentIF;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceFragmentLoop;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequencePar;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class FragmentsTests {

	@Test
	public void testIFFragment() {
		/*
		 * IF Case
		 */
		SequenceFragmentIF diag = new SequenceFragmentIF();
		diag.condition = true;

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());

		/*
		 * Else Case
		 */

		diag.condition = false;
		executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertFalse(executor.getErrors().isEmpty());
		Assert.assertEquals(5, executor.getErrors().size());
	}

	@Test
	public void testLoopFragment() {

		SequenceFragmentLoop diag = new SequenceFragmentLoop();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}

	@Test
	public void testParFragment() {
		SequencePar diag = new SequencePar();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(0, executor.getErrors().size());
	}

}
