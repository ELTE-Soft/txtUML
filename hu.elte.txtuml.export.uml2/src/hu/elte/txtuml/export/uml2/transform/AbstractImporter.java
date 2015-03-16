package hu.elte.txtuml.export.uml2.transform;

import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstancesMap;
import hu.elte.txtuml.export.uml2.transform.backend.ModelElementInformation;

import java.lang.reflect.Field;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;

abstract class AbstractImporter {
	
	protected static ModelElementInformation getInstanceInfo(Object instance)
	{
		ModelElementInformation ret;
		
		ret = globalInstances.get(instance);
		
		if(ret != null)
			return ret;
		
		ret = classAndFieldInstances.get(instance);
		
		if(ret != null) 
			return ret;
		
		ret = localInstances.get(instance);
		
		return ret;
	}
	

	protected static void createFieldsRecursively(Object classifier, boolean local)
  	{
  		InstancesMap instancesMap;
  		
  		if(local)
  			instancesMap = localInstances;
  		else
  			instancesMap = classAndFieldInstances;
		
  		if(classifier != null)
  		{
  			String classifierExpr = getInstanceInfo(classifier).getExpression();
  	  		
  	  		for(Field field: classifier.getClass().getDeclaredFields())
  	  		{
  	  			Class<?> fieldType = field.getType();
  	  			String fieldName = field.getName();
  	  			Object fieldInst =  DummyInstanceCreator.createDummyInstance(fieldType);
  	  			setObjectFieldVal(classifier,fieldName, fieldInst);
  	  			String fieldExpr = classifierExpr+"."+fieldName;
  	  			
  	  			ModelElementInformation elementInfo = new ModelElementInformation(fieldExpr,false,false);
  	  			if(fieldInst != null)
  	  			{
  	 
  	  				instancesMap.put(fieldInst,elementInfo);
  	  	  			if(ElementTypeTeller.isClassifier(fieldType))
  	  	  				createFieldsRecursively(fieldInst,local);
  	  	  			
  	  			}
  	  			
  	  		}
  		}
		
  	}
	
	protected static void setObjectFieldVal(Object object, String fieldName,Object newVal)
	{
		Field field = ElementFinder.findField(object.getClass(),fieldName);
		
		if(field!=null)
		{
			field.setAccessible(true);
			try {
				field.set(object,newVal);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			field.setAccessible(false);
		}
	}
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
	
	protected static boolean isInstanceCalculated(ModelElement instance)
	{
		ModelElementInformation instInfo=getInstanceInfo(instance);
		boolean calculated;
		if(instInfo==null)
			calculated=false;
		else 
			calculated =instInfo.isCalculated();
		return calculated;
	}


	protected static PrimitiveType UML2Integer,UML2Bool,UML2String,UML2Real,UML2UnlimitedNatural;
	protected static Class<?> modelClass=null;
	protected static InstancesMap globalInstances;
	protected static InstancesMap classAndFieldInstances;
	protected static InstancesMap localInstances;
	protected static ModelClass selfInstance=null;
	

}
