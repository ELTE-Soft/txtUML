package hu.elte.txtuml.layout.export.test;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.statements.Above;
import hu.elte.txtuml.api.layout.statements.Row;
import hu.elte.txtuml.api.layout.statements.Show;
import hu.elte.txtuml.api.layout.statements.South;
import hu.elte.txtuml.api.layout.statements.West;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.test.PublicationModel.Author;
import hu.elte.txtuml.layout.export.test.PublicationModel.CoAuthor;
import hu.elte.txtuml.layout.export.test.PublicationModel.Conference;
import hu.elte.txtuml.layout.export.test.PublicationModel.MainConference;
import hu.elte.txtuml.layout.export.test.PublicationModel.Paper;
import hu.elte.txtuml.layout.export.test.PublicationModel.Reviewer;
import hu.elte.txtuml.layout.export.test.PublicationModel.Workshop;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;

class PublicationModel extends Model {

    class Author extends ModelClass {}
    class Paper extends ModelClass {}
    class Reviewer extends ModelClass {}
    
    class Conference extends ModelClass {}
    class MainConference extends Conference {}
    class Workshop extends Conference {}
    
    class CoAuthor extends Association {
        class who extends HiddenMany<Author> {}
        class with extends Many<Author> {}
    }
    
    class Writes extends Association {
        class author extends Many<Author> {} 
        class paper extends Many<Paper> {}
    }
    
    class Reviews extends Association {
        class reviewer extends Many<Reviewer> {}
        class paper extends Many<Paper> {}
    }
    
    class PublishedIn extends Association {
        class paper extends Many<Paper> {}
        class conference extends One<Conference> {}
    }
    
}

class PublicationDiagram extends Diagram {
    
    @Row({ Author.class, Paper.class, Reviewer.class })
    @Above(val = Paper.class, from = Conference.class)
    @Show({ MainConference.class, Workshop.class })
    
    @West(val = CoAuthor.class, from = Author.class, end = LinkEnd.Start)
    @South(val = CoAuthor.class, from = Author.class, end = LinkEnd.End)
    class ClassDiagramLayout extends Layout {}
    
}

public class Test3 {
    
    public static void main(String[] args) {
        DiagramExporter exporter = DiagramExporter.create(PublicationDiagram.class);
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
