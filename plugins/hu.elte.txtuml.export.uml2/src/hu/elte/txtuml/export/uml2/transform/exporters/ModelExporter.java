package hu.elte.txtuml.export.uml2.transform.exporters;

import hu.elte.txtuml.export.uml2.mapping.ModelMapCollector;
import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.transform.backend.RuntimeExportException;
import hu.elte.txtuml.export.uml2.transform.visitors.AssociationVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.AttributeVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.ClassifierVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.MethodSkeletonVisitor;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.ResourceSetFactory;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
public class ModelExporter {

	private static final String STDLIB_URI = "pathmap://TXTUML_STDLIB/stdlib.uml";

	private final String sourcePackageName;
	private final String outputDirectory;
	private final Model exportedModel;
	private final CompilationUnit[] compilationUnits;
	private final ResourceSet resourceSet;
	private final Resource modelResource;
	private final TypeExporter typeExporter;
	private final RegionExporter regionExporter;
	private final ModelMapCollector mapping;

	private Map<TypeDeclaration, Classifier> classifiers;
	private Map<TypeDeclaration, Map<MethodDeclaration, Operation>> methods;

	public ModelExporter(CompilationUnit[] compilationUnits,
			String JtxtUMLModelName, String sourcePackageName,
			String outputDirectory) throws ExportException {
		this.compilationUnits = compilationUnits;
		this.sourcePackageName = sourcePackageName;
		this.outputDirectory = outputDirectory;

		this.exportedModel = UMLFactory.eINSTANCE.createModel();
		this.exportedModel.setName(JtxtUMLModelName);

		this.resourceSet = new ResourceSetFactory().createAndInitResourceSet();
		this.modelResource = createAndInitModelResource(sourcePackageName,
				outputDirectory, resourceSet, exportedModel);
		this.mapping = new ModelMapCollector(modelResource.getURI());

		importStandardLibrary(resourceSet, exportedModel);

		this.typeExporter = new TypeExporter(this);
		this.regionExporter = new RegionExporter(this);
		this.methods = new HashMap<>();
	}

	/**
	 * Exports the txtUML Standard Library Model into the generated model.
	 *
	 * @param resourceSet
	 *            The used resource set.
	 * @param exportedModel
	 *            The EMF-UML2 model.
	 * 
	 * @see hu.elte.txtuml.stdlib
	 */
	private static void importStandardLibrary(ResourceSet resourceSet,
			Model exportedModel) {
		// Load standard library
		Resource resource = resourceSet.getResource(URI.createURI(STDLIB_URI),
				true);
		if (resource == null) {
			return;
		}
		Package stdLib = (Package) EcoreUtil.getObjectByType(
				resource.getContents(), UMLPackage.Literals.PACKAGE);
		if (stdLib == null) {
			return;
		}

		// Import standard library into the generated model
		PackageImport packageImport = UMLFactory.eINSTANCE
				.createPackageImport();
		packageImport.setImportedPackage(stdLib);
		exportedModel.getPackageImports().add(packageImport);
	}

	/**
	 * Creates and initializes modelResource so it contains the exported model.
	 * 
	 * @param txtUMLModelName
	 *            The qualified name of the source model.
	 * @param outputPath
	 *            The output path of the resource.
	 * @param resourceSet
	 *            The resource set to add the new resource to.
	 * @param exportedModel
	 *            The EMF-UML2 model.
	 *
	 * @author Adam Ancsin
	 */
	private static Resource createAndInitModelResource(String txtUMLModelName,
			String outputPath, ResourceSet resourceSet, Model exportedModel) {
		URI uri = URI.createURI(outputPath).appendSegment(txtUMLModelName)
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		Resource modelResource = resourceSet.createResource(uri);
		modelResource.getContents().add(exportedModel);
		return modelResource;
	}

	/**
	 * Exports a txtUML model.
	 * 
	 * @return The exported UML2 model.
	 * @throws ExportException
	 * @throws RuntimeExportException
	 *
	 * @author Adam Ancsin
	 */
	public Model exportModel() throws ExportException, RuntimeExportException {

		// export model elements
		exportClassifiers();
		exportAssociations();
		exportGeneralizations();
		exportAttributesOfEveryClassifier();
		exportMethodSkeletonsOfEveryClassifier();
		exportStateMachinesOfEveryClass();
// TODO: Uncomment this	when activity export gets fixed.	
//		exportMethodBodiesOfEveryClassifier();

		this.mapping.put(sourcePackageName, exportedModel);
		finishModelExport();

		return this.exportedModel;
	}

	/**
	 * Exports all classifiers (classes and signals) from the source txtUML
	 * model.
	 *
	 * @author Adam Ancsin
	 */
	private void exportClassifiers() {
		ClassifierVisitor visitor = new ClassifierVisitor(
				new ClassifierExporter(mapping, exportedModel), true);
		Stream.of(compilationUnits).forEach(cu -> cu.accept(visitor));
		classifiers = visitor.getVisitedClassifiers();
	}

	/**
	 * Exports all associations from the source txtUML model.
	 *
	 * @author Adam Ancsin
	 * @throws ExportException
	 * 
	 */
	private void exportAssociations() throws ExportException {
		try {
			Stream.of(compilationUnits).forEach(
					cu -> cu.accept(new AssociationVisitor(mapping,
							exportedModel)));
		} catch (RuntimeExportException e) {
			throw e.getCause();
		}
	}

