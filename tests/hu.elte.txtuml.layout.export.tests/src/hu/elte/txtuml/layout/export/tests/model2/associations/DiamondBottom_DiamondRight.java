package hu.elte.txtuml.layout.export.tests.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model2.DiamondBottom;
import hu.elte.txtuml.layout.export.tests.model2.DiamondRight;

public class DiamondBottom_DiamondRight extends Association {
	public class end1 extends One<DiamondBottom> {
	}

	public class end2 extends One<DiamondRight> {
	}
}