package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.api.ElementsManagerUtils;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.uml2.uml.*;

/**
 * An abstract class for adding/removing elements to ActivityDiagrams.
 *
 * @author András Dobreff
 */
public class ActivityDiagramElementsManager extends AbstractDiagramElementsManager{
	
	private PreferencesManager preferencesManager;
	
	private List<java.lang.Class<?>> NodesToBeAdded;
	private List<java.lang.Class<?>> ConnectorsToBeAdded;
	
	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public ActivityDiagramElementsManager(UMLModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		preferencesManager = new PreferencesManager();
		NodesToBeAdded = generateNodesToBeAdded();
		ConnectorsToBeAdded = generateConnectorsToBeAdded(); 
	}

	/**
	 * Returns the types of nodes that are to be added
	 * @return Returns the types of nodes that are to be added
	 */
	private List<java.lang.Class<?>> generateNodesToBeAdded() {
		List<java.lang.Class<?>> nodes = new LinkedList<java.lang.Class<?>>(Arrays.asList(
				AcceptEventAction.class,
				Activity.class,
				ActivityFinalNode.class,
				AddStructuralFeatureValueAction.class,
				AddVariableValueAction.class,
				BroadcastSignalAction.class,
				CallBehaviorAction.class, 
				CallOperationAction.class,
				CreateObjectAction.class,
				DecisionNode.class,
				DestroyObjectAction.class,
				FinalNode.class,
				FlowFinalNode.class,
				ForkNode.class,
				InitialNode.class,
				JoinNode.class,
				MergeNode.class,
				OpaqueAction.class,
				ReadSelfAction.class,
				ReadStructuralFeatureAction.class,
				ReadVariableAction.class,
				SendObjectAction.class,
				SendSignalAction.class,
				ValueSpecificationAction.class
			));
		
		if(preferencesManager.getBoolean(PreferencesManager.ACTIVITY_DIAGRAM_COMMENT_PREF))
			nodes.add(Comment.class);
		
		return nodes;
	}
	
	/**
	 * Returns the types of connectors that are to be added
	 * @return Returns the types of connectors that are to be added 
	 */
	private List<java.lang.Class<?>> generateConnectorsToBeAdded() {
		List<java.lang.Class<?>> connectors = Arrays.asList(ControlFlow.class, ObjectFlow.class);
		return connectors;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(List<Element> elements) {
		List<java.lang.Class<?>> types = new LinkedList<java.lang.Class<?>>();
		types.addAll(NodesToBeAdded);
		types.addAll(ConnectorsToBeAdded);
		
		
		EditPart activityEditpart = (EditPart) diagramEditPart.getChildren().get(0);
		EditPart activityContentEditpart = (EditPart) activityEditpart.getChildren().get(5);
		
		for(java.lang.Class<?> type : types){
			List<Element> listofTypes = modelManager.getElementsOfTypeFromList(elements, type);
			if(!listofTypes.isEmpty()){
				ElementsManagerUtils.addElementsToEditpart(activityContentEditpart, listofTypes);
			}
		}
	}
}
