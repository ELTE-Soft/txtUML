package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;

/**
 * Represents an importer. Every importer must extend this class or one of it's subclasses.
 * @author Ádám Ancsin
 *
 */
abstract class AbstractImporter {
	
	protected static void importWarning(String msg)
	{
		System.out.println("Warning: " + msg);
	}
	
	protected static boolean containsStateMachine(Class<?> sourceClass)
	{
		for(Class<?> c : sourceClass.getDeclaredClasses())
		{
			if(ElementTypeTeller.isVertex(c))
				return true;
	    }
		return false;
    }
	
	protected static boolean containsInitial(Region region)
	{
		for(Object vert: region.getSubvertices().toArray())
		{
			if(	vert instanceof Pseudostate && 
				((Pseudostate) vert).getKind()==PseudostateKind.INITIAL_LITERAL
			)
			return true;
		}
		return false;
	}
	
	protected static Class<?> modelClass=null;
}
