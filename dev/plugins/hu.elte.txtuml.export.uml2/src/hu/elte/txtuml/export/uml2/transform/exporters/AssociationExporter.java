package hu.elte.txtuml.export.uml2.transform.exporters;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.StructuredClassifier;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.mapping.ModelMapCollector;
import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider;
import hu.elte.txtuml.utils.jdt.ElementTypeTeller;
import hu.elte.txtuml.utils.jdt.SharedUtils;

/**
 * Instances of this class are responsible for exporting associations.
 */
public class AssociationExporter {

	private final TypeDeclaration sourceClass;
	private final ModelMapCollector mapping;
	private final Model exportedModel;

	/**
	 * Creates an AssocationExporter instance.
	 * 
	 * @param sourceClass
	 *            The class representing the txtUML association.
	 * @param exportedModel
	 *            The exported UML2 model.
	 */
	public AssociationExporter(TypeDeclaration sourceClass, ModelMapCollector mapping, Model exportedModel) {
		this.sourceClass = sourceClass;
		this.mapping = mapping;
		this.exportedModel = exportedModel;
	}

	/**
	 * Exports the association.
	 * 
	 * @return The exported UML2 association.
	 * @throws ExportException
	 */
	@SuppressWarnings("rawtypes")
	public Association exportAssociation() throws ExportException {
		List classes = this.sourceClass.bodyDeclarations();
		if (classes.size() != 2) {
			throw new ExportException("The following association doesn't have exactly 2 association ends: "
					+ sourceClass.resolveBinding().getQualifiedName());
		}

		Association exportedAssociation = (Association) exportedModel.createPackagedElement(
				sourceClass.getName().getFullyQualifiedName(), UMLPackage.eINSTANCE.getAssociation());

		exportAssociationEnd(exportedAssociation, classes.get(0), classes.get(1));
		exportAssociationEnd(exportedAssociation, classes.get(1), classes.get(0));
		mapping.put(SharedUtils.qualifiedName(sourceClass), exportedAssociation);
		return exportedAssociation;
	}

	/**
	 * Exports an association end.
	 * 
	 * @param exportedAssociation
	 *            The UML2 association to which this end belongs.
	 * @param sourceElement
	 *            The class representing the txtUML association end.
	 * @throws ExportException
	 */
	private void exportAssociationEnd(Association exportedAssociation, Object sourceElement, Object otherElement)
			throws ExportException {
		if (sourceElement instanceof TypeDeclaration && otherElement instanceof TypeDeclaration) {
			TypeDeclaration endSource = (TypeDeclaration) sourceElement;
			TypeDeclaration otherSource = (TypeDeclaration) otherElement;
			String phrase = endSource.getName().getFullyQualifiedName();
			StructuredClassifier participant = getParticipant(endSource);
			StructuredClassifier otherParticipant = getParticipant(otherSource);

			int lowerBound = MultiplicityProvider.getLowerBound(endSource);
			int upperBound = MultiplicityProvider.getUpperBound(endSource);

			if (MultiplicityProvider.hasInvalidMultiplicity(endSource))
				throw new ExportException("Association end " + endSource.getName() + " has invalid multiplicity.");

			boolean navigable;

			if (SharedUtils.typeIsAssignableFrom(endSource,
					hu.elte.txtuml.api.model.assocends.Navigability.Navigable.class)) {
				navigable = true;
			} else if (SharedUtils.typeIsAssignableFrom(endSource,
					hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable.class)) {
				navigable = false;
			} else {
				throw new ExportException("Association end " + endSource.getName() + " has invalid navigability.");
			}

			Property end;
			if (ElementTypeTeller.isComposition((TypeDeclaration) endSource.getParent())
					&& !ElementTypeTeller.isContainer(endSource)) {
				end = otherParticipant.createOwnedAttribute(phrase, participant, UMLPackage.eINSTANCE.getProperty());
				end.setAssociation(exportedAssociation);
				end.setAggregation(AggregationKind.COMPOSITE_LITERAL);
			} else {
				end = exportedAssociation.createOwnedEnd(phrase, participant);
				end.setAggregation(AggregationKind.NONE_LITERAL);
			}

			end.setIsNavigable(navigable);
			end.setLower(lowerBound);
			end.setUpper(upperBound);
		}
	}

	/**
	 * @param end
	 *            Type declaration representing an association end.
	 * @return The UML class that is the type of the association end.
	 * @throws ExportException
	 */
	private StructuredClassifier getParticipant(TypeDeclaration end) throws ExportException {
		String participantName = end.resolveBinding().getSuperclass().getTypeArguments()[0].getName();

		org.eclipse.uml2.uml.Type participant = (Type) exportedModel.getMember(participantName);
		if (participant == null) {
			throw new ExportException(
					participantName + "is a type of an association end, but it cannot be found in the exported model.");
		}

		return (StructuredClassifier) exportedModel.getMember(participantName);

	}
}
