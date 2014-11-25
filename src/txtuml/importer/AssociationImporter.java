package txtuml.importer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;

class AssociationImporter extends AbstractImporter{
	
	
	AssociationImporter(Class<?> sourceClass, Model currentModel)
	{
		this.sourceClass=sourceClass;
		this.currentModel=currentModel;
	}
	
	Association getAssociation()
	{
		return currentAssociation;
	}
	Association importAssociation() throws ImportException
	{
	    List<Field> fields = new LinkedList<Field>(Arrays.asList(sourceClass.getDeclaredFields()));
	    try
	    {
	    	fields.remove(sourceClass.getDeclaredField("this$0"));
	    }
	    catch( NoSuchFieldException n )
	    {
	    }
	    if(fields.size() != 2)
	    {
	        throw new ImportException("Associations must have exactly two fields. "
	                                  + sourceClass.getSimpleName() + " has "
	                                  + Integer.toString(fields.size()) + ".");
	    }
	    
	    currentAssociation= createAssociation(fields);
	    return currentAssociation;
	}

	private Association createAssociation(List<Field> fields) throws ImportException
	{
		AssociationEnd end1=importAssociationEnd(fields.get(0));
	    AssociationEnd end2=importAssociationEnd(fields.get(1));
	    
	    Association assoc=end1.getType().createAssociation
	     		(end2.isNavigable(), end2.getAggregationKind(), end2.getName() ,end2.getLowerBound(), end2.getUpperBound(),
	      		 end2.getType(), end1.isNavigable(), end1.getAggregationKind(), end1.getName() , end1.getLowerBound(), end1.getUpperBound());
	    
	    assoc.setName(sourceClass.getSimpleName());
	    return assoc;
	}
	
	private  AssociationEnd importAssociationEnd(Field field) throws ImportException
	{
	    String phrase = field.getName();
	    String className = field.getType().getSimpleName();
	    org.eclipse.uml2.uml.Type participant = (Type) currentModel.getMember(className);
	        
	    if(participant == null)
	    {
	        throw new ImportException(phrase + ": No class " + className + " found in this model.");
	    }
	    Annotation[] annotations = field.getDeclaredAnnotations();
	    
	    if(annotations.length != 1)
	    {
	    	throw new ImportException(phrase + ": cannot determine multiplicity (Number of annotations must be 1, found "
	                              + Integer.toString(annotations.length) + ").");
	    }
	        
	    return createAssociationEnd(participant,phrase,annotations);
	}
	
	private static AssociationEnd createAssociationEnd(org.eclipse.uml2.uml.Type participant,String phrase,Annotation[] annotations)
									throws ImportException
	{
            // TODO import associations
	    /*
		int lowerBound;
	    int upperBound;
	    
	    //converting multiplicity
	    if(annotations[0].annotationType().equals(txtuml.api.One.class))
	    {
	        lowerBound=upperBound=1;
	    }
	    else if(annotations[0].annotationType().equals(txtuml.api.MaybeOne.class))
	    {
	        lowerBound=0;
	        upperBound=1;
	    }
	    else if(annotations[0].annotationType().equals(txtuml.api.Some.class))
	    {
	        lowerBound=0;
	        upperBound=org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
	    }
	    else if(annotations[0].annotationType().equals(txtuml.api.Many.class))
	    {
	        lowerBound=1;
	        upperBound=org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
	    }
	    else
	    {*/
	        throw new ImportException(phrase + ": has invalid multiplicity.");           
	    /*}
	
	    return new AssociationEnd(participant,phrase,false,AggregationKind.NONE_LITERAL,lowerBound,upperBound);*/
	}
	private Class<?> sourceClass;
	private Model currentModel;
	private Association currentAssociation;
}
