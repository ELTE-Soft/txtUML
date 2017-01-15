package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.UMLFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.papyrus.DiagramManager;
import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;
import hu.elte.txtuml.utils.eclipse.ProjectUtils;

/**
 * Integration test for {@link PapyrusModelCreator}
 * 
 * <p>
 * <b>Attention:</b> This test should be run as a JUnit Plug-in test
 * </p>
 */
public class ClassDiagramNotationManagerImplTest {

	private static final String projectName = "TestProject";
	private static final String modelName = "test";

	private DiagramManager diagramManager;
	private ClassDiagramNotationManagerImpl instance;
	private Model model;
	private NotationModel notationModel;

	@Before
	public void setUp() {
		try {
			IProject project = ProjectUtils.createProject(projectName);
			URI umlFileURI = URI.createFileURI(projectName + "/" + modelName + ".uml");
			URI UmlFileResURI = CommonPlugin.resolve(umlFileURI);

			IFile UmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(UmlFileResURI.toFileString()));
			java.net.URI rawLoc = UmlFile.getRawLocationURI();
			String sourceUMLPath = rawLoc.toString();
			createUMLFile(rawLoc.getPath());
			ProjectUtils.openProject(project);
			PapyrusModelCreator papyrusModelCreator = new PapyrusModelCreator(projectName + "/" + modelName);
			papyrusModelCreator.setUpUML(sourceUMLPath);
			if (!papyrusModelCreator.diExists()) {
				model = papyrusModelCreator.getUmlModel();
				papyrusModelCreator.createPapyrusModel();
				ServicesRegistry registry = papyrusModelCreator.getServiceRegistry();
				TransactionalEditingDomain domain;

				domain = ServiceUtils.getInstance().getTransactionalEditingDomain(registry);

				diagramManager = new DiagramManager(registry);
				diagramManager.createDiagram(model, "testClassDiagram", new CreateClassDiagramCommand(), domain);

				Diagram diagram = diagramManager.getDiagrams().get(0);
				instance = new ClassDiagramNotationManagerImpl(diagram, domain);
				notationModel = (NotationModel) registry.getService(ModelSet.class).getModel(NotationModel.MODEL_ID);
			} else {
				throw new IllegalArgumentException("The .di File does not exist");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void createUMLFile(String sourceUMLPath) {
		Model model = UMLFactory.eINSTANCE.createModel();
		Class classA = model.createOwnedClass("testClassA", false);
		classA.createOwnedAttribute("attributeA", UMLFactory.eINSTANCE.createStringExpression().getType());
		Class classB = model.createOwnedClass("testClassB", false);
		classA.createAssociation(true, AggregationKind.NONE_LITERAL, "testEndA", 0, 0, classB, true,
				AggregationKind.NONE_LITERAL, "testEndB", 0, 0);
		Class classC = model.createOwnedClass("testClassC", false);
		classA.createGeneralization(classC);

		Signal signal = UMLFactory.eINSTANCE.createSignal();
		signal.setName("testSignal");
		signal.setPackage(model);

		model.setName("testModel");
		model.setURI(sourceUMLPath);
		ResourceSet resourceSet = new ResourceSetImpl();
		URI fileURI = URI.createFileURI(sourceUMLPath);
		Resource modelResource = resourceSet.createResource(fileURI);
		modelResource.getContents().add(model);
		try {
			model.eResource().save(null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testCreateClassForDiagram() {
		// given
		Class classA = (Class) model.getOwnedMember("testClassA");
		Property property = classA.getOwnedAttribute("attributeA",
				UMLFactory.eINSTANCE.createStringExpression().getType());
		int x = 10, y = 20, w = 120, h = 80;
		Rectangle boundsBefore = new Rectangle(x, y, w, h);

		// when
		instance.createClassForDiagram(classA, boundsBefore, null);

		// then
		List<Diagram> diagrams = notationModel.getResource().getContents().stream().filter(e -> e instanceof Diagram)
				.map(e -> ((Diagram) e)).collect(Collectors.toList());
		Diagram diagram = diagrams.get(0);

		Node node = (Node) diagram.getChildren().get(0);

		BasicCompartment comp = getPropertyCompartementOfNode(node);
		Node propertyNode = (Node) comp.getChildren().get(0);

		Bounds bounds = (Bounds) node.getLayoutConstraint();

		assertEquals(property, propertyNode.getElement());
		assertEquals(classA, node.getElement());
		assertEquals(x, bounds.getX());
		assertEquals(y, bounds.getY());
		assertEquals(w, bounds.getWidth());
		assertEquals(h, bounds.getHeight());
	}

	private static BasicCompartment getPropertyCompartementOfNode(Node node) {

		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		for (View child : children) {
			if (child.getType().equals(String.valueOf(ClassAttributeCompartmentEditPart.VISUAL_ID))) {
				return (BasicCompartment) child;
			}
		}
		return null;
	}

	@Test
	public void testCreateSignalForDiagram() {
		//TODO: Implement the feature
		/*
		// given
		Signal signal = (Signal) model.getOwnedMember("testSignal");
		int x = 10, y = 20, w = 120, h = 80;
		Rectangle boundsBefore = new Rectangle(x, y, w, h);

		// when
		instance.createSignalForDiagram(signal, boundsBefore, null);

		// then
		List<Diagram> diagrams = notationModel.getResource().getContents().stream().filter(e -> e instanceof Diagram)
				.map(e -> ((Diagram) e)).collect(Collectors.toList());
		Diagram diagram = diagrams.get(0);

		Node node = (Node) diagram.getChildren().get(0);
		Bounds bounds = (Bounds) node.getLayoutConstraint();

		assertEquals(signal, node.getElement());
		assertEquals(x, bounds.getX());
		assertEquals(y, bounds.getY());
		assertEquals(w, bounds.getWidth());
		assertEquals(h, bounds.getHeight());
		*/
	}

	@Test
	public void testCreateAssociationForNodes() {
		// given
		Class classA = (Class) model.getOwnedMember("testClassA");
		Association assoc = classA.getAssociations().get(0);
		Class classB = (Class) assoc.getEndTypes().stream().filter(t -> !t.equals(classA)).findFirst().get();
		int x1 = 10, y1 = 10, x2 = 10, y2 = 20, x3 = 20, y3 = 20, x4 = 30, y4 = 20;
		List<Point> route = Arrays.asList(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), new Point(x4, y4));
		String anchor1 = "(1.0, 0.5)";
		String anchor2 = "(0.5, 1.0)";
		// when
		instance.createClassForDiagram(classA, null, null);
		instance.createClassForDiagram(classB, null, null);
		instance.createAssociationForNodes(classA, classB, assoc, route, anchor1, anchor2, null);

		// then
		List<Diagram> diagrams = notationModel.getResource().getContents().stream().filter(e -> e instanceof Diagram)
				.map(e -> ((Diagram) e)).collect(Collectors.toList());
		Diagram diagram = diagrams.get(0);

		Edge edge = (Edge) diagram.getEdges().get(0);

		RelativeBendpoints bendpoints = (RelativeBendpoints) edge.getBendpoints();

		@SuppressWarnings("unchecked")
		List<RelativeBendpoint> edgeRoute = new ArrayList<RelativeBendpoint>(bendpoints.getPoints());

		IdentityAnchor sourceAnchor = (IdentityAnchor) edge.getSourceAnchor();
		IdentityAnchor targetAnchor = (IdentityAnchor) edge.getTargetAnchor();

		assertEquals(assoc, edge.getElement());

		assertEquals(x1 - x1, edgeRoute.get(0).getSourceX());
		assertEquals(y1 - y1, edgeRoute.get(0).getSourceY());
		assertEquals(x1 - x4, edgeRoute.get(0).getTargetX());
		assertEquals(y1 - y4, edgeRoute.get(0).getTargetY());

		assertEquals(x2 - x1, edgeRoute.get(1).getSourceX());
		assertEquals(y2 - y1, edgeRoute.get(1).getSourceY());
		assertEquals(x2 - x4, edgeRoute.get(1).getTargetX());
		assertEquals(y2 - y4, edgeRoute.get(1).getTargetY());

		assertEquals(x3 - x1, edgeRoute.get(2).getSourceX());
		assertEquals(y3 - y1, edgeRoute.get(2).getSourceY());
		assertEquals(x3 - x4, edgeRoute.get(2).getTargetX());
		assertEquals(y3 - y4, edgeRoute.get(2).getTargetY());

		assertEquals(x4 - x1, edgeRoute.get(3).getSourceX());
		assertEquals(y4 - y1, edgeRoute.get(3).getSourceY());
		assertEquals(x4 - x4, edgeRoute.get(3).getTargetX());
		assertEquals(y4 - y4, edgeRoute.get(3).getTargetY());

		assertEquals(anchor1, sourceAnchor.getId());
		assertEquals(anchor2, targetAnchor.getId());
		assertEquals(classA, (edge.getSource()).getElement());
		assertEquals(classB, (edge.getTarget()).getElement());
	}

	@Test
	public void testCreateGeneralizationForNodes() {
		// given
		Class classA = (Class) model.getOwnedMember("testClassA");
		Generalization gen = classA.getGeneralizations().get(0);
		Class classC = (Class) gen.getGeneral();
		int x1 = 10, y1 = 10, x2 = 10, y2 = 50;
		List<Point> route = Arrays.asList(new Point(x1, y1), new Point(x2, y2));
		String targetAnchorId = "(0.5, 0.0)";
		String sourceAnchorId = "(0.5, 1.0)";
		// when
		instance.createClassForDiagram(classA, null, null);
		instance.createClassForDiagram(classC, null, null);
		instance.createGeneralizationForNodes(gen, route, sourceAnchorId, targetAnchorId, null);

		// then
		List<Diagram> diagrams = notationModel.getResource().getContents().stream().filter(e -> e instanceof Diagram)
				.map(e -> ((Diagram) e)).collect(Collectors.toList());
		Diagram diagram = diagrams.get(0);

		Edge edge = (Edge) diagram.getEdges().get(0);

		RelativeBendpoints bendpoints = (RelativeBendpoints) edge.getBendpoints();

		@SuppressWarnings("unchecked")
		List<RelativeBendpoint> edgeRoute = new ArrayList<RelativeBendpoint>(bendpoints.getPoints());

		IdentityAnchor sourceAnchor = (IdentityAnchor) edge.getSourceAnchor();
		IdentityAnchor targetAnchor = (IdentityAnchor) edge.getTargetAnchor();

		assertEquals(gen, edge.getElement());

		assertEquals(x1 - x1, edgeRoute.get(0).getSourceX());
		assertEquals(y1 - y1, edgeRoute.get(0).getSourceY());
		assertEquals(x1 - x2, edgeRoute.get(0).getTargetX());
		assertEquals(y1 - y2, edgeRoute.get(0).getTargetY());

		assertEquals(x2 - x1, edgeRoute.get(1).getSourceX());
		assertEquals(y2 - y1, edgeRoute.get(1).getSourceY());
		assertEquals(x2 - x2, edgeRoute.get(1).getTargetX());
		assertEquals(y2 - y2, edgeRoute.get(1).getTargetY());

		// Somehow the source and target nodes are swapped in case of
		// generalization on the Papyrus diagram, so it is the expected result.
		assertEquals(targetAnchorId, sourceAnchor.getId());
		assertEquals(sourceAnchorId, targetAnchor.getId());
		assertEquals(classA, (edge.getSource()).getElement());
		assertEquals(classC, (edge.getTarget()).getElement());
	}

	@After
	public void tearDown() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {
				projects[i].delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}
