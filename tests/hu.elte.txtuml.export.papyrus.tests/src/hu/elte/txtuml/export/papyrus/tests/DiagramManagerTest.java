package hu.elte.txtuml.export.papyrus.tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationUtils;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.statemachine.CreateStateMachineDiagramCommand;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.papyrus.DiagramManager;
import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;
import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;

/**
 * Unit test for {@link DiagramManager}
 * 
 * <p>
 * <b>Attention:</b>
 * This test should be run as a JUnit Plug-in test
 * </p>
 */
public class DiagramManagerTest {

	public DiagramManager diagramManager;
	public IProject project;
	public UMLModelManager modelManager; 
	public IMultiDiagramEditor editor;
	
	@Before
	public void setUp() throws Exception {
		cleanWorkspace();
		String projectname = "testproject";
		String modelname = "TestModel";
		
		project = ProjectUtils.createProject(projectname);
		ProjectUtils.openProject(project);
		
		PapyrusModelCreator pmc = new PapyrusModelCreator(projectname+"/"+modelname);
		
		IProject sourceproject = ProjectUtils.createProject("sourceProject");
		String modelFilename = "dummy";
		
		String tmpfolder = sourceproject.getLocation().toString();
        URI uri = URI.createURI(tmpfolder).appendSegment(modelFilename).appendFileExtension(UMLResource.FILE_EXTENSION);
        URI urifile = URI.createFileURI(uri.toString());
        
        
		createUMLFile(urifile, modelname);
		pmc.setUpUML(urifile.toString());
		
		pmc.createPapyrusModel();
		
		IFile diFile = pmc.getDi(); 
		
		IEditorInput editorInput = new FileEditorInput(diFile);
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorPart ed =  IDE.openEditor(window.getActivePage(),
				editorInput, "org.eclipse.papyrus.infra.core.papyrusEditor", true);
	
		editor = (IMultiDiagramEditor) ed;
		diagramManager = new DiagramManager(editor);
		UmlModel umlModel = (UmlModel) editor.getServicesRegistry().getService(ModelSet.class)
											.getModel(UmlModel.MODEL_ID);
		modelManager = new UMLModelManager(umlModel);
	}
	
