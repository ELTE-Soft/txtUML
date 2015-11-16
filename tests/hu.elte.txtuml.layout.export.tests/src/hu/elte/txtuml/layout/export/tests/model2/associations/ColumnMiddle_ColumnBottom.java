package hu.elte.txtuml.layout.export.tests.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.tests.model2.ColumnBottom;
import hu.elte.txtuml.layout.export.tests.model2.ColumnMiddle;

public class ColumnMiddle_ColumnBottom extends Association {
	public class end1 extends One<ColumnMiddle> {
	}

	public class end2 extends One<ColumnBottom> {
	}
}