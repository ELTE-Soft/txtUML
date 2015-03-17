package hu.elte.txtuml.export.uml2.transform;



import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.ElementModifiersAssigner;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ProfileCreator;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.InstancesMap;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;


public class ModelImporter extends AbstractImporter{
	
	private static void initModelImport(String className, String path) throws ImportException
	{
		importing=true;
		modelClass= findModel(className);
	
		String modelName=modelClass.getSimpleName();
		Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(modelName);
        
        initResourceSet();
		
        try
        {
        	URI uri = URI.createURI(path).appendSegment(className).appendFileExtension(UMLResource.FILE_EXTENSION);
	        Resource resource = resourceSet.createResource(uri);
	        resource.getContents().add(model);
        }
        catch(Exception e)
        {
        	//e.printStackTrace();
        }
        currentModel=model;
        
        initGlobalInstancesMap();
        ProfileCreator.createProfileForModel(path,modelName,resourceSet);
        loadAndApplyProfile();
        importPrimitiveTypes();
	}
	
	private static void initGlobalInstancesMap() {
		
		globalInstances=InstancesMap.create();
		globalInstances.put(ModelInt.ONE, InstanceInformation.createLiteral("1"));
		globalInstances.put(ModelInt.ZERO, InstanceInformation.createLiteral("0"));
		globalInstances.put(ModelBool.TRUE, InstanceInformation.createLiteral("true"));
		globalInstances.put(ModelBool.FALSE, InstanceInformation.createLiteral("false"));
		globalInstances.put(ModelBool.ELSE, InstanceInformation.createLiteral("else"));
		
	}

	private static void endModelImport()
	{
		globalInstances.clear();
		importing=false;
	}
	
	private static void importModelElements() throws ImportException
	{
		importClassesAndSignals();
		importAssociations();
		importSignals();
		importGeneralizations();
		importClassifierAttributes();
		importMemberFunctionsWithoutBodies();
		importClassOperationBodiesStateMachinesAndNestedSignals();
	}
	public static Model importModel(String className,String path) throws ImportException
	{
		
        initModelImport(className, path);
		importModelElements();	
		endModelImport();
		
   		return currentModel; 	  
	}
	
	public static Profile getProfile()
	{
		return currentProfile;
	}
	
