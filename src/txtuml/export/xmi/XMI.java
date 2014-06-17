package txtuml.export.xmi;

import java.io.PrintWriter;
import java.io.IOException;
import txtuml.core.*;
import txtuml.importer.Importer;
import txtuml.importer.ImportException;

public class XMI {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Two command line arguments needed.");
			return;
		}

		try {
			Model m = Importer.importModel(args[0]);
            String xmi = createXmi(m);
            PrintWriter writer = new PrintWriter(args[1] + "/" + m.getName() + ".xmi", "UTF-8");
            writer.println(xmi);
            writer.close();
		} catch(ImportException ie) {
			System.out.println("Error: " + ie.getMessage());
		} catch(IOException ioe) {
			System.out.println("IO error.");
        }
	}
    
	static int uniqueId = 0;
	
	static String createUniqeId() {
		return Integer.toString(uniqueId++);
	}
	
    static String createXmi(Model m) {
        return "<?xml version = '1.0' encoding = 'UTF-8' ?>\n"
             + "<XMI xmi.version = '1.2' xmlns:UML = 'org.omg.xmi.namespace.UML' timestamp = 'Wed May 21 09:04:47 CEST 2014'>\n"
             + "<XMI.header> <XMI.documentation>\n"
             + "<XMI.exporter>txtUML XMI export</XMI.exporter>\n"
             + "<XMI.exporterVersion>0.1</XMI.exporterVersion>\n"
             + "</XMI.documentation>\n"
             + "<XMI.metamodel xmi.name=\"UML\" xmi.version=\"1.4\"/></XMI.header>\n"
             + "<XMI.content>\n"
             + "<UML:Model xmi.id = '" + m.hashCode() + "' name = '" + m.getName() + "' isSpecification = 'false' isRoot = 'false' isLeaf = 'false' isAbstract = 'false'>\n"
             + "<UML:Namespace.ownedElement>\n"
             + exportClasses(m)
             + exportAssociations(m)
             + "</UML:Namespace.ownedElement>\n"
             + "</UML:Model>\n"
             + "</XMI.content>\n"
             + "</XMI>\n";
    }

    static String exportClasses(Model m) {
        String result = "";
        for(txtuml.core.Class cl : m.getClasses()) {
            result += "<UML:Class xmi.id = '" + cl.hashCode() + "'\n"
                    + "name = '" + cl.getName() + "' visibility = 'public' isSpecification = 'false' isRoot = 'false'\n"
                    + "isLeaf = 'false' isAbstract = 'false' isActive = 'false'/>\n";
        }
        return result;
    }

    static String exportAssociations(Model m) {
        String result = "";
        for(Association assoc : m.getAssociations()) {
            result += "<UML:Association xmi.id = '" + assoc.hashCode() + "'\n"
                    + "name = '" + assoc.getName() + "' isSpecification = 'false' isRoot = 'false' isLeaf = 'false' isAbstract = 'false'>\n"
                    + "<UML:Association.connection>\n"
                    + exportAssociationEnd(assoc.getLeft())
                    + exportAssociationEnd(assoc.getRight())
                    + "</UML:Association.connection>\n"
                    + "</UML:Association>\n";
        }
        return result;
    }
    
    static String exportAssociationEnd(AssociationEnd end) {
        return "<UML:AssociationEnd xmi.id = '" + end.hashCode() + "'\n"
                    + "name = '" + end.getPhrase() + "' visibility = 'public' isSpecification = 'false' isNavigable = 'true'\n"
                    + "ordering = 'unordered' aggregation = 'none' targetScope = 'instance' changeability = 'changeable'>\n"
                    + "<UML:AssociationEnd.multiplicity>\n"
                    + "<UML:Multiplicity xmi.id = '" + createUniqeId() + "'>\n"
                    + "<UML:Multiplicity.range>\n"
                    + "<UML:MultiplicityRange xmi.id = '" + createUniqeId() + "' lower = '" + end.getLowerBound() + "' upper = '" + end.getUpperBound() + "'/>\n"
                    + "</UML:Multiplicity.range>\n"
                    + "</UML:Multiplicity>\n"
                    + "</UML:AssociationEnd.multiplicity>\n"
                    + "<UML:AssociationEnd.participant>\n"
                    + "<UML:Class xmi.idref = '" + end.getParticipant().hashCode() + "'/>\n"
                    + "</UML:AssociationEnd.participant>\n"
                    + "</UML:AssociationEnd>\n";
    }

}
