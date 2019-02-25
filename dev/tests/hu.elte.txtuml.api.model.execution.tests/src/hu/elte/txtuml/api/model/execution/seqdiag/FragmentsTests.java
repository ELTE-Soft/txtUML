package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceFragmentIF;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceFragmentLoop;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceParErr;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceParValid;
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
		SequenceParValid diagValid = new SequenceParValid();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diagValid).run();

		Assert.assertEquals(0, executor.getErrors().size());
		
		SequenceParErr diagErr = new SequenceParErr();
		
		executor = SequenceDiagramExecutor.create();
		
		executor.setDiagram(diagErr).run();
		
		Assert.assertEquals(3, executor.getErrors().size());
	}

}
