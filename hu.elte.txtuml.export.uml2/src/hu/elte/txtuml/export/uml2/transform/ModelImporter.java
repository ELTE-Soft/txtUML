package hu.elte.txtuml.export.uml2.transform;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.ModelString;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;
import hu.elte.txtuml.export.uml2.utils.ElementModifiersAssigner;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ResourceSetFactory;
import hu.elte.txtuml.export.uml2.transform.backend.ProfileCreator;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.ImporterConfiguration;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;
import hu.elte.txtuml.export.uml2.transform.backend.TimerCreator;
import hu.elte.txtuml.export.uml2.transform.backend.UMLPrimitiveTypesProvider;
import hu.elte.txtuml.stdlib.Timer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
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

/**
 * This class is responsible for generating Eclipse UML2 model from a txtUML model.
 *
 * @author Ádám Ancsin
 *
 */
public class ModelImporter extends AbstractImporter{
	
	/**
	 * Imports a txtUML model.
	 * @param modelClass The class of the txtUML model.
	 * @param path The output directory path. (needed for resource handling)
	 * @return The imported UML2 model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	public static Model importModel(Class<?> modelClass, String path) throws ImportException
	{
		ModelImporter.modelClass = modelClass;
		initModelImport(path);
		importModelElements();	
		endModelImport();
		
   		return currentModel; 
	}
	
	/**
	 * Imports a txtUML model.
	 * @param modelClass The name of the class of the txtUML model.
	 * @param path The output directory path. (needed for resource handling)
	 * @return The imported UML2 model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	public static Model importModel(String modelClassName, String path) throws ImportException
	{
		return importModel(ElementFinder.findModel(modelClassName),path);
	}
	
	/**
	 * Gets the resource set.
	 * @return The resource set.
	 *
	 * @author Ádám Ancsin
	 */
	public static ResourceSet getResourceSet()
	{
		return resourceSet;
	}
	
	/**
	 * Gets the resource containing the currently imported model.
	 * @return The resource containing the currently imported model.
	 *
	 * @author Ádám Ancsin
	 */
	public static Resource getModelResource() 
	{
		return modelResource;
	}

	/**
	 * Gets the class of the txtUML model being imported.
	 * @return The class of the txtUML model being imported.
	 *
	 * @author Ádám Ancsin
	 */
	public static Class<?> getModelClass() 
	{
		return modelClass;
	}

	/**
	 * Gets the UML profile.
	 * @return The UML profile.
	 *
	 * @author Ádám Ancsin
	 */
	public static Profile getProfile()
	{
		return currentProfile;
	}

	/**
	 * Decides if model import is in progress.
	 * @return The decision.
	 *
	 * @author Ádám Ancsin
	 */
	public static boolean isImporting()
	{
		return importing;
	}
	
	/**
	 * Imports the type with the specified class.
	 * @param sourceClass The specified class.
	 * @return The imported type.
	 *
	 * @author Ádám Ancsin
	 */
	static org.eclipse.uml2.uml.Type importType(Class<?> sourceClass) 
	{
		
		if(sourceClass == ModelInt.class) 
			return UMLPrimitiveTypesProvider.getInteger();
		else if(sourceClass == ModelBool.class) 
			return UMLPrimitiveTypesProvider.getBoolean();
		else if(sourceClass == ModelString.class) 
			return UMLPrimitiveTypesProvider.getString();
		else if(sourceClass == Timer.class)
			return currentModel.getOwnedType(ImporterConfiguration.timerName);
		else if(sourceClass.getDeclaringClass() ==  Timer.class)
			return currentModel.getOwnedType(ImporterConfiguration.timerName+"_"+sourceClass.getSimpleName());
		else if(ElementTypeTeller.isClass(sourceClass))
			return currentModel.getOwnedType(sourceClass.getSimpleName());
		else
			return null;
	}
	
	/**
	 * Initializes model import.
	 * @param path The path of the output directory. (needed for resource handling)
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void initModelImport(String path) throws ImportException
	{
		importing=true;
		
		String modelName=modelClass.getSimpleName();
		String className=modelClass.getCanonicalName();
		
		Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(modelName);
        currentModel=model;
        
        resourceSet = ResourceSetFactory.createAndInitResourceSet();
        createAndInitModelResource(className, path);
        
        ProfileCreator.createProfileForModel(className,path,resourceSet);
        loadAndApplyProfile();
        UMLPrimitiveTypesProvider.importFromProfile(currentProfile);
        
        TimerCreator.createTimer(currentModel, currentProfile);
        
        InstanceManager.initGlobalInstancesMap();
	}
	
	/**
	 * Creates and initializes modelResource so it contains the currently imported model.
	 * @param modelClassQualifiedName The qualified name of the class representing the currently imported model.
	 * @param outputPath The output path of the resource.
	 *
	 * @author Ádám Ancsin
	 */
	private static void createAndInitModelResource(String modelClassQualifiedName, String outputPath) 
	{
		URI uri = URI.createURI(outputPath).appendSegment(modelClassQualifiedName).appendFileExtension(UMLResource.FILE_EXTENSION);
    	modelResource = resourceSet.createResource(uri);
        modelResource.getContents().add(currentModel);
	}
	
