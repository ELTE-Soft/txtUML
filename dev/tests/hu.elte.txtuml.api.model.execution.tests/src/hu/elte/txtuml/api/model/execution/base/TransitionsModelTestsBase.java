package hu.elte.txtuml.api.model.execution.base;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.models.transitions.A;

public class TransitionsModelTestsBase extends TestsBase {

	protected A a;

	protected void createAndStartA() {
		a = new A();
		Action.start(a);
	}
}