	private void cleanWorkspace() throws Exception {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject project : projects){
			ProjectUtils.deleteProject(project);
		}
	}
	
	private void createUMLFile(URI uri, String modelname){
		Model model = UMLFactory.eINSTANCE.createModel();
        
		model.setName(modelname);
		
		model.createNestedPackage("MyPackage");
		org.eclipse.uml2.uml.Class A = model.createOwnedClass("ClassA", false);

		StateMachine stateMachine = (StateMachine) A.createClassifierBehavior("SM",UMLPackage.Literals.STATE_MACHINE);
		stateMachine.createRegion("Region1");
		
    	ResourceSet resourceSet = new ResourceSetImpl();
  		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
  				UMLResource.FILE_EXTENSION,
  				UMLResource.Factory.INSTANCE
  			);
        
        Resource modelResource = resourceSet.createResource(uri);
        modelResource.getContents().add(model);
        try {
			modelResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		editor.doSave(new NullProgressMonitor());
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().closeEditor(editor, false);
	}

	@Test
	public void testCreateDiagrams() {
		List<Element> models = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
		diagramManager.createDiagrams(models, new CreateClassDiagramCommand());
		
		List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Package.class));
		diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		
		
		List<Element> statemachines = modelManager.getElementsOfTypes(Arrays.asList(StateMachine.class));
		diagramManager.createDiagrams(statemachines, new CreateStateMachineDiagramCommand());
		
		Resource notationResource = null;
		try {
			notationResource = NotationUtils.getNotationModel(this.editor.getServicesRegistry().getService(ModelSet.class)).getResource();
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
		@SuppressWarnings("unchecked")
		List<Diagram> list = (List<Diagram>)(List<?>) notationResource.getContents();
		
		assertEquals(3, list.size());
		
		Diagram modelDiag = list.get(0);
		Diagram packageDiag = list.get(1);
		Diagram statemachineDiag = list.get(2);
		
		assertEquals("TestModel", modelDiag.getName());
		assertEquals("MyPackage", packageDiag.getName());
		assertEquals("SM", statemachineDiag.getName());
		
		assertEquals(models.get(0), modelDiag.getElement());
		assertEquals(packages.get(0), packageDiag.getElement());
		assertEquals(statemachines.get(0), statemachineDiag.getElement());
	}
	

	@Test
	public void testGetDiagrams() {
		List<Element> models = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
		diagramManager.createDiagrams(models, new CreateClassDiagramCommand());
		
		List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Package.class));
		diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		
		
		List<Element> statemachines = modelManager.getElementsOfTypes(Arrays.asList(StateMachine.class));
		diagramManager.createDiagrams(statemachines, new CreateStateMachineDiagramCommand());
		
		
		List<Diagram> list = diagramManager.getDiagrams();
		
		assertEquals(3, list.size());
		
		Diagram modelDiag = list.get(0);
		Diagram packageDiag = list.get(1);
		Diagram statemachineDiag = list.get(2);
		
		assertEquals("TestModel", modelDiag.getName());
		assertEquals("MyPackage", packageDiag.getName());
		assertEquals("SM", statemachineDiag.getName());
		
		assertEquals(models.get(0), modelDiag.getElement());
		assertEquals(packages.get(0), packageDiag.getElement());
		assertEquals(statemachines.get(0), statemachineDiag.getElement());
	}

	@Test
	public void testGetDiagramContainer() {
		List<Element> models = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
		diagramManager.createDiagrams(models, new CreateClassDiagramCommand());
		
		List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Package.class));
		diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		
		
		List<Element> statemachines = modelManager.getElementsOfTypes(Arrays.asList(StateMachine.class));
		diagramManager.createDiagrams(statemachines, new CreateStateMachineDiagramCommand());
		
		Resource notationResource = null;
		try {
			notationResource = NotationUtils.getNotationModel(this.editor.getServicesRegistry().getService(ModelSet.class)).getResource();
		} catch (ServiceException e) {
			fail(e.getMessage());
		}
		@SuppressWarnings("unchecked")
		List<Diagram> list = (List<Diagram>)(List<?>) notationResource.getContents();
		
		assertEquals(3, list.size());
		
		Diagram modelDiag = list.get(0);
		Diagram packageDiag = list.get(1);
		Diagram statemachineDiag = list.get(2);
		
		assertEquals(models.get(0), diagramManager.getDiagramContainer(modelDiag));
		assertEquals(packages.get(0), diagramManager.getDiagramContainer(packageDiag));
		assertEquals(statemachines.get(0), diagramManager.getDiagramContainer(statemachineDiag));
	}

	@Test
	public void testOpenDiagram() {
		List<Element> models = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
		diagramManager.createDiagrams(models, new CreateClassDiagramCommand());
		
		List<Element> packages = modelManager.getElementsOfTypes(Arrays.asList(Package.class));
		diagramManager.createDiagrams(packages, new CreateClassDiagramCommand());
		
		List<Element> stateMachines = modelManager.getElementsOfTypes(Arrays.asList(StateMachine.class));
		diagramManager.createDiagrams(stateMachines, new CreateStateMachineDiagramCommand());
		
		List<Diagram> diags =  diagramManager.getDiagrams();

		Diagram diagram;
		Diagram active;
		
		diagram = diags.get(2);
		diagramManager.openDiagram(diagram);
		active = diagramManager.getActiveDiagramEditPart().getDiagramView();
		assertEquals(diagram, active);
		
		diagram = diags.get(0);
		diagramManager.openDiagram(diagram);
		active = diagramManager.getActiveDiagramEditPart().getDiagramView();
		assertEquals(diagram, active);
	
		diagram = diags.get(1);
		diagramManager.openDiagram(diagram);
		active = diagramManager.getActiveDiagramEditPart().getDiagramView();
		assertEquals(diagram, active);
	}

	@Test
	public void testGetActiveDiagramEditPart() {
		List<Element> models = modelManager.getElementsOfTypes(Arrays.asList(Model.class));
		diagramManager.createDiagrams(models, new CreateClassDiagramCommand());
		
		DiagramEditPart diagep = diagramManager.getActiveDiagramEditPart();
		
		assertEquals(models.get(0),((View) diagep.getModel()).getElement());
	}

}
