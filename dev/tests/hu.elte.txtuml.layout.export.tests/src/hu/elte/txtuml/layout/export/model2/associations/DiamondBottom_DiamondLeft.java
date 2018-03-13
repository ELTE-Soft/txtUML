package hu.elte.txtuml.layout.export.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.layout.export.model2.DiamondBottom;
import hu.elte.txtuml.layout.export.model2.DiamondLeft;

public class DiamondBottom_DiamondLeft extends Association {
	public class end1 extends End<One<DiamondBottom>> {
	}

	public class end2 extends End<One<DiamondLeft>> {
	}
}