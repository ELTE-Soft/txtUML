package hu.elte.txtuml.layout.export.test;

import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.test.Model1.*;
import hu.elte.txtuml.api.*;
import hu.elte.txtuml.layout.lang.AlignmentType;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.statements.Alignment;
import hu.elte.txtuml.layout.lang.statements.BottomMost;
import hu.elte.txtuml.layout.lang.statements.Contains;
import hu.elte.txtuml.layout.lang.statements.Diamond;
import hu.elte.txtuml.layout.lang.statements.North;
import hu.elte.txtuml.layout.lang.statements.Priority;
import hu.elte.txtuml.layout.lang.statements.Row;
import hu.elte.txtuml.layout.lang.statements.Show;
import hu.elte.txtuml.layout.lang.statements.TopMost;
import hu.elte.txtuml.layout.visualizer.annotations.Statement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

class Model1 extends Model {
	class A extends ModelClass {}
	
	class B extends A {}
	
	class C extends ModelClass {}
	
	class D extends ModelClass {}
	
	class E extends ModelClass {}
	
	class P extends Association {
		class e1 extends Many<A> {}
		class e2 extends One<B> {}
	}
	
    class Q extends Association {
        class e1 extends Many<A> {}
        class e2 extends One<B> {}
    }
    
    class R extends Association {
        class e1 extends Many<A> {}
        class e2 extends One<B> {}
    }
    
    class S extends Association {
        class e1 extends Many<A> {}
        class e2 extends One<B> {}
    }

    class T extends Association {
        class e1 extends Many<A> {}
        class e2 extends One<B> {}
    }
}


class Diagram1 extends Diagram {

    @Contains({A.class, B.class, NG1.class})
    class NG1 extends NodeGroup {}

    @Contains({NG1.class, C.class}) // NodeGroup NG1 as parameter
    @Alignment(AlignmentType.TopToBottom)
    class NG2 extends NodeGroup {}

    
    @Contains({A.class, B.class, D.class})
    class NG51 extends NodeGroup {}
    
    @Contains({C.class, NG1.class, F.class})
    class NG52 extends NodeGroup {}
    
    @Contains({NG3.class, A.class, B.class})
    class NG3 extends NodeGroup {}
    
    @Contains({Q.class, R.class})
    class LG1 extends LinkGroup {}
    
    class F extends Phantom {}
    class G extends Phantom {}
    
    @Contains({A.class, B.class, C.class, D.class}) // {C.class, D.class} is an anonymous NodeGroup
    class NG extends NodeGroup {}
    
    @Diamond(top = A.class, right = B.class, bottom = C.class, left = D.class)
    @Diamond(top = D.class, bottom = E.class, right = A.class, left = D.class)
    @North(val = {A.class, B.class, NG1.class}, from = B.class)
    @Show(F.class)
    @North(val = P.class, from = A.class, end = LinkEnd.Start)
    @TopMost(A.class)
    @BottomMost({B.class, C.class})
    @Priority(val = P.class, prior = 100)
    @Row({A.class, B.class, NG1.class})
	class L extends Layout {}
    
}

public class Test {
	public static void main(String[] args) {
		DiagramExporter exporter = DiagramExporter.create(Diagram1.class);
		DiagramExportationReport report = exporter.export();
        
		System.out.println("\nSTATEMENTS");		
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
