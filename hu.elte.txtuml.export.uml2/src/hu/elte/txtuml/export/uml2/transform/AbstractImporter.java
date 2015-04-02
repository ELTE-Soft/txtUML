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
	
	/**
	 * Writes an import warning with the given warning message to the standard output.
	 * @param msg The warning message.
	 *
	 * @author Ádám Ancsin
	 */
	protected static void importWarning(String msg)
	{
		System.out.println("Warning: " + msg);
	}
	
	/**
	 * Decides if the given class contains a state machine or not.
	 * @param sourceClass The given class.
	 * @return The decision.
	 *
	 * @author Ádám Ancsin
	 */
	protected static boolean containsStateMachine(Class<?> sourceClass)
	{
		for(Class<?> c : sourceClass.getDeclaredClasses())
		{
			if(ElementTypeTeller.isVertex(c))
				return true;
	    }
		return false;
    }
	
	/**
	 * Decides if the given region contains an initial pseudostate.
	 * @param region The region.
	 * @return The decision.
	 *
	 * @author Ádám Ancsin
	 */
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
	
	/**
	 * The class of the txtUML model being imported.
	 */
	protected static Class<?> modelClass=null;
}
