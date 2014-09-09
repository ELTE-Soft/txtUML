package txtuml.importer;



import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import txtuml.api.ModelBool;
import txtuml.api.ModelClass;
import txtuml.api.ModelInt;
import txtuml.api.ModelString;
import txtuml.importer.AssociationImporter;

import java.lang.Class;
import java.lang.reflect.*;


public class ModelImporter extends AbstractImporter{
	
	public static Model importModel(String className) throws ImportException {
		modelClass= findModel(className);
		Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(className);
       
        currentModel=model;
        
        createPrimitiveTypes();
		importClassNames();
		importAssociations();
		importSignals();
		importOperationsWithoutBodies();
		importClassAttributes();
		importMemberFunctionsWithoutBodies();
		importOperationBodies();
		importMemberFunctionBodies();
		importClassStateMachines();
		
   		return currentModel;
  
	}
	
	
	private static Class<?> findModel(String className) throws ImportException {
		try {
			Class<?> ret = Class.forName(className);
			if(!Model.class.isAssignableFrom(ret)) {
				//throw new ImportException("A subclass of Model is expected, got: " + className);
			}
			return ret;
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }

	public static boolean instructionImport() {
		return MethodImporter.isImporting();
	}
	
	private static void createPrimitiveTypes()
	{
		//TODO: import packages of UML types and use the types from there
		
		UML2Int=currentModel.createOwnedPrimitiveType("Int");
		UML2Bool=currentModel.createOwnedPrimitiveType("Bool");
		UML2String=currentModel.createOwnedPrimitiveType("String");
		
	}
	
	private static void importClassNames() throws ImportException 
	{
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(isClass(c)) 
			{
				currentModel.createOwnedClass(c.getSimpleName(),Modifier.isAbstract(c.getModifiers()));
			}
		}
	}
	
	private static void importAssociations() throws ImportException
	{
		for(Class<?> sourceClass : modelClass.getDeclaredClasses()) 
		{
			if(!isModelElement(sourceClass))
			{
				throw new ImportException(sourceClass.getName()+" is a non-txtUML class found in model.");
			}
			if(isAssociation(sourceClass)) 
			{
				new AssociationImporter(sourceClass,currentModel).importAssociation();
			}
		}
	}	
	
