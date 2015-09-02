package hu.elte.txtuml.export.papyrus.api.tests;

import static org.junit.Assert.fail;
import hu.elte.txtuml.eclipseutils.ProjectUtils;
import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;
import hu.elte.txtuml.export.papyrus.api.DiagramElementsModifier;
import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.DefaultPapyrusModelManager;
import hu.elte.txtuml.utils.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.gef.ui.figures.SlidableAnchor;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.BoundsImpl;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.extension.commands.IModelCreationCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.gmfdiag.common.editpart.ConnectionEditPart;
import org.eclipse.papyrus.infra.gmfdiag.css.CSSShapeImpl;
import org.eclipse.papyrus.uml.diagram.clazz.custom.edit.part.AssociationEndSourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.custom.edit.part.AssociationEndTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AppliedStereotypeAssociationEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicitySourceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationMultiplicityTargetEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.AssociationNameEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.wizards.category.DiagramCategoryDescriptor;
import org.eclipse.papyrus.uml.diagram.wizards.category.DiagramCategoryRegistry;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.uml2.uml.AggregationKind;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit Tests for {@link AbstractDiagramElementsArranger}
 * @author András Dobreff
 * 
 * <p>
 * <b>Attention:</b>
 * This test should be run as a JUnit Plug-in test
 * </p>
 */
public class DiagramElementsModifierTest {
	/**
	 * The Editor for the Test Models
	 */
	IMultiDiagramEditor editor;
	
	/**
	 * The Test Project
	 */
	IProject project;
	
	/** 
	 * Test initializing function. It builds up the Papyrus model for the Test
	 * @param objects
	 * @param links
	 */
	public void init(List<Pair<String, java.lang.Class<?>>> objects,
			List<Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>> links ){	
		
		String projectname = "testproject";
		project = ProjectUtils.createProject(projectname);
		ProjectUtils.openProject(project);
		
		PapyrusModelCreator pmc = new PapyrusModelCreator(projectname+"/model");
		try {
			pmc.createPapyrusModel();
			//System.out.println();
			HashMap<String, org.eclipse.uml2.uml.Class> nodes = new  HashMap<String, org.eclipse.uml2.uml.Class>();
			
			IFile diFile = pmc.getDi(); 
			
			IEditorInput editorInput = new FileEditorInput(diFile);
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IEditorPart ed = null;
			ed = IDE.openEditor(window.getActivePage(),
					editorInput, "org.eclipse.papyrus.infra.core.papyrusEditor", true);
		
			editor = (IMultiDiagramEditor) ed;
			
			ModelSet modelSet = editor.getServicesRegistry().getService(ModelSet.class);
			Map<String, DiagramCategoryDescriptor> categories = DiagramCategoryRegistry.getInstance().getDiagramCategoryMap();
			IModelCreationCommand creationCommand = categories.get("uml").getCommand();
			creationCommand.createModel(modelSet);
			UmlModel umlModel = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
			AbstractPapyrusModelManager manager = new DefaultPapyrusModelManager(editor, umlModel);
			
			EObject model = UmlUtils.getUmlModel().lookupRoot();
			org.eclipse.uml2.uml.Package modelpackage = (org.eclipse.uml2.uml.Package) model;
			
			TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(model);
			ted.getCommandStack().execute(new RecordingCommand(ted) {
				@Override
				protected void doExecute() {
					for(Pair<String, java.lang.Class<?>> pair: objects){
						java.lang.Class<?> type = pair.getValue();
						if(type.isAssignableFrom(org.eclipse.uml2.uml.Class.class)){
							org.eclipse.uml2.uml.Class created = modelpackage.createOwnedClass(pair.getKey(), false);
							nodes.put(pair.getKey(), created);
						}else if(type.isAssignableFrom(org.eclipse.uml2.uml.Package.class)){
							modelpackage.createNestedPackage(pair.getKey());
						}else if(type.isAssignableFrom(org.eclipse.uml2.uml.Interface.class)){
							modelpackage.createOwnedInterface(pair.getKey());
						}
						
					}
					
					for(Pair<Pair<String, Class<?>>, Pair<String, Class<?>>> link : links){
						Pair<String, Class<?>> pairA = link.getKey();
						Pair<String, Class<?>> pairB = link.getValue();
						
						String nameA = pairA.getKey();
						String nameB = pairB.getKey();
						
						org.eclipse.uml2.uml.Class classA = nodes.get(nameA);
						org.eclipse.uml2.uml.Class classB = nodes.get(nameB);
						
						classA.createAssociation(false, AggregationKind.NONE_LITERAL, "", 1, 1, classB, true, AggregationKind.NONE_LITERAL, "", 1, 1);
					}
				}
			});
			
			manager.createAndFillDiagrams(new NullProgressMonitor());
			editor.doSave(new NullProgressMonitor());
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Initialization Error");
		}
	}

	
	/**
	 * @return The Active {@link DiagramEditPart}
	 */
	protected DiagramEditPart getDiagramEditPart()
	{
		IEditorPart ied = editor.getActiveEditor();
		DiagramEditPart diagEp = ((IDiagramWorkbenchPart) ied).getDiagramEditPart();
		return diagEp;

	}
	
