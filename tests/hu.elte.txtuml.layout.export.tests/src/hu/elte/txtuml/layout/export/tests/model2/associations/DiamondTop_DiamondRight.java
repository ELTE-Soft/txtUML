package hu.elte.txtuml.layout.export.tests.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model2.DiamondRight;
import hu.elte.txtuml.layout.export.tests.model2.DiamondTop;

public class DiamondTop_DiamondRight extends Association {
	public class end1 extends One<DiamondTop> {
	}

	public class end2 extends One<DiamondRight> {
	}
}