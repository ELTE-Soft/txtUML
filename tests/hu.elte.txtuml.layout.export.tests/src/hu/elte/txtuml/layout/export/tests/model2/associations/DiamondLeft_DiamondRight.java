package hu.elte.txtuml.layout.export.tests.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model2.DiamondLeft;
import hu.elte.txtuml.layout.export.tests.model2.DiamondRight;

public class DiamondLeft_DiamondRight extends Association {
	public class end1 extends One<DiamondLeft> {
	}

	public class end2 extends One<DiamondRight> {
	}
}