	/**
	 * TearDown method
	 */
	@After
	public void tearDown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
		ProjectUtils.deleteProject(project);
	}

	/**
	 *  Test for moveGraphicalEditPart
	 */
	@Test
	public void moveGraphicalEditPartTest(){
		
		List<Pair<String, Class<?>>> objects = Arrays.asList(new Pair<String, Class<?>>("ClassA", org.eclipse.uml2.uml.Class.class));
		init(objects, Arrays.asList());
		@SuppressWarnings("unchecked")
		List<EditPart> eps = getDiagramEditPart().getChildren();
		ClassEditPart classEp = (ClassEditPart) eps.get(0);
		
		int new_x = 250;
		int new_y = 150;
		DiagramElementsModifier.moveGraphicalEditPart(classEp, new Point(new_x, new_y));

		Assert.assertTrue(classEp.getModel() instanceof CSSShapeImpl);
		LayoutConstraint layoutConstraint = ((CSSShapeImpl) classEp.getModel()).getLayoutConstraint();
		Assert.assertTrue(layoutConstraint instanceof BoundsImpl);
		Assert.assertEquals(new_x, ((BoundsImpl) layoutConstraint).getX());
		Assert.assertEquals(new_y, ((BoundsImpl) layoutConstraint).getY());
	}

	
	/**
	 * Test for  resizeGraphicalEditPart
	 */
	@Test
	public void resizeGraphicalEditPartTest(){
		
		List<Pair<String, Class<?>>> objects = Arrays.asList(
				new Pair<String, Class<?>>("ClassB", org.eclipse.uml2.uml.Class.class)
				);
		init(objects, Arrays.asList());
		@SuppressWarnings("unchecked")
		List<EditPart> eps = getDiagramEditPart().getChildren();
		ClassEditPart classEp = (ClassEditPart) eps.get(0);
		
		int new_width = 200;
		int new_height = 600;
		DiagramElementsModifier.resizeGraphicalEditPart(classEp, new_width, new_height);

		Assert.assertTrue(classEp.getModel() instanceof CSSShapeImpl);
		LayoutConstraint layoutConstraint = ((CSSShapeImpl) classEp.getModel()).getLayoutConstraint();
		
		Assert.assertTrue(layoutConstraint instanceof BoundsImpl);
		
		Assert.assertEquals(new_width, ((BoundsImpl) layoutConstraint).getWidth());
		Assert.assertEquals(new_height, ((BoundsImpl) layoutConstraint).getHeight());
	}
	
	/**
	 * Test for  setConnectionAnchors
	 */
	@Test
	public void setConnectionAnchorsTest(){
		Pair<String, Class<?>> A = new Pair<String, Class<?>>("ClassA", org.eclipse.uml2.uml.Class.class);
		Pair<String, Class<?>> B = new Pair<String, Class<?>>("ClassB", org.eclipse.uml2.uml.Class.class);
		
		List<Pair<String, Class<?>>> objects = Arrays.asList(A,	B);
		List<Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>> associations = Arrays.asList(
					new Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>(A, B)
				);
		
		init(objects, associations);
		@SuppressWarnings("unchecked")
		List<EditPart> eps = getDiagramEditPart().getChildren();
		ClassEditPart classAEp = (ClassEditPart) eps.get(0);
		ClassEditPart classBEp = (ClassEditPart) eps.get(1);

		@SuppressWarnings("unchecked")
		List<ConnectionEditPart> conns = classBEp.getSourceConnections();
		ConnectionEditPart assoc = conns.get(0);
		DiagramElementsModifier.setConnectionAnchors(assoc, "(1, 0.5)", "(0, 0.5)");
		
		ConnectionAnchor source = classAEp.getSourceConnectionAnchor(assoc);
		ConnectionAnchor target = classBEp.getTargetConnectionAnchor(assoc);
		Point sourceReferencePoint = ((SlidableAnchor) source).getReferencePoint();
		Point targetReferencePoint = ((SlidableAnchor) target).getReferencePoint();
		PrecisionPoint sourceAnchor = SlidableAnchor.getAnchorRelativeLocation(sourceReferencePoint, source.getOwner().getBounds());
		PrecisionPoint targetAnchor = SlidableAnchor.getAnchorRelativeLocation(targetReferencePoint, target.getOwner().getBounds());

		Assert.assertEquals(new PrecisionPoint(1, 0.5), sourceAnchor);
		Assert.assertEquals(new PrecisionPoint(0, 0.5), targetAnchor);
	}

	/**
	 * Test for setConnectionBendpoints
	 */
	@Test
	public void setConnectionBendpointsTest(){
		Pair<String, Class<?>> A = new Pair<String, Class<?>>("ClassA", org.eclipse.uml2.uml.Class.class);
		Pair<String, Class<?>> B = new Pair<String, Class<?>>("ClassB", org.eclipse.uml2.uml.Class.class);
		
		List<Pair<String, Class<?>>> objects = Arrays.asList(A,	B);
		List<Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>> associations = Arrays.asList(
					new Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>(A, B)
				);
		
		init(objects, associations);
		@SuppressWarnings("unchecked")
		List<EditPart> eps = getDiagramEditPart().getChildren();
		ClassEditPart classAEp = (ClassEditPart) eps.get(0);

		@SuppressWarnings("unchecked")
		List<ConnectionEditPart> conns = classAEp.getTargetConnections();
		ConnectionEditPart assoc = conns.get(0);
		
		List<Point> bendpointslist = Arrays.asList(new Point(10, 10), new Point(150, 200), new Point(400, 300));
		DiagramElementsModifier.setConnectionBendpoints(assoc, bendpointslist);
	}
	
	/**
	 * Test for hideConnectionLabelsforEdtiparts
	 */
	@Test
	public void hideConnectionLabelsforEdtipartsTest(){
		Pair<String, Class<?>> A = new Pair<String, Class<?>>("ClassA", org.eclipse.uml2.uml.Class.class);
		Pair<String, Class<?>> B = new Pair<String, Class<?>>("ClassB", org.eclipse.uml2.uml.Class.class);
		Pair<String, Class<?>> C = new Pair<String, Class<?>>("ClassC", org.eclipse.uml2.uml.Class.class);
		
		List<Pair<String, Class<?>>> objects = Arrays.asList(A,	B, C);
		List<Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>> associations = Arrays.asList(
					new Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>(A, B),
					new Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>(C, C)
				);
		
		init(objects, associations);
		@SuppressWarnings("unchecked")
		List<EditPart> eps = getDiagramEditPart().getChildren();
		ClassEditPart classAEp = (ClassEditPart) eps.get(0);
		ClassEditPart classBEp = (ClassEditPart) eps.get(1);
		ClassEditPart classCEp = (ClassEditPart) eps.get(2);

		@SuppressWarnings("unchecked")
		List<ConnectionEditPart> connsA = classAEp.getSourceConnections();
		Assert.assertTrue(connsA.size() == 0);
		
		@SuppressWarnings("unchecked")
		List<ConnectionEditPart> connsB = classBEp.getSourceConnections();
		
		Assert.assertTrue(connsB.size() == 1);
		ConnectionEditPart assoc1 = connsB.get(0);
		
		@SuppressWarnings("unchecked")
		List<ConnectionEditPart> connsC = classCEp.getSourceConnections();
		
		Assert.assertTrue(connsB.size() == 1);
		ConnectionEditPart assoc2 = connsC.get(0);
		
		
		DiagramElementsModifier
			.hideConnectionLabelsForEditParts(Arrays.asList(classAEp, classBEp), Arrays.asList());
		DiagramElementsModifier
			.hideConnectionLabelsForEditParts(Arrays.asList(classCEp), Arrays.asList(AssociationNameEditPart.class));
		
		Assert.assertTrue(assoc1.getChildren().get(0) instanceof AppliedStereotypeAssociationEditPart);
		AppliedStereotypeAssociationEditPart ass1_child1 = (AppliedStereotypeAssociationEditPart) assoc1.getChildren().get(0);
		Assert.assertFalse(((View) ass1_child1.getModel()).isVisible());
		
		Assert.assertTrue(assoc1.getChildren().get(1) instanceof AssociationNameEditPart);
		AssociationNameEditPart ass1_child2 = (AssociationNameEditPart) assoc1.getChildren().get(1);
		Assert.assertFalse(((View) ass1_child2.getModel()).isVisible());
		
		Assert.assertTrue(assoc1.getChildren().get(2) instanceof AssociationEndTargetEditPart);
		AssociationEndTargetEditPart ass1_child3 = (AssociationEndTargetEditPart) assoc1.getChildren().get(2);
		Assert.assertFalse(((View) ass1_child3.getModel()).isVisible());
		
		Assert.assertTrue(assoc1.getChildren().get(3) instanceof AssociationEndSourceEditPart);
		AssociationEndSourceEditPart ass1_child4 = (AssociationEndSourceEditPart) assoc1.getChildren().get(3);
		Assert.assertFalse(((View) ass1_child4.getModel()).isVisible());
		
		Assert.assertTrue(assoc1.getChildren().get(4) instanceof AssociationMultiplicitySourceEditPart);
		AssociationMultiplicitySourceEditPart ass1_child5 = (AssociationMultiplicitySourceEditPart) assoc1.getChildren().get(4);
		Assert.assertFalse(((View) ass1_child5.getModel()).isVisible());
		
		Assert.assertTrue(assoc1.getChildren().get(5) instanceof AssociationMultiplicityTargetEditPart);
		AssociationMultiplicityTargetEditPart ass1_child6 = (AssociationMultiplicityTargetEditPart) assoc1.getChildren().get(5);
		Assert.assertFalse(((View) ass1_child6.getModel()).isVisible());
		
		
		Assert.assertTrue(assoc2.getChildren().get(0) instanceof AppliedStereotypeAssociationEditPart);
		AppliedStereotypeAssociationEditPart ass2_child1 = (AppliedStereotypeAssociationEditPart) assoc2.getChildren().get(0);
		Assert.assertFalse(((View) ass2_child1.getModel()).isVisible());
		
		Assert.assertTrue(assoc2.getChildren().get(1) instanceof AssociationNameEditPart);
		AssociationNameEditPart ass2_child2 = (AssociationNameEditPart) assoc2.getChildren().get(1);
		Assert.assertTrue(((View) ass2_child2.getModel()).isVisible());
		
		Assert.assertTrue(assoc2.getChildren().get(2) instanceof AssociationEndTargetEditPart);
		AssociationEndTargetEditPart ass2_child3 = (AssociationEndTargetEditPart) assoc2.getChildren().get(2);
		Assert.assertFalse(((View) ass2_child3.getModel()).isVisible());
		
		Assert.assertTrue(assoc2.getChildren().get(3) instanceof AssociationEndSourceEditPart);
		AssociationEndSourceEditPart ass2_child4 = (AssociationEndSourceEditPart) assoc2.getChildren().get(3);
		Assert.assertFalse(((View) ass2_child4.getModel()).isVisible());
		
		Assert.assertTrue(assoc2.getChildren().get(4) instanceof AssociationMultiplicitySourceEditPart);
		AssociationMultiplicitySourceEditPart ass2_child5 = (AssociationMultiplicitySourceEditPart) assoc2.getChildren().get(4);
		Assert.assertFalse(((View) ass2_child5.getModel()).isVisible());
		
		Assert.assertTrue(assoc2.getChildren().get(5) instanceof AssociationMultiplicityTargetEditPart);
		AssociationMultiplicityTargetEditPart ass2_child6 = (AssociationMultiplicityTargetEditPart) assoc2.getChildren().get(5);
		Assert.assertFalse(((View) ass2_child6.getModel()).isVisible());
	}
}
