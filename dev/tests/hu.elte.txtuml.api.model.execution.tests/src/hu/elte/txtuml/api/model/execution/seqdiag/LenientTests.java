package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingLenientErr;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceMessageSendingLenientValid;
import hu.elte.txtuml.api.model.execution.seqdiag.sequences.SequenceOnlyFromActorLenient;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class LenientTests {

	@Test
	public void testOnlyFromActor() {
		SequenceOnlyFromActorLenient diag = new SequenceOnlyFromActorLenient();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();
		
		Assert.assertEquals(0, executor.getErrors().size());
	}
	
	@Test
	public void testValidPasses() {
		SequenceMessageSendingLenientValid diag = new SequenceMessageSendingLenientValid();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(0, executor.getErrors().size());
	}

	@Test
	public void testMessageError() {
		SequenceMessageSendingLenientErr diag = new SequenceMessageSendingLenientErr();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertEquals(1, executor.getErrors().size());
	}
}
