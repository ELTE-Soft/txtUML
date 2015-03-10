package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.ModelManager;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.uml2.uml.Element;

public abstract class AbstractDiagramElementsManager{
		
	protected ModelManager modelManager;
	DiagramEditPart diagramEditPart;
	
	public AbstractDiagramElementsManager(ModelManager modelManager, DiagramEditPart diagramEditPart) {
		this.modelManager = modelManager;
		this.diagramEditPart = diagramEditPart;
	}
	
	protected void addElementsToEditpart(EditPart EP, List<Element> diagramElements) {
		if(!diagramElements.isEmpty()){
			DropObjectsRequest dropObjectsRequest = new DropObjectsRequest();
			dropObjectsRequest.setObjects(diagramElements);
			dropObjectsRequest.setLocation(new Point(0,0));
			Command commandDrop = EP.getCommand(dropObjectsRequest);
			if (commandDrop != null){
				commandDrop.execute();
			}
		}
	}
	
	protected void removeEditParts(EditingDomain editingDomain, List<EditPart> editParts) {
			List<Object> modelElements = new LinkedList<Object>();
			for(EditPart editPart : editParts){
				modelElements.add(editPart.getModel());
			}
			
			org.eclipse.emf.common.command.Command command = RemoveCommand.create(editingDomain, modelElements);
			if(command instanceof RemoveCommand){
				RemoveCommand removeCommand = (RemoveCommand) command;
				editingDomain.getCommandStack().execute(removeCommand);
			}
	}

	public abstract void addElementsToDiagram(List<Element> elements) throws ServiceException;
}