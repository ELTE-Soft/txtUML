package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.export.uml2.transform.backend.AssociationEnd;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;

/**
 * Instances of this class are responsible for importing associations.
 * @author Ádám Ancsin
 *
 */
class AssociationImporter extends AbstractImporter{
	
	/**
	 * Creates an AssocationImporter instance.
	 * @param sourceClass The class representing the txtUML association.
	 * @param currentModel The current UML2 model.
	 */
	AssociationImporter(Class<?> sourceClass, Model currentModel)
	{
		this.sourceClass=sourceClass;
		this.currentModel=currentModel;
	}
	
	/**
	 * Gets the current UML2 association.
	 * @return The current UML2 association.
	 *
	 * @author Ádám Ancsin
	 */
	Association getAssociation()
	{
		return currentAssociation;
	}
	
	/**
	 * Imports the association.
	 * @return The imported UML2 association.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	Association importAssociation() throws ImportException
	{
	    List<Class<?> > classes = new LinkedList<Class<?> >(Arrays.asList(sourceClass.getDeclaredClasses()));
	    
	    currentAssociation= createAssociation(classes);
	    return currentAssociation;
	}

	/**
	 * Creates the UML2 association.
	 * @param classes The declared classes (representing the association ends of the association) of the txtUML association.
	 * @return The created UML2 association.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private Association createAssociation(List<Class<?> > classes) throws ImportException
	{
		AssociationEnd end1=importAssociationEnd(classes.get(0));
	    AssociationEnd end2=importAssociationEnd(classes.get(1));
	    
	    Association assoc=end1.getType().createAssociation
	     		(end2.isNavigable(), end2.getAggregationKind(), end2.getName() ,end2.getLowerBound(), end2.getUpperBound(),
	      		 end2.getType(), end1.isNavigable(), end1.getAggregationKind(), end1.getName() , end1.getLowerBound(), end1.getUpperBound());
	    
	    assoc.setName(sourceClass.getSimpleName());
	    return assoc;
	}
	
	/**
	 * Imports an association end.
	 * @param sourceClass The class representing the txtUML association end.
	 * @return The imported association end.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	@SuppressWarnings("rawtypes")
	private  AssociationEnd importAssociationEnd(Class sourceClass) throws ImportException
	{
	    String phrase = sourceClass.getSimpleName();
	    Class genericParameter0 =(Class)
	    		((ParameterizedType)sourceClass.getGenericSuperclass())
	    		.getActualTypeArguments()[0];
	    	    
	    
	    String className = genericParameter0.getSimpleName();
	   
	    int lowerBound; 
		int upperBound; 
	    
	    if(hu.elte.txtuml.api.semantics.Multiplicity.One.class.isAssignableFrom(sourceClass))
			lowerBound=upperBound=1;
		else if(hu.elte.txtuml.api.semantics.Multiplicity.ZeroToOne.class.isAssignableFrom(sourceClass))
		{
			lowerBound=0;
			upperBound=1;
		}
		else if(hu.elte.txtuml.api.semantics.Multiplicity.ZeroToUnlimited.class.isAssignableFrom(sourceClass))
		{
			lowerBound=0;
			upperBound= org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		}
		else if(hu.elte.txtuml.api.semantics.Multiplicity.OneToUnlimited.class.isAssignableFrom(sourceClass))
		{
			lowerBound=1;
			upperBound= org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		}
		else
			throw new ImportException("Association end "+sourceClass.getName()+" has invalid multiplicity.");            
	    
	    boolean navigable;
	    
	    if(hu.elte.txtuml.api.semantics.Navigability.Navigable.class.isAssignableFrom(sourceClass))
	    	navigable = true;
	    else if(hu.elte.txtuml.api.semantics.Navigability.NonNavigable.class.isAssignableFrom(sourceClass))
	    	navigable = false;
	    else
	    	throw new ImportException("Association end "+sourceClass.getName()+" has invalid navigability.");    
	    
	    org.eclipse.uml2.uml.Type participant = (Type) currentModel.getMember(className);
	    
	    if(participant == null)
	        throw new ImportException(phrase + ": No class " + className + " found in this model.");
	   
	    return new AssociationEnd(participant,phrase,lowerBound,upperBound,AggregationKind.NONE_LITERAL, navigable);
	}

	private Class<?> sourceClass;
	private Model currentModel;
	private Association currentAssociation;
}
