package hu.elte.txtuml.export.papyrus;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.impl.ViewImpl;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.commands.OpenDiagramCommand;
import org.eclipse.papyrus.commands.wrappers.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationUtils;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;

public class DiagramManager {
	private IMultiDiagramEditor editor;
	
	public DiagramManager(IMultiDiagramEditor editor) {
		this.editor = editor;
	}
	
	public void createDiagrams(List<Element> Containers, ICreationCommand diagramCreationCommand){
		ModelSet ms;
		try {
			ms = editor.getServicesRegistry().getService(ModelSet.class);
			for(Element Container : Containers){
				diagramCreationCommand.createDiagram(ms, Container, ((NamedElement)Container).getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public EList<EObject> getDiagrams() throws ServiceException{
		Resource notationResource;
		notationResource = NotationUtils.getNotationModel(editor.getServicesRegistry().getService(ModelSet.class)).getResource();
		EList<EObject> list =  notationResource.getContents();
		return list;
	}
	
	public EObject getDiagramContainer(Diagram diagram){
		try {
			openDiagram(diagram);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		DiagramEditPart ep = getActiveDiagramEditPart();
		ViewImpl mod = (ViewImpl) ep.getModel();
		EObject element = mod.getElement();
		return element;
	}
	
	public void openDiagram(Diagram diag)  throws ServiceException{
		TransactionalEditingDomain editingDomain = editor.getServicesRegistry().getService(TransactionalEditingDomain.class);
		editingDomain.getCommandStack().execute(new GMFtoEMFCommandWrapper(new OpenDiagramCommand(editingDomain, diag)));
	}

	public DiagramEditPart getActiveDiagramEditPart(){
		IEditorPart ied = editor.getActiveEditor();
		
		if(ied instanceof IDiagramWorkbenchPart){
			return ((IDiagramWorkbenchPart) ied).getDiagramEditPart();
		}else{
			return null;
		}
	}
}
