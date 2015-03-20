package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;

import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;

abstract class AbstractImporter {
	
	protected static void importWarning(String msg) {
		System.out.println("Warning: " + msg);
	}
	
	protected static boolean containsStateMachine(Class<?> sourceClass){
		for(Class<?> c : sourceClass.getDeclaredClasses()){
			if(ElementTypeTeller.isState(c)){
				return true;
			}
	    }
		return false;
    }
	
	protected static boolean containsInitialState(Region region)
	{
		for(Object vert: region.getSubvertices().toArray())
		{
			if(vert instanceof Pseudostate)
			{
				if(((Pseudostate) vert).getKind()==PseudostateKind.INITIAL_LITERAL)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	protected static Class<?> modelClass=null;
	
	

}
