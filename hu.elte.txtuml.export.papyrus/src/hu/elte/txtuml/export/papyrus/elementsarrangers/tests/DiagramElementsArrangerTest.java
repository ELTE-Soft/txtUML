package hu.elte.txtuml.export.papyrus.elementsarrangers.tests;

import static org.junit.Assert.fail;
import hu.elte.txtuml.export.papyrus.DiagramManager;
import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;
import hu.elte.txtuml.export.papyrus.PapyrusModelManager;
import hu.elte.txtuml.export.papyrus.ProjectManager;
import hu.elte.txtuml.export.papyrus.elementsarrangers.AbstractDiagramElementsArranger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.ui.action.actions.CommonActionConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.core.editor.BackboneException;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.extension.commands.IModelCreationCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.diagram.wizards.category.DiagramCategoryDescriptor;
import org.eclipse.papyrus.uml.diagram.wizards.category.DiagramCategoryRegistry;
import org.eclipse.papyrus.uml.tools.model.UmlModel;
import org.eclipse.papyrus.uml.tools.model.UmlUtils;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DiagramElementsArrangerTest {
	
	DiagramEditPart diagEp;
	DiagramElementsArranger diagramElementsArranger;
	
	class DiagramElementsArranger extends AbstractDiagramElementsArranger{
		public DiagramElementsArranger(DiagramEditPart diagramEditPart) {
			super(diagramEditPart);
		}

		@Override
		public void arrange() {
			//do nothing
		}
		
		@Override
		public void hideConnectionLabelsForEditParts(
				List<EditPart> elements, List<Class<?>> excluding) {
			super.hideConnectionLabelsForEditParts(elements, excluding);
		}
		
		@Override
		public boolean isInstanceOfAny(Object object,
				Collection<Class<?>> types) {
			return super.isInstanceOfAny(object, types);
		}
		
		@Override
		public void moveGraphicalEditPart(GraphicalEditPart graphEp, Point p) {
			super.moveGraphicalEditPart(graphEp, p);
		}
		
		@Override
		public void resizeGraphicalEditPart(GraphicalEditPart graphEP,
				int new_width, int new_height) {
			super.resizeGraphicalEditPart(graphEP, new_width, new_height);
		}
		
		@Override
		public void setConnectionAnchors(ConnectionNodeEditPart connection,
				String src, String trg) {
			super.setConnectionAnchors(connection, src, trg);
		}
		
		@Override
		public void SetConnectionBendpoints(
				ConnectionNodeEditPart connection, List<Point> bendpoints) {
			super.SetConnectionBendpoints(connection, bendpoints);
		}
	}

	@Before
	public void SetUp(){		
		ProjectManager pm = new ProjectManager();
		String projectname = "testproject";
		IProject proj = pm.createProject(projectname);
		pm.openProject(proj);
		PapyrusModelCreator pmc = new PapyrusModelCreator();
		try {
			pmc.init(projectname+"/model");
			pmc.createPapyrusModel();
			
			IFile file = pmc.getDi(); 
			
			IEditorInput editorInput = new FileEditorInput(file);
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IEditorPart ed = null;
			ed = IDE.openEditor(window.getActivePage(),
					editorInput, "org.eclipse.papyrus.infra.core.papyrusEditor", true);
		
			IMultiDiagramEditor editor = (IMultiDiagramEditor) ed;
			
			ModelSet modelSet = editor.getServicesRegistry().getService(ModelSet.class);
			Map<String, DiagramCategoryDescriptor> categories = DiagramCategoryRegistry.getInstance().getDiagramCategoryMap();
			IModelCreationCommand creationCommand = categories.get("uml").getCommand();
			creationCommand.createModel(modelSet);
			
			PapyrusModelManager manager = new PapyrusModelManager(editor);
			
			
			
			UmlModel umlModel = (UmlModel) modelSet.getModel(UmlModel.MODEL_ID);
			
			
			manager.createAndFillDiagrams();
			
			IEditorPart ied = editor.getActiveEditor();
			
			diagEp = ((IDiagramWorkbenchPart) ied).getDiagramEditPart();
			diagramElementsArranger = new DiagramElementsArranger(diagEp);
			
			EObject model = UmlUtils.getUmlModel().lookupRoot();
			org.eclipse.uml2.uml.Package modelpackage = (org.eclipse.uml2.uml.Package) model;
			
			TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(model);
			ted.getCommandStack().execute(new RecordingCommand(ted) {
				protected void doExecute() {
					modelpackage.createOwnedClass("ClassName", true);
				}
			});
			
			
		} catch (ServiceException | NotFoundException | IOException | CoreException | BackboneException e) {
			fail("Initialization Error");
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test(){
		List<EditPart> eps = diagEp.getChildren();
	}

}
