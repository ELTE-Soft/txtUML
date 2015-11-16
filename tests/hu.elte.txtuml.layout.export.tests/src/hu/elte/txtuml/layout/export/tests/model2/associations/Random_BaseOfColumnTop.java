package hu.elte.txtuml.layout.export.tests.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model2.BaseOfColumnTop;
import hu.elte.txtuml.layout.export.tests.model2.Random;

public class Random_BaseOfColumnTop extends Association {
	public class end1 extends One<Random> {
	}

	public class end2 extends One<BaseOfColumnTop> {
	}
}