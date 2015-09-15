package hu.elte.txtuml.api.model.tests.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.tests.base.HierarchicalModelTestsBase;
import hu.elte.txtuml.api.model.tests.models.HierarchicalModel.Sig0;
import hu.elte.txtuml.api.model.tests.models.HierarchicalModel.Sig1;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class CompositeStateTest extends HierarchicalModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig0());
		Action.send(a, new Sig0());
		Action.send(a, new Sig1());
		
		stopModelExecution();
		
		executionAsserter.assertEvents(x -> {
						transition(x, a, a.new Initialize());
						x.processingSignal(a, new Sig0());
						transition(x, a, a.new S1_CS1());
						x.enteringVertex(a, a.new CS1().new Init());
						transition(x, a, a.new CS1().new Initialize());
						x.processingSignal(a, new Sig0());
						transition(x, a, a.new CS1().new S2_CS2());
						x.enteringVertex(a, a.new CS1().new CS2().new Init());
						transition(x, a, a.new CS1().new CS2().new Initialize());
						x.processingSignal(a, new Sig1());
						x.leavingVertex(a, a.new CS1().new CS2().new S3());
						x.leavingVertex(a, a.new CS1().new CS2());
						transition(x, a, a.new CS1_S1());
						x.executionTerminated(); });
	}
}