	/**
	 * Imports the model elements of the txtUML model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void importModelElements() throws ImportException
	{
		importClassesAndSignals();
		importAssociations();
		importGeneralizations();
		importAttributesOfAllClassifiers();
		importMemberFunctionSkeletons();
		importClassMemberFunctionBodiesStateMachinesAndNestedSignals();
	}

	/**
	 * Ends the model import in progress. 
	 * 
	 * @author Ádám Ancsin
	 */
	private static void endModelImport()
	{
		InstanceManager.clearGlobalInstancesMap();
		importing=false;
	}
	
	/**
	 * Loads the UML profile and applies it to the model.
	 * 
	 * @author Ádám Ancsin
	 */
	private static void loadAndApplyProfile()
	{
		Resource resource = resourceSet.getResource(
				URI.createFileURI("").
				appendSegment(modelClass.getCanonicalName()).
				appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION),
				true
			);
		currentProfile=(Profile) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PROFILE);
		currentModel.applyProfile(currentProfile);
	}
	
	/**
	 * Imports all generalizations of the model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void importGeneralizations() throws ImportException 
	{
		for(Class<?> c : modelClass.getDeclaredClasses())
		{
			if(!ElementTypeTeller.isModelElement(c))
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			else if(ElementTypeTeller.isSpecificClassifier(c))	 
				createGeneralization(c,c.getSuperclass());
		}
	}
	
	/**
	 * Creates a generalization.
	 * @param specific The specific (super) classifier.
	 * @param general The general (sub) classifier.
	 *
	 * @author Ádám Ancsin
	 */
	private static void createGeneralization(Class<?> specific, Class<?> general)
	{
		Classifier uml2SpecClassifier = 
				(Classifier) currentModel.getOwnedMember(specific.getSimpleName());
		
		Classifier uml2GeneralClassifier = 
				(Classifier) currentModel.getOwnedMember(general.getSimpleName());
		
		uml2SpecClassifier.createGeneralization(uml2GeneralClassifier);
	}
	
	/**
	 * Imports the specified classifier.
	 * @param sourceClass The class representing the classifier.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
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
					importedClass.applyStereotype(
							currentProfile.getOwnedStereotype(
									ImporterConfiguration.externalClassStereotypeName
									)
								);
				}
				catch(Exception e)
				{
					throw new ImportException(
							"Cannot apply stereotype " +
							ImporterConfiguration.externalClassStereotypeName +
							" to class: " +
							sourceClass.getCanonicalName()
						);
				}
			}
		}
		else
			createSignalAndSignalEvent(sourceClass);
		
	}
	/**
	 * Imports the classes and signals of the model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void importClassesAndSignals() throws ImportException 
	{	
		for(Class<?> c : modelClass.getDeclaredClasses()) {
			
			if(!ElementTypeTeller.isModelElement(c))
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			else if(ElementTypeTeller.isClassifier(c))
				importClassifier(c);
		}
	}
	
	/**
	 * Imports the associations of the model.
	 * 
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void importAssociations() throws ImportException
	{
		for(Class<?> sourceClass : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(sourceClass))
				throw new ImportException(sourceClass.getName()+" is a non-txtUML class found in model.");
			else if(ElementTypeTeller.isAssociation(sourceClass)) 
				new AssociationImporter(sourceClass,currentModel).importAssociation();
		}
	}	
		
	/**
	 * Creates a signal and a signal event in the UML2 model based on the specified class
	 * representing a signal in the txtUML model.
	 * @param sourceClass The class representing the signal in the txtUML model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	private static void createSignalAndSignalEvent(Class<?> sourceClass) throws ImportException
	{
		Signal signal = (Signal)
				currentModel.createOwnedType(sourceClass.getSimpleName(),UMLPackage.Literals.SIGNAL);
		
		SignalEvent signalEvent = (SignalEvent)
				currentModel.createPackagedElement(signal.getName()+"_event",UMLPackage.Literals.SIGNAL_EVENT);
		
		signalEvent.setSignal(signal);
	}
		
	/**
	 * Imports the attributes of all classifiers in the model.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
    private static void importAttributesOfAllClassifiers() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			else if(ElementTypeTeller.isClassifier(c)) 
				importClassifierAttributes(c);
		}
    }
    
    /**
     * Imports the attributes of the specified txtUML classifier.
     * @param classifier The specified txtUML classifier.
     * @throws ImportException
     *
     * @author Ádám Ancsin
     */
    private static void importClassifierAttributes(Class<?> classifier) throws ImportException
    {
    	org.eclipse.uml2.uml.Classifier uml2Classifier=
				(org.eclipse.uml2.uml.Classifier) currentModel.getMember(classifier.getSimpleName());
        
		for(Field f : classifier.getDeclaredFields()) 
		{		
            if(ElementTypeTeller.isAttribute(f)) 
            	importAttribute(uml2Classifier,f);
        }
    }
    
    /**
     * Imports the given attribute of the specified classifier.
     * @param owner The owner classifier.
     * @param field The Java field representing the attribute in the txtUML model.
     * @throws ImportException
     *
     * @author Ádám Ancsin
     */
    private static void importAttribute(Classifier owner, Field field) throws ImportException
    {
    	String fieldName=field.getName();
    	org.eclipse.uml2.uml.Type fieldType=importType(field.getType());
    	
    	Property property=null;
    	
    	if(owner instanceof Signal)
    		property=((Signal) owner).createOwnedAttribute(fieldName,fieldType);
    	else if(owner instanceof org.eclipse.uml2.uml.Class)
    		property=((org.eclipse.uml2.uml.Class) owner).createOwnedAttribute(fieldName,fieldType);
    	else
    		throw new ImportException(owner.getName()+" is not a Class nor a Signal.");
    	
    	ElementModifiersAssigner.setModifiers(property,field);
    }
    
    /**
     * Import the skeletons (no body) of class member functions.
     * @throws ImportException
     *
     * @author Ádám Ancsin
     */
    private static void importMemberFunctionSkeletons() throws ImportException
    {
		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");
			if(ElementTypeTeller.isModelClass(c)) 
				createClassMemberFunctionSkeletons(c);
		}
    }

    /**
     * Creates member function skeletons for the specified class.
     * @param sourceClass The txtUML class.
     *
     * @author Ádám Ancsin
     */
    private static void createClassMemberFunctionSkeletons(Class<?> sourceClass)
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) 
    			currentModel.getMember(sourceClass.getSimpleName());
    	createClassMemberFunctionSkeletons(sourceClass,ownerClass);
    }
    
    /**
     * Creates member function skeletons for the specified class.
     * @param sourceClass The txtUML class.
     * @param ownerClass The imported class.
     *
     * @author Ádám Ancsin
     */
    private static void createClassMemberFunctionSkeletons(Class<?> sourceClass, org.eclipse.uml2.uml.Class ownerClass)
    {
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{         
            Operation operation=importOperationSkeleton(ownerClass,sourceClass,method);
            ElementModifiersAssigner.setModifiers(operation, method);        
        }
    }
    
    /**
     * Imports the bodies of the member functions of the specified class.
     * @param sourceClass The txtUML class.
     * @throws ImportException 
     * 
     * @author Ádám Ancsin
     */
    private static void importClassMemberFunctionBodies(Class<?> sourceClass) throws ImportException
    {
    	org.eclipse.uml2.uml.Class ownerClass=(org.eclipse.uml2.uml.Class) currentModel.getMember(sourceClass.getSimpleName());
    	for(Method method : sourceClass.getDeclaredMethods()) 
    	{
           	importOperationBody(ElementFinder.findOperation(method.getName(), ownerClass),ownerClass,method);
        }
    }
    
    /**
     * Imports the member function bodies, state machines and nested signals of all model classes in the model.
     * @throws ImportException
     *
     * @author Ádám Ancsin
     */
 	private static void importClassMemberFunctionBodiesStateMachinesAndNestedSignals() throws ImportException
 	{
 		for(Class<?> c : modelClass.getDeclaredClasses()) 
		{
			if(!ElementTypeTeller.isModelElement(c))
				throw new ImportException(c.getName()+" is a non-txtUML class found in model.");

			if(ElementTypeTeller.isModelClass(c)) 
			{
				org.eclipse.uml2.uml.Class currClass = (org.eclipse.uml2.uml.Class) currentModel.getOwnedMember(c.getSimpleName());
				
				importNestedSignals(c);
			
				InstanceManager.createClassAndFieldInstancesAndInitClassAndFieldInstancesMap(c);
				
				if(containsStateMachine(c))
					importStateMachine(currClass,c);
				
				importClassMemberFunctionBodies(c);
	
				InstanceManager.clearClassAndFieldInstancesMap();
			}
		}
 	}
    
 	/**
 	 * Imports the nested signals of the specified class.
 	 * @param sourceClass The txtUML class.
 	 * @throws ImportException
 	 *
 	 * @author Ádám Ancsin
 	 */
  	private static void importNestedSignals(Class<?> sourceClass) throws ImportException
  	{
		for(Class<?> innerClass:sourceClass.getDeclaredClasses())
		{
			//innerClass is an event and no signal in the model has the same name
			if(ElementTypeTeller.isEvent(innerClass) && currentModel.getOwnedType(innerClass.getSimpleName())==null)
			{
				createSignalAndSignalEvent(innerClass);
				importClassifierAttributes(innerClass);
			}
		}
  	}

  	/**
  	 * Imports the state machine of the specified class.
  	 * @param ownerClass The UML2 class.
  	 * @param sourceClass The txtUML class.
  	 * @return The imported UML2 state machine.
  	 * @throws ImportException
  	 *
  	 * @author Ádám Ancsin
  	 */
	private static StateMachine importStateMachine(org.eclipse.uml2.uml.Class ownerClass,Class<?> sourceClass) 
											throws ImportException
	{	
		
        StateMachine stateMachine = (StateMachine) 
        		ownerClass.createClassifierBehavior(ownerClass.getName(),UMLPackage.Literals.STATE_MACHINE);
        
        Region region = new RegionImporter
        		(sourceClass,InstanceManager.getSelfInstance(),currentModel,stateMachine.createRegion(stateMachine.getName()))
        		.importRegion();
        
        if(region.getSubvertices().size() != 0 && !containsInitial(region)) 
        {
        	importWarning(
        			sourceClass.getName() +
        			" has one or more vertices but no initial pseudostate (state machine will not be created)"
        		);
        	return null;
        }
        return stateMachine; 
    }
	
	/**
	 * Imports the body of a specified operation.
	 * @param operation The specified operation.
	 * @param ownerClass The owner UML2 class of the operation.
	 * @param sourceMethod The txtUML method.
	 * @return The imported UML2 activity of the operation.
	 *
	 * @throws ImportException 
	 *
	 * @author Ádám Ancsin
	 */
	private static Activity importOperationBody
		(Operation operation,org.eclipse.uml2.uml.Class ownerClass,Method sourceMethod) throws ImportException
	{
		Activity activity=(Activity) ownerClass.createOwnedBehavior(sourceMethod.getName(),UMLPackage.Literals.ACTIVITY);
		activity.setSpecification(operation);
		MethodImporter.importMethod(currentModel,activity,sourceMethod,InstanceManager.getSelfInstance());

		return activity;
	}
	
	/**
	 * Creates the return result of the specified operation.
	 * @param operation The specified operation.
	 * @param sourceMethod The txtUML method.
	 *
	 * @author Ádám Ancsin
	 */
	private static void createOperationReturnResult(Operation operation,Method sourceMethod)
	{
		Class<?> returnTypeClass = sourceMethod.getReturnType();
		if(returnTypeClass!=void.class)
		{
			Type returnType=ModelImporter.importType(returnTypeClass);
			operation.createReturnResult("return",returnType);
		}
	}
	
	/**
	 * Creates an owned operation of a specified UML2 class based on a txtUML method.
	 * @param ownerClass The owner UML2 class.
	 * @param sourceMethod The txtUML method.
	 * @return The created UML2 operation.
	 *
	 * @author Ádám Ancsin
	 */
	private static Operation createOwnedOperation(org.eclipse.uml2.uml.Class ownerClass,Method sourceMethod)
	{
		BasicEList<String> paramNames=new BasicEList<>();
		BasicEList<Type> paramTypes=new BasicEList<>();
		
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
	
	/**
	 * Imports the skeleton of an operation.
	 * @param ownerClass The owner UML2 class of the operation.
	 * @param sourceClass The owner txtUML class.
	 * @param sourceMethod The txtUML method.
	 * @return The imported operation skeleton.
	 *
	 * @author Ádám Ancsin
	 */
	private static Operation importOperationSkeleton
		(org.eclipse.uml2.uml.Class ownerClass,Class<?> sourceClass,Method sourceMethod)
	{
		Operation operation = createOwnedOperation(ownerClass, sourceMethod);
		createOperationReturnResult(operation, sourceMethod);
		return operation;
	}
	
   
    private static Profile currentProfile = null;
    private static ResourceSet resourceSet = null;
    private static Resource modelResource=null;
	private static Model currentModel = null;
	private static boolean importing = false;
}