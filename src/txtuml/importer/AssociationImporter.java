package txtuml.importer;

import java.lang.reflect.ParameterizedType;

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
	    Class<?>[] classes = sourceClass.getDeclaredClasses();
	    if(classes.length != 2)
	    {
	        throw new ImportException("Associations must have exactly two fields. "
	                                  + sourceClass.getSimpleName() + " has "
	                                  + Integer.toString(classes.length) + ".");
	    }
	    
	    currentAssociation = createAssociation(classes);
	    return currentAssociation;
	}

	private Association createAssociation(Class<?>[] classes) throws ImportException
	{
		AssociationEnd end1=importAssociationEnd(classes[0]);
	    AssociationEnd end2=importAssociationEnd(classes[1]);
	    
	    Association assoc=end1.getType().createAssociation
	     		(end2.isNavigable(), end2.getAggregationKind(), end2.getName() ,end2.getLowerBound(), end2.getUpperBound(),
	      		 end2.getType(), end1.isNavigable(), end1.getAggregationKind(), end1.getName() , end1.getLowerBound(), end1.getUpperBound());
	    
	    assoc.setName(sourceClass.getSimpleName());
	    return assoc;
	}
	
	private  AssociationEnd importAssociationEnd(Class<?> cl) throws ImportException
	{
	    String assoc = cl.getSimpleName();
	    ParameterizedType scl = (ParameterizedType)cl.getGenericSuperclass();
	    String multiplicityError = "Association ends have to extend one of the generic classes 'One', 'MaybeOne', 'Some', 'Many'.";
	    if(scl == null) {
	    	throw new ImportException(assoc + ": " + multiplicityError);
	    }
	    java.lang.reflect.Type[] tpars = scl.getActualTypeArguments();
	    if(tpars == null || tpars.length != 1) {
	    	throw new ImportException(assoc + ": " + multiplicityError);
	    }
	    Class<?> parCl = (Class<?>)tpars[0];
	    String className = parCl.getSimpleName();
	    org.eclipse.uml2.uml.Type participant = (Type) currentModel.getMember(className);
	        
	    if(participant == null)
	    {
	        throw new ImportException(assoc + ": No class " + className + " found in this model.");
	    }

	    return createAssociationEnd(participant,assoc,cl.getSuperclass());
	}
	
	private static AssociationEnd createAssociationEnd(org.eclipse.uml2.uml.Type participant,String phrase,Class<?> mult)
									throws ImportException
	{
		int lowerBound;
	    int upperBound;
	    
	    //converting multiplicity
	    if(mult.equals(txtuml.api.Association.One.class))
	    {
	        lowerBound=upperBound=1;
	    }
	    else if(mult.equals(txtuml.api.Association.MaybeOne.class))
	    {
	        lowerBound=0;
	        upperBound=1;
	    }
	    else if(mult.equals(txtuml.api.Association.Some.class))
	    {
	        lowerBound=0;
	        upperBound=org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
	    }
	    else if(mult.equals(txtuml.api.Association.Many.class))
	    {
	        lowerBound=1;
	        upperBound=org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
	    }
	    else
	    {
	        throw new ImportException(phrase + ": has invalid multiplicity.");           
	    }
	
	    return new AssociationEnd(participant,phrase,false,AggregationKind.NONE_LITERAL,lowerBound,upperBound);
	}
	private Class<?> sourceClass;
	private Model currentModel;
	private Association currentAssociation;
}
