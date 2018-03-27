package hu.elte.txtuml.layout.export.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.layout.export.model2.DiamondLeft;
import hu.elte.txtuml.layout.export.model2.DiamondTop;

public class DiamondTop_DiamondLeft extends Association {
	public class end1 extends End<One<DiamondTop>> {
	}

	public class end2 extends End<One<DiamondLeft>> {
	}
}