package hu.elte.txtuml.export.uml2.transform;



import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.export.uml2.transform.AssociationImporter;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ImportException;
import hu.elte.txtuml.export.uml2.utils.ModelTypeInformation;

import java.io.IOException;
import java.lang.Class;
import java.lang.reflect.*;
import java.util.Map;
import java.util.WeakHashMap;


public class ModelImporter extends AbstractImporter{
	
	private static void initModelImport(String className, String path) throws ImportException
	{
		importing=true;
		modelClass= findModel(className);
	
		Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(modelClass.getSimpleName());
        
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
        

        initModelTypeInstancesInfo();
        createProfile(path);
        loadAndApplyProfile();
        importPrimitiveTypes();
	}
	
	private static void initModelTypeInstancesInfo() {
		
		modelTypeInstancesInfo=new WeakHashMap<>();
		modelTypeInstancesInfo.put(ModelInt.ONE, new ModelTypeInformation("1",true,false,1));
		modelTypeInstancesInfo.put(ModelInt.ZERO, new ModelTypeInformation("0",true,false,0));
		modelTypeInstancesInfo.put(ModelBool.TRUE, new ModelTypeInformation("true",true,false));
		modelTypeInstancesInfo.put(ModelBool.FALSE, new ModelTypeInformation("false",true,false));
		modelTypeInstancesInfo.put(ModelBool.ELSE, new ModelTypeInformation("else",true,false));
		
	}

	private static void endModelImport()
	{
		modelTypeInstancesInfo.clear();
		importing=false;
	}
	
	private static void importModelElements() throws ImportException
	{
		importClassNames();
		importAssociations();
		importSignals();
		importGeneralizations();
		importClassAttributes();
		importMemberFunctionsWithoutBodies();
		importMemberFunctionBodies();
		importClassStateMachinesAndNestedSignals();
	}
	public static Model importModel(String className,String path) throws ImportException
	{
		
        initModelImport(className, path);
       
		importModelElements();
		
		endModelImport();
		
   		return currentModel;
   	  
	}
	
