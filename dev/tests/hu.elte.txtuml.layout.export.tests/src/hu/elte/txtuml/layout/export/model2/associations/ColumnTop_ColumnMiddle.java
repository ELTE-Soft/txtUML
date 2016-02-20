package hu.elte.txtuml.layout.export.model2.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.layout.export.model2.ColumnMiddle;
import hu.elte.txtuml.layout.export.model2.ColumnTop;

// Associations
public class ColumnTop_ColumnMiddle extends Association {
	public class end1 extends One<ColumnTop> {
	}

	public class end2 extends One<ColumnMiddle> {
	}
}