package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceEmpty;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingStrictErr1;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingStrictErr2;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingStrictValid;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceOnlyFromActor;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class StrictSeqTests {

	@Test
	public void testEmpty() {
		SequenceEmpty diag = new SequenceEmpty();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());		
	}

	@Test
	public void testOnlyFromActor() {
		SequenceOnlyFromActor diag = new SequenceOnlyFromActor();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();
		
		Assert.assertEquals(4, executor.getErrors().size());
	}
	
	@Test
	public void testValidPasses() {
		SequenceMessageSendingStrictValid diag = new SequenceMessageSendingStrictValid();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertTrue(executor.getErrors().isEmpty());
	}

	@Test
	public void testFirstMessageError() {
		SequenceMessageSendingStrictErr1 diag = new SequenceMessageSendingStrictErr1();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(1, executor.getErrors().size());
	}

	@Test
	public void testLastMessageError() {
		SequenceMessageSendingStrictErr2 diag = new SequenceMessageSendingStrictErr2();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(2, executor.getErrors().size());
	}
}
