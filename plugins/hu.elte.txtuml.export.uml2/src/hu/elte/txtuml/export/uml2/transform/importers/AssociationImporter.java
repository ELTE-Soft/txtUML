package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.utils.MultiplicityProvider;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Instances of this class are responsible for importing associations.
 * @author Adam Ancsin
 *
 */
public class AssociationImporter extends AbstractImporter{
	
	private TypeDeclaration sourceClass;
	private Model importedModel;
	private Association importedAssociation;
	
	/**
	 * Creates an AssocationImporter instance.
	 * @param sourceClass The class representing the txtUML association.
	 * @param importedModel The imported UML2 model.
	 */
	public AssociationImporter(TypeDeclaration sourceClass, Model importedModel)
	{
		this.sourceClass = sourceClass;
		this.importedModel = importedModel;
	}
	
	/**
	 * Gets the imported UML2 association.
	 * @return The imported UML2 association.
	 *
	 * @author Adam Ancsin
	 */
	public Association getImportedAssociation()
	{
		return importedAssociation;
	}
	
	/**
	 * Imports the association.
	 * @return The imported UML2 association.
	 * @throws ImportException
	 *
	 */
	@SuppressWarnings("rawtypes")
	public Association importAssociation() throws ImportException
	{
		List classes = this.sourceClass.bodyDeclarations();
		if(classes.size()!=2)
		{
			throw new ImportException(
					"The following association doesn't have exactly 2 association ends: " + 
					sourceClass.resolveBinding().getQualifiedName()
				);
		}
		
		
	    this.importedAssociation = (Association)this.importedModel.createPackagedElement(
	    		sourceClass.getName().getFullyQualifiedName(), UMLPackage.eINSTANCE.getAssociation()); 
		importAssociationEnd(classes.get(0));
	    importAssociationEnd(classes.get(1));
		    
	    return importedAssociation;
	}
	
	/**
	 * Imports an association end.
	 * @param sourceElement The class representing the txtUML association end.
	 * @throws ImportException
	 *
	 */
	private void importAssociationEnd(Object sourceElement) throws ImportException
	{
		if(sourceElement instanceof TypeDeclaration) {
			TypeDeclaration endSource = (TypeDeclaration) sourceElement;
			String phrase = endSource.getName().getFullyQualifiedName();
			String className = endSource.resolveBinding().getSuperclass().getTypeArguments()[0].getName();

			int lowerBound = MultiplicityProvider.getLowerBound(endSource);
			int upperBound = MultiplicityProvider.getUpperBound(endSource);
				
			if(MultiplicityProvider.hasInvalidMultiplicity(endSource))
				throw new ImportException("Association end "+endSource.getName()+" has invalid multiplicity.");            

			boolean navigable;

			if(SharedUtils.typeIsAssignableFrom(endSource, hu.elte.txtuml.api.model.assocends.Navigability.Navigable.class)) {
				navigable = true;
			} else if (SharedUtils.typeIsAssignableFrom(endSource, hu.elte.txtuml.api.model.assocends.Navigability.NonNavigable.class)) {
				navigable = false;
			} else {
				throw new ImportException("Association end " + endSource.getName() + " has invalid navigability.");   
			}
				
			org.eclipse.uml2.uml.Type participant = (Type) importedModel.getMember(className);

			if(participant == null) {
				throw new ImportException(phrase + ": No class " + className + " found in this model.");
			}
			
			Property end;
		    if(navigable) {
			    end = this.importedAssociation.createNavigableOwnedEnd(phrase, participant);
		    } else {
		    	end = this.importedAssociation.createOwnedEnd(phrase, participant);
		    }
		    end.setLower(lowerBound);
		    end.setUpper(upperBound);
		    end.setAggregation(AggregationKind.NONE_LITERAL);
		}
	}
}
