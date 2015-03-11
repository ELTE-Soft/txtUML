package hu.elte.txtuml.layout.export.example;


import Annotations.Statement;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.statements.*;

class D extends Diagram {

	class A implements LayoutNode {
		
	}

	class B implements LayoutNode {
		
	}

	
	@Contains({A.class, B.class})
	class G extends LayoutGroup {
	}
	
	class L extends Layout {
	}
}

public class Trial {
	
	public static void main(String[] args) {
		DiagramExportationReport report = DiagramExporter.create(D.class).export();
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
