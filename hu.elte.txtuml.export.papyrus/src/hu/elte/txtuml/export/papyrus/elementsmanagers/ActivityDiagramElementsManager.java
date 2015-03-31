package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.ModelManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.uml2.uml.*;

public class ActivityDiagramElementsManager extends AbstractDiagramElementsManager{
	
	private List<java.lang.Class<?>> NodesToBeAdded;
	private List<java.lang.Class<?>> ConnectorsToBeAdded;
	
	
	public ActivityDiagramElementsManager(ModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		NodesToBeAdded = generateNodesToBeAdded();
		ConnectorsToBeAdded = generateConnectorsToBeAdded(); 
	}

	private List<java.lang.Class<?>> generateNodesToBeAdded() {
		List<java.lang.Class<?>> nodes = Arrays.asList(
				InitialNode.class, FinalNode.class,
				ForkNode.class, JoinNode.class,
				DecisionNode.class, MergeNode.class,
				OpaqueAction.class, ReadSelfAction.class,
				AddVariableValueAction.class, AddStructuralFeatureValueAction.class,
				SendSignalAction.class, CallOperationAction.class);
		return nodes;
	}
	
	private List<java.lang.Class<?>> generateConnectorsToBeAdded() {
		List<java.lang.Class<?>> connectors = Arrays.asList(ControlFlow.class, ObjectFlow.class);
		return connectors;
	}

	public void addElementsToDiagram(List<Element> elements) throws ServiceException {
		List<java.lang.Class<?>> types = new LinkedList<java.lang.Class<?>>();
		types.addAll(NodesToBeAdded);
		types.addAll(ConnectorsToBeAdded);
		
		
		EditPart activityEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		
		for(java.lang.Class<?> type : types){
			List<Element> listofTypes = modelManager.getElementsOfTypeFromList(elements, type);
			if(!listofTypes.isEmpty()){
				super.addElementsToEditpart(activityContentEditpart, listofTypes);
			}
		}
	}
}
