package hu.elte.txtuml.export.uml2.transform.importers;
import hu.elte.txtuml.export.uml2.mapping.ModelMapCollector;
import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.ProfileCreator;
import hu.elte.txtuml.export.uml2.transform.backend.UMLPrimitiveTypesHolder;
import hu.elte.txtuml.export.uml2.transform.visitors.AssociationVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.AttributeVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.ClassifierVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.MethodSkeletonVisitor;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ResourceSetFactory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * This class is responsible for generating Eclipse UML2 model from a txtUML
 * model.
 *
 * @author Adam Ancsin
 *
 */
public class ModelImporter {
	
	private Model importedModel;
	private TypeDeclaration sourceModel;
	private Profile profile;
	private ResourceSet resourceSet;
	private Resource modelResource;
	private Map<TypeDeclaration, Classifier> classifiers;
	private Map<TypeDeclaration, Map<MethodDeclaration, Operation>> methods;
	private UMLPrimitiveTypesHolder primitiveTypesHolder;
	private TypeImporter typeImporter;
	private ClassifierImporter classifierImporter;
	private RegionImporter regionImporter;
	
	static ModelMapCollector mapping = null;
	
	private static final String stdLibURI = "pathmap://TXTUML_STDLIB/stdlib.uml";

	public ModelImporter(TypeDeclaration sourceModel) {
		this.sourceModel = sourceModel;
	}

	/**
	 * Imports a txtUML model.
	 * 
	 * @param path
	 *            The output directory path. (needed for resource handling)
	 * @return The imported UML2 model.
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	public Model importModel(String txtUMLModelName, String path)
			throws ImportException {
		initModelImport(txtUMLModelName, path);
		importModelElements();
		ModelImporter.mapping.put(txtUMLModelName, getImportedModel());
		endModelImport(txtUMLModelName, path);

		return this.importedModel;
	}

	/**
	 * Initializes model import.
	 * 
	 * @param path
	 *            The path of the output directory. (needed for resource
	 *            handling)
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	private void initModelImport(String modelName, String path)
			throws ImportException {
		this.importedModel = UMLFactory.eINSTANCE.createModel();
		this.importedModel.setName(modelName);

		this.resourceSet = new ResourceSetFactory().createAndInitResourceSet();
		createAndInitModelResource(modelName, path);
		mapping = new ModelMapCollector(modelResource.getURI());

		ProfileCreator.createProfileForModel(modelName, path, this.resourceSet);
		loadAndApplyProfile();
		importStandardLibrary();
		this.primitiveTypesHolder = UMLPrimitiveTypesHolder.createFromProfile(this.profile);
		this.typeImporter = new TypeImporter(this);
		this.classifierImporter = new ClassifierImporter(this);
		this.regionImporter = new RegionImporter(this);
		this.methods = new HashMap<>();
	}

	/**
	 * Loads the UML profile and applies it to the imported model.
	 * 
	 * @author Adam Ancsin
	 */
	private void loadAndApplyProfile() {
		Resource resource = this.resourceSet.getResource(URI.createFileURI("")
				.appendSegment(this.importedModel.getQualifiedName())
				.appendFileExtension(UMLResource.PROFILE_FILE_EXTENSION), true);
		this.profile = (Profile) EcoreUtil.getObjectByType(
				resource.getContents(), UMLPackage.Literals.PROFILE);
		this.importedModel.applyProfile(profile);
	}

	/**
	 * Imports the txtUML Standard Library Model into the generated model.
	 * 
	 * @see	hu.elte.txtuml.stdlib
	 */
	private void importStandardLibrary() {
		// Load standard library
		Resource resource = this.resourceSet.getResource(URI.createURI(stdLibURI), true);
		if(resource == null) {
			return;
		}
		Package stdLib = (Package)EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		if(stdLib == null) {
			return;
		}

		// Import standard library into the generated model
		PackageImport packageImport = UMLFactory.eINSTANCE.createPackageImport();
		packageImport.setImportedPackage(stdLib);
		this.importedModel.getPackageImports().add(packageImport);
	}
	
