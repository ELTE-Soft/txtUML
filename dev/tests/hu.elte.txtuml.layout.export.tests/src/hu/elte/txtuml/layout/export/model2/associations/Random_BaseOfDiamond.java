package hu.elte.txtuml.layout.export.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.layout.export.model2.BaseOfDiamond;
import hu.elte.txtuml.layout.export.model2.Random;

public class Random_BaseOfDiamond extends Association {
	public class end1 extends End<One<Random>> {
	}

	public class end2 extends End<One<BaseOfDiamond>> {
	}
}