	/**
	 * Exports all generalizations from the source txtUML model.
	 * 
	 * @author Adam Ancsin
	 */
	private void exportGeneralizations() {
		for (TypeDeclaration classifierDeclaration : this.classifiers.keySet()) {
			if (ElementTypeTeller.isSpecificClassifier(classifierDeclaration)) {
				exportGeneralization(classifierDeclaration);
			}
		}
	}

	/**
	 * Exports a generalization for the specified subtype with the given
	 * classifier declaration.
	 * 
	 * @param classifierDeclaration
	 *            The declaration of the specified subtype classifier.
	 *
	 * @author Adam Ancsin
	 */
	private void exportGeneralization(TypeDeclaration classifierDeclaration) {
		ITypeBinding superclassBinding = classifierDeclaration.resolveBinding()
				.getSuperclass();
		final String generalName = superclassBinding.getName();
		final String specificName = classifierDeclaration.getName()
				.getFullyQualifiedName();

		Classifier specific = (Classifier) exportedModel
				.getOwnedType(specificName);
		Classifier general = (Classifier) exportedModel
				.getOwnedType(generalName);

		specific.createGeneralization(general);
	}

	/**
	 * Exports the attributes of every classifier in the model.
	 * 
	 * @author Adam Ancsin
	 */
	private void exportAttributesOfEveryClassifier() {
		classifiers.entrySet().forEach(
				entry -> {
					Classifier classifier = entry.getValue();
					TypeDeclaration classifierDeclaration = entry.getKey();
					exportAttributesOfSpecificClassifier(classifier,
							classifierDeclaration);
				});
	}

	private void exportAttributesOfSpecificClassifier(Classifier classifier,
			TypeDeclaration classifierDeclaration) {
		AttributeVisitor visitor = new AttributeVisitor(new AttributeExporter(
				typeExporter, mapping, classifier), classifierDeclaration);
		classifierDeclaration.accept(visitor);
	}

	/**
	 * Exports the member function skeletons of every classifier in the model.
	 * 
	 * @author Adam Ancsin
	 */
	private void exportMethodSkeletonsOfEveryClassifier() {
		classifiers.forEach((classifierDeclaration, classifier) -> {
			if (classifier instanceof Class) {
				Class specifiedClass = (Class) classifier;
				exportMethodSkeletonsOfSpecificClass(classifierDeclaration,
						specifiedClass);
			}
		});
	}

	private void exportMethodSkeletonsOfSpecificClass(
			TypeDeclaration classDeclaration, Class specificClass) {
		MethodSkeletonVisitor visitor = new MethodSkeletonVisitor(
				new MethodSkeletonExporter(typeExporter, specificClass),
				classDeclaration);
		classDeclaration.accept(visitor);
		methods.put(classDeclaration, visitor.getVisitedMethods());
	}

	private void exportStateMachinesOfEveryClass() {
		classifiers.forEach((declaration, classifier) -> {
			if (classifier instanceof Class) {
				exportStateMachine(declaration, (Class) classifier);
			}
		});
	}

	private void exportStateMachine(TypeDeclaration classifierDeclaration,
			Class ownerClass) {
		StateMachine stateMachine = (StateMachine) ownerClass
				.createClassifierBehavior(ownerClass.getName(),
						UMLPackage.Literals.STATE_MACHINE);
		Region region = stateMachine.createRegion(ownerClass.getName());
		regionExporter
				.exportRegion(classifierDeclaration, stateMachine, region);
		
		mapping.put(ownerClass.getName(), stateMachine);
		mapping.put(ownerClass.getName(), region);
	}

	@SuppressWarnings("unused")
	private void exportMethodBodiesOfEveryClassifier() {
		classifiers.forEach((declaration, classifier) -> {
			if (classifier instanceof Class) {
				exportMethodBodiesOfSpecificClass(declaration,
						(Class) classifier);
			}
		});
	}

	private void exportMethodBodiesOfSpecificClass(
			TypeDeclaration classDeclaration, Class specificClass) {
		Map<MethodDeclaration, Operation> memberFunctions = methods
				.get(classDeclaration);
		memberFunctions.forEach((methodDeclaration, operation) -> {
			String methodName = operation.getName();
			Activity activity = (Activity) specificClass.createOwnedBehavior(
					methodName, UMLPackage.Literals.ACTIVITY);
			activity.setSpecification(operation);
			MethodBodyExporter.export(activity, this, methodDeclaration,
					operation.getOwnedParameters());
		});
	}

	/**
	 * Finishes the model export in progress.
	 * 
	 * @author Adam Ancsin
	 */
	private void finishModelExport() {
		try {
			mapping.save(URI.createURI(outputDirectory), sourcePackageName);
		} catch (ModelMapException e) {
			System.out.println("Faild to save model mapping.");
		}
	}

	public ResourceSet getResourceSet() {
		return this.resourceSet;
	}

	/**
	 * @return The resource containing the currently exported model.
	 *
	 * @author Adam Ancsin
	 */
	public Resource getModelResource() {
		return this.modelResource;
	}

	/**
	 * @return The exported UML2 model.
	 *
	 * @author Adam Ancsin
	 */
	public Model getExportedModel() {
		return this.exportedModel;
	}

	public TypeExporter getTypeExporter() {
		return typeExporter;
	}

	public RegionExporter getRegionExporter() {
		return regionExporter;
	}

	public ModelMapCollector getMapping() {
		return mapping;
	}
}