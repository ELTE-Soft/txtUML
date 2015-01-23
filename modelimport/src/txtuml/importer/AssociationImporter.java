package txtuml.importer;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
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
	    List<Class<?> > classes = new LinkedList<Class<?> >(Arrays.asList(sourceClass.getDeclaredClasses()));
	   /* try
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
	    }*/
	    
	    currentAssociation= createAssociation(classes);
	    return currentAssociation;
	}

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
	
	private  AssociationEnd importAssociationEnd(Class sourceClass) throws ImportException
	{
	    String phrase = sourceClass.getSimpleName();
	    Class genericParameter0 =(Class)
	    		((ParameterizedType)sourceClass.getGenericSuperclass())
	    		.getActualTypeArguments()[0];
	    	    
	    
	    String className = genericParameter0.getSimpleName();
	   
	    int lowerBound; 
		int upperBound; 
	    
	    if(txtuml.api.Association.One.class.isAssignableFrom(sourceClass))
		{
			lowerBound=upperBound=1;
		}
		else if(txtuml.api.Association.MaybeOne.class.isAssignableFrom(sourceClass))
		{
			lowerBound=0;
			upperBound=1;
		}
		else if(txtuml.api.Association.Some.class.isAssignableFrom(sourceClass))
		{
			lowerBound=0;
			upperBound= org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		}
		else if(txtuml.api.Association.Many.class.isAssignableFrom(sourceClass))
		{
			lowerBound=1;
			upperBound= org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		}
		else
		{
			throw new ImportException("Invalid multiplicity.");            
		}
	    
	    org.eclipse.uml2.uml.Type participant = (Type) currentModel.getMember(className);
	    
	    if(participant == null)
	    {
	        throw new ImportException(phrase + ": No class " + className + " found in this model.");
	    }
	   
	    return new AssociationEnd(participant,phrase,false,AggregationKind.NONE_LITERAL,lowerBound,upperBound);
	}

	private Class<?> sourceClass;
	private Model currentModel;
	private Association currentAssociation;
}
