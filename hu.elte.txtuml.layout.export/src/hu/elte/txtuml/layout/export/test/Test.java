package hu.elte.txtuml.layout.export.test;

import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.test.M.*;
import hu.elte.txtuml.api.*;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.statements.Above;
import hu.elte.txtuml.layout.lang.statements.Below;
import hu.elte.txtuml.layout.lang.statements.Left;
import hu.elte.txtuml.layout.lang.statements.Right;
import hu.elte.txtuml.layout.lang.statements.Show;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

class M extends Model {
	class A extends ModelClass {}
	
	class B extends ModelClass {}
	
	class P extends Association {
		class e1 extends Many<A> {}
		class e2 extends One<B> {}
	}
}


class D extends Diagram {

	//@Show(A.class)
	@Show({})
	@Show({B.class, P.class})
	@Above(val = A.class, from = B.class)
	@Below(val = A.class, from = B.class)
	@Right(val = A.class, from = B.class)
	@Left(val = A.class, from = B.class)
	class L extends Layout {}
}

public class Test {
	public static void main(String[] args) {
		System.out.println("\nSTATEMENTS");
		DiagramExporter exporter = DiagramExporter.create(D.class);
		DiagramExportationReport report = exporter.export();
		for (Statement st : report.getStatements()) {
			System.out.println(st);
		}
		System.out.println("\nNODES");
		for (RectangleObject n : report.getNodes()) {
			System.out.println(n);
		}
		System.out.println("\nLINKS");
		for (LineAssociation l : report.getLinks()) {
			System.out.println(l);
		}
		
	}
}
