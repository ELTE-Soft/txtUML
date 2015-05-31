package hu.elte.txtuml.export.papyrus;

import java.util.List;

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

/**
 * Controls the diagrams. 
 *
 * @author András Dobreff
 */
public class DiagramManager {
	private IMultiDiagramEditor editor;
	
	/**
	 * The constructor
	 * @param editor - The editor to the instance will be attached.
	 */
	public DiagramManager(IMultiDiagramEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Calls the createDiagram method of the {@link ICreationCommand} with the containers.
	 * The ModelSet will be the ModelSet that is attached to the editor of the DiagramManager.
	 * The diagrams name will be the name of the container.
	 * @param containers - the {@link Element}s thats children will be placed on the diagram.
	 * @param diagramCreationCommand - the Command that will be executed.
	 */
	public void createDiagrams(List<Element> containers, ICreationCommand diagramCreationCommand){
		ModelSet ms;
		try {
			ms = editor.getServicesRegistry().getService(ModelSet.class);
			for(Element container : containers){
				diagramCreationCommand.createDiagram(ms, container, ((NamedElement)container).getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the previously created diagrams.
	 * @return The list of the the Diagrams
	 * @throws ServiceException
	 */
	public List<Diagram> getDiagrams() throws ServiceException{
		Resource notationResource;
		notationResource = NotationUtils.getNotationModel(editor.getServicesRegistry().getService(ModelSet.class)).getResource();
		@SuppressWarnings("unchecked")
		List<Diagram> list = (List<Diagram>)(List<?>) notationResource.getContents();
		return list;
	}
	
	/**
	 * Gets the EObjects thats children is on the diagram 
	 * @param diagram - The Diagram
	 * @return Container of the diagram
	 */
	public Element getDiagramContainer(Diagram diagram){
		try {
			openDiagram(diagram);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		DiagramEditPart ep = getActiveDiagramEditPart();
		ViewImpl mod = (ViewImpl) ep.getModel();
		EObject element = mod.getElement();
		return (Element) element;
	}
	
	/**
	 * Opens the tab of diagram in the editor
	 * @param diag - The diagram is to be opened
	 * @throws ServiceException
	 */
	public void openDiagram(Diagram diag)  throws ServiceException{
		TransactionalEditingDomain editingDomain = editor.getServicesRegistry().getService(TransactionalEditingDomain.class);
		editingDomain.getCommandStack().execute(new GMFtoEMFCommandWrapper(new OpenDiagramCommand(editingDomain, diag)));
	}

	/**
	 * Gets the DiagramEditPart that is referenced to the editor.
	 * @return Returns the DiagramEditPart or null if the editor is not an {@link IDiagramWorkbenchPart}
	 */
	public DiagramEditPart getActiveDiagramEditPart(){
		IEditorPart ied = editor.getActiveEditor();
		
		if(ied instanceof IDiagramWorkbenchPart){
			return ((IDiagramWorkbenchPart) ied).getDiagramEditPart();
		}else{
			return null;
		}
	}
}
