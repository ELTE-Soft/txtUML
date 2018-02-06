package hu.elte.txtuml.layout.export.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.layout.export.model2.ColumnBottom;
import hu.elte.txtuml.layout.export.model2.ColumnMiddle;

public class ColumnMiddle_ColumnBottom extends Association {
	public class end1 extends End<One<ColumnMiddle>> {
	}

	public class end2 extends End<One<ColumnBottom>> {
	}
}