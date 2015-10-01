package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.UMLPackage;

public class ClassifierImporter {
	private ModelImporter modelImporter;
	
	public ClassifierImporter(ModelImporter modelImporter) {
		this.modelImporter = modelImporter;
	}
	
	public org.eclipse.uml2.uml.Class importClass(TypeDeclaration typeDeclaration) {
		String name = typeDeclaration.getName().getFullyQualifiedName();
		return this.modelImporter.getImportedModel().createOwnedClass(name, ElementTypeTeller.isAbstract(typeDeclaration));
	}

	public Signal importSignal(TypeDeclaration typeDeclaration) {
		String name = typeDeclaration.getName().getFullyQualifiedName();

		Signal signal = (Signal) this.modelImporter.getImportedModel().createOwnedType(name,
				UMLPackage.Literals.SIGNAL);

		SignalEvent signalEvent = (SignalEvent) this.modelImporter.getImportedModel().createPackagedElement(name + "_event",
				UMLPackage.Literals.SIGNAL_EVENT);

		signalEvent.setSignal(signal);
		
		return signal;
	}	
}