	private static org.eclipse.uml2.uml.Package loadResource(URI uri)
	{
		Resource resource=resourceSet.getResource(uri,true);
		return (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),UMLPackage.Literals.PACKAGE);
	}
	private static void createProfile(String path) throws ImportException
	{
		//creating profile
		Profile profile = UMLFactory.eINSTANCE.createProfile();
		profile.setName("Custom Profile");
		profile.setURI(URI.createFileURI(path).appendSegment("Profile_"+currentModel.getName()).appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION).toString());

		//loading the UML metamodel
		Model umlMetamodel = (Model) loadResource(URI.createURI(UMLResource.UML_METAMODEL_URI));

		//loading UML Primitive Types Library and importing the primitve types from there
		org.eclipse.uml2.uml.Package umlLibrary= loadResource(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
		profile.createElementImport(umlLibrary.getOwnedType("Integer"));
		profile.createElementImport(umlLibrary.getOwnedType("Real"));
		profile.createElementImport(umlLibrary.getOwnedType("Boolean"));
		profile.createElementImport(umlLibrary.getOwnedType("String"));
		profile.createElementImport(umlLibrary.getOwnedType("UnlimitedNatural"));


		//creating the ExternalClass stereotype and an extension for it
		try
		{
			org.eclipse.uml2.uml.Class classifierMetaclass = (org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType("Classifier");

			profile.createMetaclassReference(classifierMetaclass);

			Stereotype externalStereotype=profile.createOwnedStereotype("ExternalClass", false);
			externalStereotype.createExtension(classifierMetaclass,false);
		}
		catch(Exception e)
		{
			
		}

		//defining and saving the profile
		profile.define();
		Resource resource = resourceSet.createResource(URI.createFileURI("").appendSegment("Profile_"+currentModel.getName()).appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION));
		resource.getContents().add(profile);
		try
		{
			resource.save(null);

		} 
		catch (IOException ioe)
		{
			throw new ImportException("I/O error occured during model import. Cannot save UML profile.");
		}
		
		
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
	
	private static boolean isSpecificClassOrEvent(Class<?> c)
	{
		if(!ElementTypeTeller.isClassifier(c))
		{
			return false;
		}
		Class<?> superClass=c.getSuperclass();
		
		if(superClass==null) return false;
		else if(superClass==ModelClass.class) return false;
		else if(superClass==ExternalClass.class) return false;
		else if(superClass==hu.elte.txtuml.api.Signal.class) return false;
		else return true;
		
	}
	private static void importGeneralizations() throws ImportException 
	{
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			else if(isSpecificClassOrEvent(c))	 
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
	private static void importClassName(Class<?> sourceClass) throws ImportException
	{
		org.eclipse.uml2.uml.Class importedClass =
				currentModel.createOwnedClass(sourceClass.getSimpleName(),Modifier.isAbstract(sourceClass.getModifiers()));
		setModifiers(importedClass,sourceClass);
		
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
	private static void importClassNames() throws ImportException 
	{
	
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			else if(ElementTypeTeller.isClass(c))
			{
				importClassName(c);
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
	        if(ElementTypeTeller.isAttribute(f)) 
	        {
	        	importAttribute(signal,f);
	        }
	    }
	}
	
    private static void importClassAttributes() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			else if(ElementTypeTeller.isClass(c)) 
			{
				org.eclipse.uml2.uml.Class currClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(c.getSimpleName());
	            
				for(Field f : c.getDeclaredFields()) 
				{		
	                if(ElementTypeTeller.isAttribute(f)) 
	                {
	                	importAttribute(currClass,f);
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
    	setModifiers(property,field);
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
    private static void importMemberFunctionBodies() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
			{
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			}
			if(ElementTypeTeller.isModelClass(c)) 
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
           
            Operation operation=importOperationWithoutBody(ownerClass,sourceClass,method);
            setModifiers(operation, method);
           
        }
    }
    
    private static void importClassMemberFunctionBodies( Class<?> sourceClass)
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(sourceClass.getSimpleName());
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{
   
           	importOperationBody(ElementFinder.findOperation(ownerClass, method.getName()),ownerClass,sourceClass,method);
        }
    }
    
  	private static void importClassStateMachinesAndNestedSignals() throws ImportException
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
				
				if(isContainsStateMachine(c))
				{
					importStateMachine(currClass,c);
				}
				
			}
		}
	}
    
  	private static void importNestedSignals(Class<?> sourceClass) throws ImportException
  	{
		for(Class<?> innerClass:sourceClass.getDeclaredClasses())
		{
			
			if(ElementTypeTeller.isEvent(innerClass) && currentModel.getOwnedType(innerClass.getSimpleName())==null)
			{
				//innerClass is an event and no signal in the model has the same name
				createSignalAndEvent(innerClass);		
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
    

    private static void setVisibilityBasedOnModifiersGivenByReflection(NamedElement element,int modifiers)
	{
		if(Modifier.isPrivate(modifiers))
		{
			element.setVisibility(VisibilityKind.PRIVATE_LITERAL);
		}
		else if(Modifier.isProtected(modifiers))
		{
			element.setVisibility(VisibilityKind.PROTECTED_LITERAL);
		}
		else if(Modifier.isPublic(modifiers))
		{
			element.setVisibility(VisibilityKind.PUBLIC_LITERAL);
		}
		else
		{
			if(element instanceof Property)
			{
				element.setVisibility(VisibilityKind.PRIVATE_LITERAL);
			}
			else if(element instanceof Operation || element instanceof org.eclipse.uml2.uml.Classifier)
			{
				element.setVisibility(VisibilityKind.PUBLIC_LITERAL);
			}
			else
			{
				element.setVisibility(VisibilityKind.PACKAGE_LITERAL);
			}
		}
	}
	private static void setElementModifiersBasedOnModifiersGivenByReflection(NamedElement element,int modifiers)
	{
		setVisibilityBasedOnModifiersGivenByReflection(element,modifiers);
		
		if(element instanceof Classifier)
		{
			boolean isAbstract = Modifier.isAbstract(modifiers);
			Classifier classifierElem=(Classifier) element;
			classifierElem.setIsAbstract(isAbstract);
		}
		
	}
	private static void setModifiers(NamedElement importedElement,Class<?> sourceClass)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceClass.getModifiers());	
	}
	private static void setModifiers(NamedElement importedElement, Method sourceMethod)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceMethod.getModifiers());	
	}
	private static void setModifiers(NamedElement importedElement,Field sourceField)
	{
		setElementModifiersBasedOnModifiersGivenByReflection(importedElement,sourceField.getModifiers());	
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

