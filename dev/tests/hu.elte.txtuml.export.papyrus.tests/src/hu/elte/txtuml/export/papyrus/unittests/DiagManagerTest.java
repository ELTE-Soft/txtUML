package hu.elte.txtuml.export.papyrus.unittests;

import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.uml2.uml.Element;
import org.junit.After;
import org.junit.Before;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;


import hu.elte.txtuml.export.papyrus.DiagramManager;

public class DiagManagerTest {
	private IMultiDiagramEditor editor;
	private DiagramManager diagramManager;
	
	@Before
	public void setUp(){
		this.editor = Mockito.mock(IMultiDiagramEditor.class);
		
		this.diagramManager = new DiagramManager(editor);
	}
	
	@After
	public void tearDown(){
		this.diagramManager = null;
		this.editor = null;
	}
	
	@Test
	public void testDiagramCreation(){
		ServicesRegistry reg = Mockito.mock(ServicesRegistry.class);
		ModelSet ms = Mockito.mock(ModelSet.class);
		
		try {
			Mockito.when(reg.getService(ModelSet.class)).thenReturn(ms);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		Mockito.when(this.editor.getServicesRegistry()).thenReturn(reg);
		Element elem = Mockito.mock(Element.class);
		ICreationCommand command = Mockito.mock(ICreationCommand.class); 
		
		String diagramName = "MockDiagram";		
		diagramManager.createDiagram(elem, diagramName, command);
		
		verify(command).createDiagram(ms, elem, diagramName);
	}
}
