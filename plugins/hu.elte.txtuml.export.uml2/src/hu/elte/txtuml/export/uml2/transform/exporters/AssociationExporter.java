package hu.elte.txtuml.export.uml2.transform.exporters;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
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
	public AssociationExporter(TypeDeclaration sourceClass,
			ModelMapCollector mapping, Model exportedModel) {
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
			throw new ExportException(
					"The following association doesn't have exactly 2 association ends: "
							+ sourceClass.resolveBinding().getQualifiedName());
		}

		Association exportedAssociation = (Association) exportedModel
				.createPackagedElement(sourceClass.getName()
						.getFullyQualifiedName(), UMLPackage.eINSTANCE
						.getAssociation());

		exportAssociationEnd(exportedAssociation, classes.get(0));
		exportAssociationEnd(exportedAssociation, classes.get(1));
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
	private void exportAssociationEnd(Association exportedAssociation,
			Object sourceElement) throws ExportException {
		if (sourceElement instanceof TypeDeclaration) {
			TypeDeclaration endSource = (TypeDeclaration) sourceElement;
			String phrase = endSource.getName().getFullyQualifiedName();
			String className = endSource.resolveBinding().getSuperclass()
					.getTypeArguments()[0].getName();

			int lowerBound = MultiplicityProvider.getLowerBound(endSource);
			int upperBound = MultiplicityProvider.getUpperBound(endSource);

			if (MultiplicityProvider.hasInvalidMultiplicity(endSource))
				throw new ExportException("Association end "
						+ endSource.getName() + " has invalid multiplicity.");

			boolean navigable;

			if (SharedUtils
					.typeIsAssignableFrom(
							endSource,
							hu.elte.txtuml.api.model.assocends.Navigability.Navigable.class)) {
				navigable = true;
			} else if (SharedUtils
					.typeIsAssignableFrom(
							endSource,
							hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable.class)) {
				navigable = false;
			} else {
				throw new ExportException("Association end "
						+ endSource.getName() + " has invalid navigability.");
			}

			org.eclipse.uml2.uml.Type participant = (Type) exportedModel
					.getMember(className);

			if (participant == null) {
				throw new ExportException(phrase + ": No class " + className
						+ " found in this model.");
			}

			Property end;
			if (navigable) {
				end = exportedAssociation.createNavigableOwnedEnd(phrase,
						participant);
			} else {
				end = exportedAssociation.createOwnedEnd(phrase, participant);
			}

			if (ElementTypeTeller.isComposition((TypeDeclaration) endSource
					.getParent()) && !ElementTypeTeller.isContainer(endSource)) {
				end.setAggregation(AggregationKind.COMPOSITE_LITERAL);
			} else {
				end.setAggregation(AggregationKind.NONE_LITERAL);
			}

			end.setLower(lowerBound);
			end.setUpper(upperBound);
		}
	}
}