	private static void importSignals() throws ImportException
	{
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(isEvent(c)) 
			{
				createSignalAndEvent(c);
			}
		}
	}
	
	
	private static void createSignalAndEvent(Class<?> sourceClass) throws ImportException
	{
		Signal signal = (Signal)currentModel.createOwnedType(sourceClass.getSimpleName(),UMLPackage.Literals.SIGNAL);
		
		importSignalAttributes(signal, sourceClass);
		
		SignalEvent signalEvent =(SignalEvent) currentModel.createPackagedElement(signal.getName()+"_event",UMLPackage.Literals.SIGNAL_EVENT);
		signalEvent.setSignal(signal);
	}
	
	
	private static void importSignalAttributes(Signal signal, Class<?> sourceClass) throws ImportException
	{
		for(Field f : sourceClass.getDeclaredFields()) 
		{
	        if(isAttribute(f)) 
	        {
	        	createSignalAttribute(signal,f);
	        }
	    }
	}
	
	private static void importOperationsWithoutBodies()
	{
		org.eclipse.uml2.uml.Class dummyClass=currentModel.createOwnedClass("DummyClassForMethods", false);
	    for(Method method : modelClass.getDeclaredMethods()) 
	    {
	        
            importOperationWithoutBody(dummyClass,modelClass,method);
	        
	    }
	}
	
	private static void importOperationBodies()
	{
		org.eclipse.uml2.uml.Class dummyClass	=	(org.eclipse.uml2.uml.Class) 
					currentModel.getOwnedMember("DummyClassForMethods");
	    for(Method method : modelClass.getDeclaredMethods()) 
	    {
	        
            importOperationBody(MethodImporter.getOperation(dummyClass,method.getName()),dummyClass,modelClass,method);
	       
	    }
	}
	
    private static void importClassAttributes() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(isClass(c)) 
			{
				org.eclipse.uml2.uml.Class currClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(c.getSimpleName());
	            
				for(Field f : c.getDeclaredFields()) 
				{		
	                if(isAttribute(f)) 
	                {
	                	createClassAttribute(currClass,f);
	                }
	            }
			}
		}
    }
    
    private static void createClassAttribute(org.eclipse.uml2.uml.Class ownerClass, Field field)
    {
    	int upperBound=1;
    	int lowerBound=1;
    	
    	org.eclipse.uml2.uml.Type fieldType=importType(field.getType());
    	
    	ownerClass.createOwnedAttribute(field.getName(),fieldType,lowerBound,upperBound);
    }
    
    private static void createSignalAttribute(org.eclipse.uml2.uml.Signal signal, Field field)
    {
    	int upperBound=1;
    	int lowerBound=1;
    	
    	org.eclipse.uml2.uml.Type fieldType=importType(field.getType());
    	
    	signal.createOwnedAttribute(field.getName(),fieldType,lowerBound,upperBound);
    }
    
    static org.eclipse.uml2.uml.Type importType(Class<?> sourceClass) 
    {
        if(sourceClass == ModelInt.class) 
        {
        	return UML2Int;
        }
        if(sourceClass == ModelBool.class) 
        {
        	return UML2Bool;
        }
        if(sourceClass == ModelString.class) 
        {
        	return UML2String;
        }
        if(ModelClass.class.isAssignableFrom(sourceClass))
        {
        	return currentModel.getOwnedType(sourceClass.getSimpleName());
        }
        return null;
    }
	
    private static void importMemberFunctionsWithoutBodies() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(isClass(c)) 
			{
				createClassMemberFunctions(c);
			}
		}
    }
    private static void importMemberFunctionBodies() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(isClass(c)) 
			{
				importClassMemberFunctionBodies(c);
			}
		}
    }
    
    private static void createClassMemberFunctions( Class<?> sourceClass)
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(sourceClass.getSimpleName());
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{
            if(isMemberFunction(method)) 
            {    
            	importOperationWithoutBody(ownerClass,sourceClass,method);;
            }
        }
    }
    
    private static void importClassMemberFunctionBodies( Class<?> sourceClass)
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(sourceClass.getSimpleName());
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{
            if(isMemberFunction(method)) 
            {    
            	importOperationBody(MethodImporter.getOperation(ownerClass, method.getName()),ownerClass,sourceClass,method);
            }
        }
    }
    
  	private static void importClassStateMachines() throws ImportException
	{
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(isClass(c)) 
			{
				org.eclipse.uml2.uml.Class currClass = (org.eclipse.uml2.uml.Class) currentModel.getOwnedMember(c.getSimpleName());
				
				if(isContainsStateMachine(c))
				{
					importStateMachine(currClass,c);
				}
				
			}
		}
	}
    
	private static StateMachine importStateMachine(org.eclipse.uml2.uml.Class ownerClass,Class<?> sourceClass) 
											throws ImportException
	{	
		
        StateMachine stateMachine = (StateMachine) ownerClass.createClassifierBehavior(ownerClass.getName(),UMLPackage.Literals.STATE_MACHINE);
        Region region = new RegionImporter(sourceClass,currentModel,stateMachine.createRegion(stateMachine.getName())).importRegion();
        
        
       
        if(region.getSubvertices().size() != 0 && !isContainsInitialState(region)) 
        {
        	importWarning(sourceClass.getName() + " has one or more states but no initial state (state machine will not be created)");
        	return null;
        }
        return stateMachine; 
    }
	
	private static Activity importOperationBody
		(Operation operation,org.eclipse.uml2.uml.Class ownerClass,Class<?> sourceClass,Method sourceMethod)
	{
		Activity activity=(Activity) ownerClass.createOwnedBehavior(sourceMethod.getName(),UMLPackage.Literals.ACTIVITY);
		activity.setSpecification(operation);
		MethodImporter.importMethod(currentModel,activity,sourceMethod,sourceClass);
		
		
		return activity;
	}
	
	private static Operation createOperationReturnResult(Operation operation,Method sourceMethod)
	{
		Class<?> returnTypeClass = sourceMethod.getReturnType();
		if(returnTypeClass!=void.class)
		{
			Type returnType=ModelImporter.importType(returnTypeClass);
			operation.createReturnResult("return",returnType);
		}
		return operation;
	}
	
	private static Operation createOwnedOperation(org.eclipse.uml2.uml.Class ownerClass,Method sourceMethod)
	{
		BasicEList<String> paramNames=new BasicEList<String>();
		BasicEList<Type> paramTypes=new BasicEList<Type>();
		
		int i=0;
		for(Class<?> paramTypeClass : sourceMethod.getParameterTypes())
		{
			Type paramType=ModelImporter.importType(paramTypeClass);
			paramTypes.add(paramType);
			paramNames.add("arg"+i);
			++i;
		
		}
	
		return ownerClass.createOwnedOperation(sourceMethod.getName(),paramNames,paramTypes);
		
	}
	private static Operation importOperationWithoutBody(org.eclipse.uml2.uml.Class ownerClass,Class<?> sourceClass,Method sourceMethod)
	{
		Operation operation = createOwnedOperation(ownerClass, sourceMethod);
		createOperationReturnResult(operation, sourceMethod);
		return operation;
	}
	
	public static Class<?> getModelClass() {
		return modelClass;
	}
	
	private static PrimitiveType UML2Int,UML2Bool,UML2String;
	private static Model currentModel=null;
	private static Class<?> modelClass=null;
	
	
}

