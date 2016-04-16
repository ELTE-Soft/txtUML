package hu.elte.txtuml.export.papyrus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSDiagramImpl;
import org.eclipse.papyrus.infra.gmfdiag.css.resource.CSSNotationResource;
import org.eclipse.uml2.uml.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import hu.elte.txtuml.export.papyrus.DiagramManager;

//TODO Rewrite these tests

public class DiagramManagerUnitTest {
	private DiagramManager diagramManager;
	private ModelSet ms;
	private DiagramEditor editorPart;
	@Before
	public void setUp(){
		IMultiDiagramEditor editor = Mockito.mock(IMultiDiagramEditor.class);

		ServicesRegistry reg = Mockito.mock(ServicesRegistry.class);
		this.ms = Mockito.mock(ModelSet.class);
		
		try {
			Mockito.when(reg.getService(ModelSet.class)).thenReturn(this.ms);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		this.editorPart = Mockito.mock(DiagramEditor.class);
		
		Mockito.when(editor.getServicesRegistry()).thenReturn(reg);
		Mockito.when(editor.getActiveEditor()).thenReturn(this.editorPart);
		
	//	this.diagramManager = new DiagramManager(editor);
	}
	
	@After
	public void tearDown(){
		this.diagramManager = null;
	}
	
	
	@Test
	public void testDiagramCreation(){
		Element elem = Mockito.mock(Element.class);
		ICreationCommand command = Mockito.mock(ICreationCommand.class); 
		String diagramName = "MockDiagram";		
		
		//diagramManager.createDiagram(elem, diagramName, command);
		
		verify(command).createDiagram(ms, elem, diagramName);
	}
	
	@Test
	public void testGetDiagramWithEmptyContent(){
		NotationModel notationModel = Mockito.mock(NotationModel.class);
		CSSNotationResource resource = Mockito.mock(CSSNotationResource.class);
		EList<EObject> content = new BasicEList<EObject>();
		
		Mockito.when(resource.getContents()).thenReturn(content);
		Mockito.when(notationModel.getResource()).thenReturn(resource);
		Mockito.when(ms.getModel(NotationModel.MODEL_ID)).thenReturn(notationModel);
		
		assertTrue(diagramManager.getDiagrams().isEmpty());
	}
	
	@Test
	public void testGetDiagramWithOneDiagram(){
		NotationModel notationModel = Mockito.mock(NotationModel.class);
		CSSNotationResource resource = Mockito.mock(CSSNotationResource.class);
		Diagram diagram = (Diagram) Mockito.mock(CSSDiagramImpl.class);
		EList<EObject> content = new BasicEList<EObject>(Arrays.asList(diagram));
		
		Mockito.when(resource.getContents()).thenReturn(content);
		Mockito.when(notationModel.getResource()).thenReturn(resource);
		Mockito.when(ms.getModel(NotationModel.MODEL_ID)).thenReturn(notationModel);
		
		assertTrue(diagramManager.getDiagrams().size() == 1);
		assertEquals(diagram, diagramManager.getDiagrams().get(0));
	}
	
	@Test
	public void testGetDiagramContainer(){
		Diagram diagram = Mockito.mock(Diagram.class);
		Element container = Mockito.mock(Element.class);
		Mockito.when(diagram.getElement()).thenReturn(container);
		
		Element result = diagramManager.getDiagramContainer(diagram);
		
		assertEquals(container, result);
	}
	
	@Test
	public void testGetDiagramEditPart(){
		DiagramEditPart diagEp = Mockito.mock(DiagramEditPart.class);
		Mockito.when(this.editorPart.getDiagramEditPart()).thenReturn(diagEp);
		
		DiagramEditPart result = diagramManager.getActiveDiagramEditPart();
		
		assertEquals(diagEp, result);
	}
}