	/**
	 * Creates and initializes modelResource so it contains the imported model.
	 * 
	 * @param txtUMLModelName
	 *            The qualified name of the source model.
	 * @param outputPath
	 *            The output path of the resource.
	 *
	 * @author Adam Ancsin
	 */
	private void createAndInitModelResource(String txtUMLModelName,
			String outputPath) {
		URI uri = URI.createURI(outputPath)
				.appendSegment(txtUMLModelName)
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		this.modelResource = this.resourceSet.createResource(uri);
		this.modelResource.getContents().add(this.importedModel);
	}

	/**
	 * Imports the model elements of the txtUML model.
	 * 
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	private void importModelElements() throws ImportException {
		importClassifiers();
		importAssociations();
		importGeneralizations();
		importAttributesOfEveryClassifier();
		importMethodSkeletonsOfEveryClassifier();
		importStateMachinesOfEveryClass();
		importMethodBodiesOfEveryClassifier();
	}

	private void importMethodBodiesOfEveryClassifier() {
		this.classifiers.forEach((declaration, classifier) -> {
			if (classifier instanceof org.eclipse.uml2.uml.Class) {
				importMethodBodiesOfSpecifiedClass(declaration, (org.eclipse.uml2.uml.Class) classifier);
			}
		});
	}

	private void importMethodBodiesOfSpecifiedClass(TypeDeclaration classDeclaration,
			Class specifiedClass) {
		Map<MethodDeclaration, Operation> memberFunctions = this.methods.get(classDeclaration);
		memberFunctions.forEach( (methodDeclaration, operation) -> {
			String methodName = operation.getName();
			Activity activity= (Activity) specifiedClass.createOwnedBehavior(methodName, UMLPackage.Literals.ACTIVITY);
			activity.setSpecification(operation);
			new MethodBodyImporter(activity, this.typeImporter, this.importedModel).
				importOperationBody(operation, methodDeclaration);
		});
	}

	private void importStateMachinesOfEveryClass() {
		this.classifiers.forEach((declaration, classifier) -> {
			if (classifier instanceof org.eclipse.uml2.uml.Class) {
				importStateMachine(declaration, (org.eclipse.uml2.uml.Class) classifier);
			}
		});

	}

	private void importStateMachine(TypeDeclaration classifierDeclaration,
			Class ownerClass) {	
		StateMachine stateMachine = (StateMachine) ownerClass.createClassifierBehavior(
				ownerClass.getName(),
				UMLPackage.Literals.STATE_MACHINE);
		Region region = stateMachine.createRegion(ownerClass.getName());
		this.regionImporter.importRegion(classifierDeclaration, region);
	}

	

	/**
	 * Imports the member function skeletons of every classifier in the model.
	 * 
	 * @author Adam Ancsin
	 */
	private void importMethodSkeletonsOfEveryClassifier() {
		this.classifiers.forEach( (classifierDeclaration, classifier) -> {
			if(classifier instanceof Class) {
				Class specifiedClass = (Class) classifier;
				importMethodSkeletonsOfSpecifiedClass(classifierDeclaration, specifiedClass);
			}
		});
	}

	private void importMethodSkeletonsOfSpecifiedClass(TypeDeclaration classDeclaration, Class specifiedClass) {
		MethodSkeletonVisitor visitor = new MethodSkeletonVisitor(
				new MethodSkeletonImporter(this.typeImporter, specifiedClass),
				classDeclaration);
		classDeclaration.accept(visitor);
		this.methods.put(classDeclaration, visitor.getVisitedMethods());
	}
	
	/**
	 * Imports the attributes of every classifier in the model.
	 * 
	 * @author Adam Ancsin
	 */
	private void importAttributesOfEveryClassifier() {
		this.classifiers.entrySet().forEach(entry -> {
			Classifier classifier = entry.getValue();
			TypeDeclaration classifierDeclaration = entry.getKey();
			importAttributesOfSpecifiedClassifier(classifierDeclaration, classifier);
		});
	}
	
