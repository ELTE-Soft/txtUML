package hu.elte.txtuml.examples.performance_test.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;

public class Test extends ModelClass {
	public void test() {
		A a = Action.create(A.class, 100000);
		Action.start(a);
	}
}