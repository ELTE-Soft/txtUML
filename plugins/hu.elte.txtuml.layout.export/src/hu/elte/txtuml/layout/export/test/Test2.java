package hu.elte.txtuml.layout.export.test;

import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.statements.*;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.test.Model2.*;

class Model2 extends Model {
    
    // Classes
    class Random extends ModelClass {}
    
    class BaseOfColumnTop extends ModelClass {}
    class ColumnTop extends BaseOfColumnTop {}
    class ColumnMiddle extends ModelClass {}
    class ColumnBottom extends ModelClass {}
    
    class BetweenBases extends ModelClass {}

    class BaseOfDiamond extends ModelClass {}
    class DiamondTop extends BaseOfDiamond {}
    class DiamondLeft extends BaseOfDiamond {}
    class DiamondBottom extends BaseOfDiamond {}
    class DiamondRight extends BaseOfDiamond {}

    // Associations
    class ColumnTop_ColumnMiddle extends Association {
        class end1 extends One<ColumnTop> {}
        class end2 extends One<ColumnMiddle> {}
    }

    class ColumnMiddle_ColumnBottom extends Association {
        class end1 extends One<ColumnMiddle> {}
        class end2 extends One<ColumnBottom> {}
    }

    class ColumnBottom_ColumnBottom extends Association {
        class end1 extends One<ColumnBottom> {}
        class end2 extends One<ColumnBottom> {}
    }
    
    class DiamondTop_DiamondLeft extends Association {
        class end1 extends One<DiamondTop> {}
        class end2 extends One<DiamondLeft> {}
    }
    
    class DiamondTop_DiamondBottom extends Association {
        class end1 extends One<DiamondTop> {}
        class end2 extends One<DiamondBottom> {}
    }

    class DiamondTop_DiamondRight extends Association {
        class end1 extends One<DiamondTop> {}
        class end2 extends One<DiamondRight> {}
    }
    
    class DiamondLeft_DiamondRight extends Association {
        class end1 extends One<DiamondLeft> {}
        class end2 extends One<DiamondRight> {}
    }
    
    class DiamondBottom_DiamondLeft extends Association {
        class end1 extends One<DiamondBottom> {}
        class end2 extends One<DiamondLeft> {}
    }

    class DiamondBottom_DiamondRight extends Association {
        class end1 extends One<DiamondBottom> {}
        class end2 extends One<DiamondRight> {}
    }
    
    class Random_BaseOfColumnTop extends Association {
        class end1 extends One<Random> {}
        class end2 extends One<BaseOfColumnTop> {}
    }
    
    class Random_BetweenBases extends Association {
        class end1 extends One<Random> {}
        class end2 extends One<BetweenBases> {}
    }   

    class Random_BaseOfDiamond extends Association {
        class end1 extends One<Random> {}
        class end2 extends One<BaseOfDiamond> {}
    }

}

class Diagram2 extends Diagram {
    
    @Contains({ ColumnTop.class, ColumnMiddle.class, ColumnBottom.class })
    @Alignment(AlignmentType.TopToBottom)
    class ColumnGroup extends NodeGroup {}

    @Diamond(top = DiamondTop.class, left = DiamondLeft.class, bottom = DiamondBottom.class, right = DiamondRight.class)
    @West(val = ColumnGroup.class, from = {DiamondTop.class, DiamondLeft.class, DiamondRight.class, DiamondBottom.class})
    @Row({BaseOfColumnTop.class, BetweenBases.class, BaseOfDiamond.class})
    @Show(Random.class)
    @West(val = ColumnBottom_ColumnBottom.class, from = ColumnBottom.class, end = LinkEnd.Start)
    @East(val = ColumnBottom_ColumnBottom.class, from = ColumnBottom.class, end = LinkEnd.End)
    @Priority(val = DiamondTop_DiamondBottom.class, prior = 10) // optional
    class Diagram2Layout extends Layout {}
    
}

public class Test2 {
    
    public static void main(String[] args) {
        DiagramExporter exporter = DiagramExporter.create(Diagram2.class);
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
        
        System.out.println("\nThe exportation was successful.");
        
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
