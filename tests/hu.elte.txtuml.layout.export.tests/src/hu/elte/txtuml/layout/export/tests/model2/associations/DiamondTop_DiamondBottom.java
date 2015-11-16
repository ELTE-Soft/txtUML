package hu.elte.txtuml.layout.export.tests.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model2.DiamondBottom;
import hu.elte.txtuml.layout.export.tests.model2.DiamondTop;

public class DiamondTop_DiamondBottom extends Association {
	public class end1 extends One<DiamondTop> {
	}

	public class end2 extends One<DiamondBottom> {
	}
}