package hu.elte.txtuml.export.uml2.transform.exporters;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.StructuredClassifier;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Usage;

import hu.elte.txtuml.utils.jdt.ElementTypeTeller;

public class PortExporter {

	TypeExporter typeExporter;

	public PortExporter(TypeExporter typeExporter) {
		this.typeExporter = typeExporter;
	}

	public void exportPort(TypeDeclaration typeDeclaration, StructuredClassifier ownerClassifier) {
		ITypeBinding[] typeArguments = typeDeclaration.resolveBinding().getSuperclass().getTypeArguments();

		Type required = typeExporter.exportType(typeArguments[0]);
		Type provided = typeExporter.exportType(typeArguments[1]);

		Port createdPort = UMLFactory.eINSTANCE.createPort();
		if (ElementTypeTeller.isBehavioralPort(typeDeclaration)) {
			createdPort.setIsBehavior(true);
		}
		createdPort.setName(typeDeclaration.getName().getIdentifier());

		Interface dummyProvided = UMLFactory.eINSTANCE.createInterface();
		Generalization dummyInherit = UMLFactory.eINSTANCE.createGeneralization();
		dummyProvided.setName(provided.getName() + "_for_" + createdPort.getName());
		dummyInherit.setSpecific(dummyProvided);
		dummyInherit.setGeneral((Classifier) provided);
		Usage providedRequired = UMLFactory.eINSTANCE.createUsage();
		providedRequired.getClients().add(dummyProvided);
		providedRequired.getSuppliers().add(required);
		createdPort.setType(dummyProvided);
		provided.getModel().getPackagedElements().add(providedRequired);

		
		if (ownerClassifier instanceof Class) {
			((Class) ownerClassifier).getNestedClassifiers().add(dummyProvided);
		}
		ownerClassifier.getOwnedAttributes().add(createdPort);
	}

}
