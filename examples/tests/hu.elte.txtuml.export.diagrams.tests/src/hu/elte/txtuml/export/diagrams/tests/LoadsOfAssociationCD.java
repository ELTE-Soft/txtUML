package hu.elte.txtuml.export.diagrams.tests;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.export.diagrams.tests.model.*; 

public class LoadsOfAssociationCD extends ClassDiagram{

	@Row({A.class, B.class, C.class})
	@North(from=D.class, val=A.class)
	@Row({D.class, E.class, F.class})
	class L extends Layout{}
	
}
