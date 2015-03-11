package hu.elte.txtuml.layout.export.example;

import Annotations.Statement;
import hu.elte.txtuml.api.Association;
import hu.elte.txtuml.api.Model;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.AlignmentType;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.statements.*;
import hu.elte.txtuml.layout.export.example.ExampleModel.*;

class ExampleModel extends Model {
	class A extends ModelClass {
	}
	
	class B extends ModelClass {
	}
	
	class P extends Association {
	}

}

// The three above are all parts of the model (two classes and an association
// for example). The diagram definition below are based on referencing these.

public class Example1 extends Diagram {

	@Contains({ A.class, B.class })
	// means group(A, G) group(B, G)
	@Alignment(AlignmentType.TopToBottom)
	// means layout(G, TopToBottom)
	@North(val = A.class, from = B.class)
	class G extends LayoutGroup {
	}

	@Contains(P.class)
	// means group(P, G)
	class G2 extends LayoutGroup {
	}

	
	@North(val = A.class, from = B.class, end = LinkEnd.Start)
	@South(val = A.class, from = B.class)
	@East(val = A.class, from = B.class)
	@West(val = A.class, from = B.class, end = LinkEnd.End)
	@Above(val = A.class, from = B.class)
	@Below(val = A.class, from = B.class)
	@Right(val = A.class, from = B.class)
	@Left(val = A.class, from = B.class)
	@TopMost(A.class)
	@BottomMost(A.class)
	@RightMost(A.class)
	@LeftMost(A.class)
	@Priority(val = P.class, prior = 100)
	@North(val = P.class, from = B.class)
	@South(val = P.class, from = B.class, end = LinkEnd.Default)
	@East(val = P.class, from = B.class)
	@West(val = P.class, from = B.class)
	class Example1Layout extends Layout {
	}
	
	public static void main(String[] args) {
		DiagramExportationReport report = DiagramExporter.create(Example1.class).export();
		System.out.println(report.isSuccessful());
		for (Statement statement : report.getResult()) {
			System.out.print(statement.getType() + " :");
			for (String param : statement.getParameters()) {
				System.out.print(" " + param);
			}
			System.out.println();
		}
	}
}