	private static void loadAndApplyProfile()
	{
		Resource resource = resourceSet.getResource(URI.createFileURI("").appendSegment("Profile_"+currentModel.getName()).appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION),true);
		currentProfile=(Profile) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PROFILE);
		currentModel.applyProfile(currentProfile);
	}
	
	private static void importGeneralizations() throws ImportException 
	{
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			else if(ElementTypeTeller.isSpecificClassifier(c))	 
			{
				createGeneralization(c,c.getSuperclass());
			}
		}
	}
	
	private static void createGeneralization(Class<?> specific, Class<?> general)
	{
		Classifier uml2SpecClassifier = 
				(Classifier) currentModel.getOwnedMember(specific.getSimpleName());
		
		Classifier uml2GeneralClassifier = 
				(Classifier) currentModel.getOwnedMember(general.getSimpleName());
		
		uml2SpecClassifier.createGeneralization(uml2GeneralClassifier);
	}
	
	private static Class<?> findModel(String className) throws ImportException {
		try {
			Class<?> ret = Class.forName(className);
			if(!hu.elte.txtuml.api.Model.class.isAssignableFrom(ret)) {
				throw new ImportException("A subclass of Model is expected, got: " + className);
			}
			return ret;
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }

	public static boolean instructionImport() {
		return MethodImporter.isImporting();
	}
	
	private static void importPrimitiveTypes()
	{
		UML2Integer=(PrimitiveType) currentProfile.getImportedMember("Integer");
		UML2Bool=(PrimitiveType) currentProfile.getImportedMember("Boolean");
		UML2String=(PrimitiveType) currentProfile.getImportedMember("String");
		UML2Real=(PrimitiveType) currentProfile.getImportedMember("Real");
		UML2UnlimitedNatural=(PrimitiveType) currentProfile.getImportedMember("UnlimitedNatural");	
	}
	private static void importClassifier(Class<?> sourceClass) throws ImportException
	{
		org.eclipse.uml2.uml.Class importedClass = null;
		
		if(ElementTypeTeller.isClass(sourceClass))
		{
			importedClass =	currentModel.createOwnedClass(
					sourceClass.getSimpleName(),Modifier.isAbstract(sourceClass.getModifiers())
				);
			
			if(ElementTypeTeller.isExternalClass(sourceClass))
			{
				try
				{
					importedClass.applyStereotype(currentProfile.getOwnedStereotype("ExternalClass"));
				}
				catch(Exception e)
				{
					throw new ImportException("Error: cannot apply stereotype ExternalClass to class: "+sourceClass.getCanonicalName());
				}
			}
		}
		else
			createSignalAndSignalEvent(sourceClass);
		
	}
	private static void importClassesAndSignals() throws ImportException 
	{	
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			else if(ElementTypeTeller.isClassifier(c))
			{
				importClassifier(c);
			}	
		}
	}
	
	private static void importAssociations() throws ImportException
	{
		for(Class<?> sourceClass : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(sourceClass))
			{
				throw new ImportException(sourceClass.getName()+" is a non-txtUML class found in model.");
			}
			else if(ElementTypeTeller.isAssociation(sourceClass)) 
			{
				new AssociationImporter(sourceClass,currentModel).importAssociation();
			}
		}
	}	
	
	private static void importSignals() throws ImportException
	{
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(ElementTypeTeller.isEvent(c)) 
			{
				createSignalAndSignalEvent(c);
			}
		}
	}
	
	private static void createSignalAndSignalEvent(Class<?> sourceClass) throws ImportException
	{
		Signal signal = (Signal)currentModel.createOwnedType(sourceClass.getSimpleName(),UMLPackage.Literals.SIGNAL);
		
		SignalEvent signalEvent =(SignalEvent) currentModel.createPackagedElement(signal.getName()+"_event",UMLPackage.Literals.SIGNAL_EVENT);
		signalEvent.setSignal(signal);
	}
		
    private static void importClassifierAttributes() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			else if(ElementTypeTeller.isClassifier(c)) 
			{
				org.eclipse.uml2.uml.Classifier classifier=
						(org.eclipse.uml2.uml.Classifier) currentModel.getMember(c.getSimpleName());
	            
				for(Field f : c.getDeclaredFields()) 
				{		
	                if(ElementTypeTeller.isAttribute(f)) 
	                {
	                	importAttribute(classifier,f);
	                }
	            }
			}
		}
    }
    
    private static void importAttribute(Classifier owner, Field field) throws ImportException
    {
    	String fieldName=field.getName();
    	org.eclipse.uml2.uml.Type fieldType=importType(field.getType());
    	
    	Property property=null;
    	if(owner instanceof Signal)
    	{
    		property=((Signal) owner).createOwnedAttribute(fieldName,fieldType);
    	}
    	else if(owner instanceof org.eclipse.uml2.uml.Class)
    	{
    		property=((org.eclipse.uml2.uml.Class) owner).createOwnedAttribute(fieldName,fieldType);
    	}
    	else
    	{
    		throw new ImportException(owner.getName()+" is not a Class nor a Signal.");
    	}
    	ElementModifiersAssigner.setModifiers(property,field);
    }
    
    static org.eclipse.uml2.uml.Type importType(Class<?> sourceClass) 
    {
        if(sourceClass == ModelInt.class) 
        {
        	return UML2Integer;
        }
        else if(sourceClass == ModelBool.class) 
        {
        	return UML2Bool;
        }
        else if(sourceClass == ModelString.class) 
        {
        	return UML2String;
        }
        else if(ElementTypeTeller.isClass(sourceClass))
        {
        	return currentModel.getOwnedType(sourceClass.getSimpleName());
        }
        else
        {
        	return null;
        }
    }
	
    private static void importMemberFunctionsWithoutBodies() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(ElementTypeTeller.isModelClass(c)) 
			{
				createClassMemberFunctions(c);
			}
		}
    }

    private static void createClassMemberFunctions( Class<?> sourceClass)
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(sourceClass.getSimpleName());
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{
           
            Operation operation=importOperationWithoutBody(ownerClass,sourceClass,method);
            ElementModifiersAssigner.setModifiers(operation, method);
           
        }
    }
    
    private static void importClassMemberFunctionBodies( Class<?> sourceClass)
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(sourceClass.getSimpleName());
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{
           	importOperationBody(ElementFinder.findOperation(ownerClass, method.getName()),ownerClass,method);
        }
    }
    
 	private static void importClassOperationBodiesStateMachinesAndNestedSignals() throws ImportException
 	{
 		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(ElementTypeTeller.isModelClass(c)) 
			{
				org.eclipse.uml2.uml.Class currClass = (org.eclipse.uml2.uml.Class) currentModel.getOwnedMember(c.getSimpleName());
				
				importNestedSignals(c);
			
				createInstancesAndInitInstancesMap(c);
				
				if(containsStateMachine(c))
				{
					importStateMachine(currClass,c);
				}
				
				importClassMemberFunctionBodies(c);
				
				classAndFieldInstances.clear();
			}
		}
 	}
    
  	private static void createInstancesAndInitInstancesMap(Class<?> c) {
		
		classAndFieldInstances=InstancesMap.create();

  		selfInstance=(ModelClass) DummyInstanceCreator.createDummyInstance(c);
 
		classAndFieldInstances.put(selfInstance, InstanceInformation.create("self"));
		
		createNonLocalFieldsRecursively(selfInstance);
		
	}
  	
  	private static void createNonLocalFieldsRecursively(Object classifier)
  	{
  		createFieldsRecursively(classifier, false);
  	}

	private static void importNestedSignals(Class<?> sourceClass) throws ImportException
  	{
		for(Class<?> innerClass:sourceClass.getDeclaredClasses())
		{
			if(ElementTypeTeller.isEvent(innerClass) && currentModel.getOwnedType(innerClass.getSimpleName())==null)
			{
				//innerClass is an event and no signal in the model has the same name
				createSignalAndSignalEvent(innerClass);		
			}
		}
  	}

	private static StateMachine importStateMachine(org.eclipse.uml2.uml.Class ownerClass,Class<?> sourceClass) 
											throws ImportException
	{	
		
        StateMachine stateMachine = (StateMachine) ownerClass.createClassifierBehavior(ownerClass.getName(),UMLPackage.Literals.STATE_MACHINE);
        Region region = new RegionImporter
        		(sourceClass,selfInstance,currentModel,stateMachine.createRegion(stateMachine.getName()))
        		.importRegion();
        
        
       
        if(region.getSubvertices().size() != 0 && !containsInitialState(region)) 
        {
        	importWarning(sourceClass.getName() + " has one or more states but no initial state (state machine will not be created)");
        	return null;
        }
        return stateMachine; 
    }
	
	private static Activity importOperationBody
		(Operation operation,org.eclipse.uml2.uml.Class ownerClass,Method sourceMethod)
	{
		Activity activity=(Activity) ownerClass.createOwnedBehavior(sourceMethod.getName(),UMLPackage.Literals.ACTIVITY);
		activity.setSpecification(operation);
		MethodImporter.importMethod(currentModel,activity,sourceMethod,selfInstance);
		
		
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
	

    private static void initResourceSet()
    {
    	 resourceSet = new ResourceSetImpl();
          // Initialize registrations of resource factories, library models,
          // profiles, Ecore metadata, and other dependencies required for
          // serializing and working with UML resources. This is only necessary in
          // applications that are not hosted in the Eclipse platform run-time, in
          // which case these registrations are discovered automatically from
          // Eclipse extension points.
  		
  		URI locationURI=URI.createURI("platform:/plugin/org.eclipse.uml2.uml.resources");
  		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
  		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,UMLResource.Factory.INSTANCE);
  		 
  		Map<URI,URI> uriMap=resourceSet.getURIConverter().getURIMap();
  		  
  		uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), locationURI.appendSegment("libraries").appendSegment(""));
  		uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), locationURI.appendSegment("metamodels").appendSegment(""));
  		uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), locationURI.appendSegment("profiles").appendSegment(""));
          
  		UMLResourcesUtil.init(resourceSet);
    }
    
    public static ResourceSet getResourceSet()
    {
    	return resourceSet;
    }
    
    public static boolean isImporting()
    {
    	return importing;
    }
    private static Profile currentProfile=null;
    private static ResourceSet resourceSet;
	private static Model currentModel=null;
	private static boolean importing=false;
	
}

