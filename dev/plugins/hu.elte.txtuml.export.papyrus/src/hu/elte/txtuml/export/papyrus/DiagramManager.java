package hu.elte.txtuml.export.papyrus;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.ui.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.sashwindows.di.service.IPageManager;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Controls the diagrams. 
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
		for(Element container : containers){
			this.createDiagram(container, ((NamedElement)container).getName(), diagramCreationCommand);
		}
	}
	
	/**
	 * Calls the createDiagram method of the {@link ICreationCommand} with the container.
	 * The ModelSet will be the ModelSet that is attached to the editor of the DiagramManager.
	 * 
	 * @param container - the {@link Element} thats children will be placed on the diagram.
	 * @param diagramName - The name of the new Diagram
	 * @param diagramCreationCommand - the Command that will be executed.
	 */
	public void createDiagram(Element container, String diagramName, ICreationCommand diagramCreationCommand){
		try {
			ModelSet ms = this.editor.getServicesRegistry().getService(ModelSet.class);
			diagramCreationCommand.createDiagram(ms, container, diagramName);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Gets the previously created diagrams.
	 * @return The list of the the Diagrams
	 */
	public List<Diagram> getDiagrams(){
		try{
			Resource notationResource;
			notationResource = ((NotationModel) this.editor.getServicesRegistry().getService(ModelSet.class).getModel(NotationModel.MODEL_ID)).getResource();
			List<Diagram> list = new ArrayList<Diagram>();
			
			notationResource.getContents().forEach( content -> {
				if(content instanceof Diagram)
					list.add((Diagram) content);
			});
			return list;
		}catch(ServiceException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the {@link Element} that is the container of the diagram
	 * @param diagram - The Diagram
	 * @return Container of the diagram
	 */
	public Element getDiagramContainer(Diagram diagram){
		return (Element) diagram.getElement();
	}
	
	/**
	 * Opens the tab of diagram in the editor
	 * @param diag - The diagram is to be opened
	 */
	public void openDiagram(Diagram diag){
		this.editor.getActiveEditor(); //Some kind of magic, but has to be done at least once before selecting different diagrams
		try{
			ServicesRegistry serviceRegistry = this.editor.getServicesRegistry();
			IPageManager pageMngr = ServiceUtils.getInstance().getIPageManager(serviceRegistry);
			pageMngr.selectPage(diag);
		}catch(ServiceException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the DiagramEditPart that is referenced to the editor.
	 * @return Returns the DiagramEditPart or null if the editor is not an {@link IDiagramWorkbenchPart}
	 */
	public DiagramEditPart getActiveDiagramEditPart(){
		IEditorPart ied = this.editor.getActiveEditor();
		
		if(ied instanceof DiagramEditor){
			return ((DiagramEditor) ied).getDiagramEditPart();
		}else{
			return null;
		}
	}
}
