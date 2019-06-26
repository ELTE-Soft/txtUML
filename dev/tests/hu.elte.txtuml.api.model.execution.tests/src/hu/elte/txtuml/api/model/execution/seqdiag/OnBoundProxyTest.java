package hu.elte.txtuml.api.model.execution.seqdiag;

import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.unittests.NotBoundErrorTest;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public class OnBoundProxyTest {

	@Test
	public void testNotBoundProxy() {
		NotBoundErrorTest diag = new NotBoundErrorTest();

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();

		executor.setDiagram(diag).run();

		Assert.assertFalse(executor.getErrors().isEmpty());
		Assert.assertEquals(1, executor.getErrors().size());

	}

}