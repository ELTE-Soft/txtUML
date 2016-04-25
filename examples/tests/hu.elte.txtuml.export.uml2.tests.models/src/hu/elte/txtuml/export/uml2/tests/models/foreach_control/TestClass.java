package hu.elte.txtuml.export.uml2.tests.models.foreach_control;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;

public class TestClass extends ModelClass {

	public void test(Collection<Integer> coll) {
		int sum = 0;
		for (Integer i : coll) {
			sum += i;
		}
	}
}