package hu.elte.txtuml.layout.export.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.model2.BetweenBases;
import hu.elte.txtuml.layout.export.model2.Random;

public class Random_BetweenBases extends Association {
	public class end1 extends One<Random> {
	}

	public class end2 extends One<BetweenBases> {
	}
}