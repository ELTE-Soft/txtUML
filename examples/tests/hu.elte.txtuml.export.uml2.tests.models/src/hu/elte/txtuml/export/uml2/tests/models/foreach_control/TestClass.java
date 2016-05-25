package hu.elte.txtuml.export.uml2.tests.models.foreach_control;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	private int sum = 0;

	public void test(Collection<Integer> coll) {
		for (Integer i : coll) {
			sum += i;
		}
	}
}