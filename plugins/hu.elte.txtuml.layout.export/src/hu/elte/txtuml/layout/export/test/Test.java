package hu.elte.txtuml.layout.export.test;

import hu.elte.txtuml.api.layout.Alignment;
import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.api.layout.BottomMost;
import hu.elte.txtuml.api.layout.Contains;
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.TopMost;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.test.Model1.A;
import hu.elte.txtuml.layout.export.test.Model1.B;
import hu.elte.txtuml.layout.export.test.Model1.C;
import hu.elte.txtuml.layout.export.test.Model1.D;
import hu.elte.txtuml.layout.export.test.Model1.E;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

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

    @Contains({A.class, B.class})
    class NG1 extends NodeGroup {}

    @Contains({NG1.class, C.class}) // NodeGroup NG1 as parameter
    @Alignment(AlignmentType.TopToBottom)
    class NG2 extends NodeGroup {}
    
    @Contains({A.class, NG1.class, D.class})
    class NG51 extends NodeGroup {}
    
    @Contains({C.class, NG52.class, F.class})
    class NG52 extends NodeGroup {}
    
    @Contains({NGB.class})
    class NGA extends NodeGroup {}
    
    @Contains({NGC.class})
    class NGB extends NodeGroup {}
    
    @Contains({NGA.class})
    class NGC extends NodeGroup {}
    
    class NG3 extends NodeGroup {}
    
    @Alignment(AlignmentType.TopToBottom)
    class LG1 extends LinkGroup {}
    
    class MyClass {}
    
    class F extends Phantom {}
    class G extends Phantom {}
    
    @Deprecated
    @Contains({}) // {C.class, D.class} is an anonymous NodeGroup
    class NG extends NodeGroup {}
    
    @Diamond(top = A.class, right = B.class, bottom = C.class, left = D.class)
    @Diamond(top = D.class, bottom = E.class, right = A.class, left = D.class)
    @North(val = {A.class, B.class, NG1.class}, from = B.class)
    @Show({})
    @Row({NG52.class})
    @North(val = {NG52.class}, from = A.class, end = LinkEnd.End)
    @TopMost({NG52.class})
    @BottomMost({B.class, C.class})
    @Priority(val = {LG1.class}, prior = 100)
    @Row({A.class, B.class, NG1.class})
    @North(val = NGA.class, from = NG52.class)
    @Deprecated
	class L extends Layout {}
    
}

public class Test {
	public static void main(String[] args) {
		DiagramExporter exporter = DiagramExporter.create(Diagram1.class);
		DiagramExportationReport report = exporter.export();
        
		System.out.println("Warnings:");
		for (String warning : report.getWarnings()) {
		    System.out.println(warning);
		}
		
		System.out.println("\nErrors:");
		for (String error : report.getErrors()) {
		    System.out.println(error);
		}
		
		if (!report.isSuccessful()) {
		    System.out.println("\nThe exportation wasn't successful.");
		    return;
		}
		
		System.out.println("\nStatements:");		
		for (Statement st : report.getStatements()) {
			System.out.println(st);
		}
		
		System.out.println("\nNodes:");
		for (RectangleObject n : report.getNodes()) {
			System.out.println(n);
		}
		
		System.out.println("\nLinks:");
		for (LineAssociation l : report.getLinks()) {
			System.out.println(l);
		}		
	}
}
