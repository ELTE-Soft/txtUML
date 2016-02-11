package hu.elte.txtuml.export.uml2.transform.exporters;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.mapping.ModelMapCollector;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

public class ClassifierExporter {

	private final ModelMapCollector mapping;
	private final Model exportedModel;

	public ClassifierExporter(ModelMapCollector mapping, Model exportedModel) {
		this.mapping = mapping;
		this.exportedModel = exportedModel;
	}

	public Class exportClass(TypeDeclaration typeDeclaration) {
		String name = typeDeclaration.getName().getFullyQualifiedName();
		Class cls = exportedModel.createOwnedClass(name, ElementTypeTeller.isAbstract(typeDeclaration));
		mapping.put(SharedUtils.qualifiedName(typeDeclaration), cls);
		return cls;
	}

	public Signal exportSignal(TypeDeclaration typeDeclaration) {
		String name = typeDeclaration.getName().getFullyQualifiedName();

		Signal signal = (Signal) exportedModel.createOwnedType(name, UMLPackage.Literals.SIGNAL);

		SignalEvent signalEvent = (SignalEvent) exportedModel.createPackagedElement(name + "_event",
				UMLPackage.Literals.SIGNAL_EVENT);

		signalEvent.setSignal(signal);

		return signal;
	}
}