	private void importAttributesOfSpecifiedClassifier(TypeDeclaration classifierDeclaration,  Classifier classifier) {
		AttributeVisitor visitor = new AttributeVisitor(
				new AttributeImporter(this.typeImporter, classifier),
				classifierDeclaration);
		classifierDeclaration.accept(visitor);
	}
	
	
	/**
	 * Imports all generalizations from the source txtUML model.
	 * 
	 * @author Adam Ancsin
	 */
	private void importGeneralizations() {
		for (TypeDeclaration classifierDeclaration : this.classifiers.keySet()) {
			if (ElementTypeTeller.isSpecificClassifier(classifierDeclaration)) {
				importGeneralization(classifierDeclaration);
			}
		}
	}

	/**
	 * Imports a generalization for the specified subtype with the given
	 * classifier declaration.
	 * 
	 * @param classifierDeclaration
	 *            The declaration of the specified subtype classifier.
	 *
	 * @author Adam Ancsin
	 */
	private void importGeneralization(TypeDeclaration classifierDeclaration) {
		ITypeBinding superclassBinding = classifierDeclaration.resolveBinding().getSuperclass();
		final String generalName = superclassBinding.getName();
		final String specificName = classifierDeclaration.getName().getFullyQualifiedName();
		
		Classifier specific = (Classifier) this.importedModel.getOwnedType(specificName);
		Classifier general = (Classifier) this.importedModel.getOwnedType(generalName);
		
		specific.createGeneralization(general);
	}

	/**
	 * Imports all associations from the source txtUML model.
	 *
	 * @author Adam Ancsin
	 */
	private void importAssociations() {
		this.sourceModel.accept(new AssociationVisitor(this));
	}

	/**
	 *  Imports all classifiers (classes and signals) from the source txtUML model.
	 *
	 * @author Adam Ancsin
	 */
	private void importClassifiers() {
		ClassifierVisitor visitor = new ClassifierVisitor(this.classifierImporter, true);
		this.sourceModel.accept(visitor);
		this.classifiers = visitor.getVisitedClassifiers();
	}

	/**
	 * Ends the model import in progress.
	 * 
	 * @author Adam Ancsin
	 */
	private static void endModelImport(String txtUMLModelName, String path) {
		try {
			mapping.save(URI.createURI(path), txtUMLModelName);
		} catch (ModelMapException e) {
			System.out.println("Faild to save model mapping.");
		}	
	}

	
	/**
	 * Gets the resource set.
	 * 
	 * @return The resource set.
	 *
	 * @author Adam Ancsin
	 */
	public ResourceSet getResourceSet() {
		return this.resourceSet;
	}

	/**
	 * Gets the resource containing the currently imported model.
	 * 
	 * @return The resource containing the currently imported model.
	 *
	 * @author Adam Ancsin
	 */
	public Resource getModelResource() {
		return this.modelResource;
	}

	/**
	 * Gets the UML profile.
	 * 
	 * @return The UML profile.
	 *
	 * @author Adam Ancsin
	 */
	public Profile getProfile() {
		return this.profile;
	}
	
	/**
	 * Gets the imported UML2 model.
	 * 
	 * @return The imported UML2 model.
	 *
	 * @author Adam Ancsin
	 */
	public Model getImportedModel() {
		return this.importedModel;
	}
	
	
	/**
	 * Gets the UML2 primitive type holder.
	 * 
	 * @return The UML2 primitive type holder.
	 *
	 * @author Adam Ancsin
	 */
	public UMLPrimitiveTypesHolder getPrimitiveTypesHolder() {
		return primitiveTypesHolder;
	}

	/** Gets the type importer.
	 * @return The type importer.
	 */
	public TypeImporter getTypeImporter() {
		return typeImporter;
	}

	public RegionImporter getRegionImporter() {
		return this.regionImporter;
	}
}