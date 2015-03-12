package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.ModelManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.uml2.uml.Element;

public class ActivityDiagramElementsManager extends AbstractDiagramElementsManager{
	
	private List<String> NodesToBeAdded;
	private List<String> ConnectorsToBeAdded;
	
	
	public ActivityDiagramElementsManager(ModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		NodesToBeAdded = generateNodesToBeAdded();
		ConnectorsToBeAdded = generateConnectorsToBeAdded(); 
	}

	private List<String> generateNodesToBeAdded() {
		List<String> nodes = Arrays.asList("InitialNode", "FinalNode", "ForkNode", "JoinNode",
				"DecisionNode", "MergeNode", "OpaqueAction", "ReadSelfAction",
				"AddVariableValueAction", "AddStructuralFeatureValueAction", "SendSignalAction",
				"CallOperationAction");
		return nodes;
	}
	
	private List<String> generateConnectorsToBeAdded() {
		List<String> connectors = Arrays.asList("ControlFlow", "ObjectFlow");
		return connectors;
	}

	public void addElementsToDiagram(List<Element> elements) throws ServiceException {
		List<String> types = new LinkedList<String>();
		types.addAll(NodesToBeAdded);
		types.addAll(ConnectorsToBeAdded);
		
		
		EditPart activityEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		
		for(String type : types){
			List<Element> listofTypes = modelManager.getElementsOfTypeFromList(elements, type);
			if(!listofTypes.isEmpty()){
				super.addElementsToEditpart(activityContentEditpart, listofTypes);
			}
		}
	}
}
