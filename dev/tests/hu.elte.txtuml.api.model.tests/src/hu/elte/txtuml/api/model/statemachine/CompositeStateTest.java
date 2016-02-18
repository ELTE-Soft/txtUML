package hu.elte.txtuml.api.model.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.base.HierarchicalModelTestsBase;
import hu.elte.txtuml.api.model.models.hierarchical.A;
import hu.elte.txtuml.api.model.models.hierarchical.Sig0;
import hu.elte.txtuml.api.model.models.hierarchical.Sig1;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

@RunWith(SeparateClassloaderTestRunner.class)
public class CompositeStateTest extends HierarchicalModelTestsBase {

	@Test
	public void test() {
		Action.send(a, new Sig0());
		Action.send(a, new Sig0());
		Action.send(a, new Sig1());

		stopModelExecution();

		A.CS1 cs1 = a.new CS1();
		A.CS1.CS2 cs2 = cs1.new CS2();

		executionAsserter.assertEvents(x -> {
			transition(x, a, a.new Initialize());
			x.processingSignal(a, new Sig0());
			transition(x, a, a.new S1_CS1());
			x.enteringVertex(a, cs1.new Init());
			transition(x, a, cs1, cs1.new Initialize());
			x.processingSignal(a, new Sig0());
			transition(x, a, cs1, cs1.new S2_CS2());
			x.enteringVertex(a, cs2.new Init());
			transition(x, a, cs2, cs2.new Initialize());
			x.processingSignal(a, new Sig1());
			x.leavingVertex(a, cs2.new S3());
			x.leavingVertex(a, cs2);
			transition(x, a, a.new CS1_S1());
			x.executionTerminated();
		});
	}
}
