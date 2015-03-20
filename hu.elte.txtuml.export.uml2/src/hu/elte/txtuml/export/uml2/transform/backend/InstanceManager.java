package hu.elte.txtuml.export.uml2.transform.backend;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.FieldValueAccessor;

public class InstanceManager {

	public static void initGlobalInstancesMap() 
	{
		globalInstances=InstancesMap.create();
		globalInstances.put(ModelInt.ONE, InstanceInformation.createLiteral("1"));
		globalInstances.put(ModelInt.ZERO, InstanceInformation.createLiteral("0"));
		globalInstances.put(ModelBool.TRUE, InstanceInformation.createLiteral("true"));
		globalInstances.put(ModelBool.FALSE, InstanceInformation.createLiteral("false"));
		globalInstances.put(ModelBool.ELSE, InstanceInformation.createLiteral("else"));
	}

	public static void createClassAndFieldInstancesAndInitClassAndFieldInstancesMap(Class<?> c) 
	{	
		classAndFieldInstances=InstancesMap.create();
		selfInstance=(ModelClass) DummyInstanceCreator.createDummyInstance(c);
		classAndFieldInstances.put(selfInstance, InstanceInformation.create("self"));
		createNonLocalFieldsRecursively(selfInstance);
	}
	
	public static void createLocalInstancesMapEntry(Object instance, InstanceInformation instanceInfo)
	{
		localInstances.put(instance, instanceInfo);
	}
	public static void initLocalInstancesMap()
	{
		localInstances = InstancesMap.create();
	}
	
	private static void createFieldsRecursively(Object classifier, boolean local)
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
	  			FieldValueAccessor.setObjectFieldVal(classifier,fieldName, fieldInst);
	  			String fieldExpr = classifierExpr+"."+fieldName;
	  			
	  			InstanceInformation elementInfo = InstanceInformation.create(fieldExpr);
	  			if(fieldInst != null)
	  			{
	
	  				instancesMap.put(fieldInst,elementInfo);
	  				
	  	  			if(ElementTypeTeller.isClassifier(fieldType))
	  	  				createFieldsRecursively(fieldInst,local);
	  	  			
	  			}
	  			
	  		}
		}
		
	}

	public static void createLocalFieldsRecursively(Object classifier)
	{
		createFieldsRecursively(classifier, true);
	}

	public static void createNonLocalFieldsRecursively(Object classifier)
	{
		createFieldsRecursively(classifier, false);
	}
	
	public static void clearGlobalInstancesMap()
	{
		globalInstances.clear();
	}
	
	public static void clearLocallInstancesMap()
	{
		localInstances.clear();
	}
	
	public static void clearClassAndFieldInstancesMap()
	{
		classAndFieldInstances.clear();
	}
	
	public static ModelClass getSelfInstance()
	{
		return selfInstance;
	}
	
	public static InstanceInformation getInstanceInfo(Object instance)
	{
		InstanceInformation ret;
		
		ret = globalInstances.get(instance);
		
		if(ret != null)
			return ret;
		
		ret = classAndFieldInstances.get(instance);
		
		if(ret != null) 
			return ret;
		
		ret = localInstances.get(instance);
		
		return ret;
	}
	
	public static boolean isInstanceLiteral(ModelElement instance) 
	{	
		InstanceInformation instInfo=getInstanceInfo(instance);
		return instInfo!=null && instInfo.isLiteral();
	}
	
	public static boolean isInstanceCalculated(ModelElement instance)
	{
		InstanceInformation instInfo=getInstanceInfo(instance);
		boolean calculated;
		if(instInfo==null)
			calculated=false;
		else 
			calculated =instInfo.isCalculated();
		return calculated;
	}

	private static InstancesMap globalInstances;
	private static InstancesMap classAndFieldInstances;
	private static InstancesMap localInstances;
	private static ModelClass selfInstance=null;
	
}
