package hu.elte.txtuml.export.uml2.transform.backend;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.FieldValueAccessor;

/**
 * This class is responsible for managing dummy instances during model import.
 * @author Ádám Ancsin
 *
 */
public final class InstanceManager {

	/**
	 * Initializes the global instances map. Creates map entries for the global constants.
	 * 
	 * @author Ádám Ancsin
	 */
	public static void initGlobalInstancesMap() 
	{
		globalInstances=InstancesMap.create();
		globalInstances.put(ModelInt.ONE, InstanceInformation.createLiteral("1"));
		globalInstances.put(ModelInt.ZERO, InstanceInformation.createLiteral("0"));
		globalInstances.put(ModelBool.TRUE, InstanceInformation.createLiteral("true"));
		globalInstances.put(ModelBool.FALSE, InstanceInformation.createLiteral("false"));
		globalInstances.put(ModelBool.ELSE, InstanceInformation.createLiteral("else"));
	}

	/**
	 * Initializes the classAndFieldInstance map. Creates a dummy instance of the specified class
	 * and all of it's fields recursively. (e.g. if the class is a model class and has a field which's type is another
	 * model class, the latter's fields are also instantiated, and so on)
	 * @param sourceClass The specified class.
	 *
	 * @author Ádám Ancsin
	 */
	public static void createClassAndFieldInstancesAndInitClassAndFieldInstancesMap(Class<?> sourceClass) 
	{	
		classAndFieldInstances=InstancesMap.create();
		selfInstance=(ModelClass) DummyInstanceCreator.createDummyInstance(sourceClass);
		classAndFieldInstances.put(selfInstance, InstanceInformation.create("self"));
		createNonLocalFieldsRecursively(selfInstance);
	}
	
	/**
	 * Creates a local instances map entry for the specified instance with the given instance information.
	 * @param instance The specified instance.
	 * @param instanceInfo The given instance information for the specified instance.
	 *
	 * @author Ádám Ancsin
	 */
	public static void createLocalInstancesMapEntry(Object instance, InstanceInformation instanceInfo)
	{
		localInstances.put(instance, instanceInfo);
	}
	
	/**
	 * Initializes the local instances map.
	 * 
	 * @author Ádám Ancsin
	 */
	public static void initLocalInstancesMap()
	{
		localInstances = InstancesMap.create();
	}
	
	/**
	 * Creates dummy instances, generates instance information and creates instance map entries
	 * of the specified classifier instance's fields recursively.
	 * @param classifier The specified classifier.
	 * @param local Indicates that the instances will be local or non-local.
	 *
	 * @author Ádám Ancsin
	 */
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


	/**
	 * Creates local dummy instances, generates instance information and creates instance map entries
	 * of the specified classifier instance's fields recursively.
	 * @param classifier The specified classifier.
	 *
	 * @author Ádám Ancsin
	 */
	public static void createLocalFieldsRecursively(Object classifier)
	{
		createFieldsRecursively(classifier, true);
	}

	/**
	 * Creates non-local dummy instances, generates instance information and creates instance map entries
	 * of the specified classifier instance's fields recursively.
	 * @param classifier The specified classifier.
	 *
	 * @author Ádám Ancsin
	 */
	public static void createNonLocalFieldsRecursively(Object classifier)
	{
		createFieldsRecursively(classifier, false);
	}
	
	/**
	 * Clears the global instances map. 
	 *
	 * @author Ádám Ancsin
	 */
	public static void clearGlobalInstancesMap()
	{
		globalInstances.clear();
	}
	
	/**
	 * Clears the local instance map.
	 * 
	 * @author Ádám Ancsin
	 */
	public static void clearLocallInstancesMap()
	{
		localInstances.clear();
	}
	
	/**
	 * Clears the class and field instances map.
	 * 
	 * @author Ádám Ancsin
	 */
	public static void clearClassAndFieldInstancesMap()
	{
		classAndFieldInstances.clear();
	}
	
	/**
	 * Gets the "self" instance.
	 * @return The "self" instance.
	 *
	 * @author Ádám Ancsin
	 */
	public static ModelClass getSelfInstance()
	{
		return selfInstance;
	}
	
	/**
	 * Gets instance information for the specified instance.
	 * @param instance The specified instance.
	 * @return Instance information for the specified instance. Null, if not found in records.
	 *
	 * @author Ádám Ancsin
	 */
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
	
	/**
	 * Decides if the specified instance is a literal.
	 * @param instance The specified instance.
	 * @return The decision.
	 *
	 * @author Ádám Ancsin
	 */
	public static boolean isInstanceLiteral(ModelElement instance) 
	{	
		InstanceInformation instInfo=getInstanceInfo(instance);
		return instInfo!=null && instInfo.isLiteral();
	}
	
	/**
	 * Decides if the specified instance is a calculated instance.
	 * @param instance The specified instance.
	 * @return The decision.
	 *
	 * @author Ádám Ancsin
	 */
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

	/**
	 * A map for storing information for global instances.
	 */
	private static InstancesMap globalInstances;
	/**
	 * A map for storing information for the dummy instances of the class being imported
	 * and it's fields.
	 */
	private static InstancesMap classAndFieldInstances;
	/**
	 * A map for storing information for dummy instances that are considered local.
	 * (e.g. local variables in a method, results of method calls in a method body, etc.)
	 */
	private static InstancesMap localInstances;
	/**
	 * The "sef" instance.
	 */
	private static ModelClass selfInstance=null;
}
