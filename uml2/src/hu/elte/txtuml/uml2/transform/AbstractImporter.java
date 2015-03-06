package hu.elte.txtuml.uml2.transform;

import java.lang.reflect.*;
import java.util.WeakHashMap;


import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;

import hu.elte.txtuml.api.*;
import hu.elte.txtuml.uml2.utils.ElementFinder;
import hu.elte.txtuml.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.uml2.utils.ModelTypeInformation;

abstract class AbstractImporter {
	
	protected static Object getObjectFieldVal(Object object,String fieldName)
	{	
		Field field = ElementFinder.findField(object.getClass(),fieldName);
		return accessObjectFieldVal(object, field);	
	}
	
	protected static Object accessObjectFieldVal(Object object, Field field)
	{
		Object val=null;
		
		if(field!=null)
		{
			field.setAccessible(true);
			try {
				val = field.get(object);
				return val;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			field.setAccessible(false);
		}
		
		
		return val;
	}
	
	
	
	protected static void importWarning(String msg) {
		System.out.println("Warning: " + msg);
	}
	
	protected static boolean isContainsStateMachine(Class<?> sourceClass){
		for(Class<?> c : sourceClass.getDeclaredClasses()){
			if(ElementTypeTeller.isState(c)){
				return true;
			}
	    }
		return false;
    }
	
	protected static boolean isContainsInitialState(Region region)
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
	
	protected static void setLocalInstanceToBeCreated(boolean bool) {
		localInstanceToBeCreated = bool;
	}
	
	
	protected static boolean localInstanceToBeCreated = false;
	protected static PrimitiveType UML2Integer,UML2Bool,UML2String,UML2Real,UML2UnlimitedNatural;
	protected static Class<?> modelClass=null;
	protected static WeakHashMap<ModelType<?>, ModelTypeInformation> modelTypeInstancesInfo=null;


}
