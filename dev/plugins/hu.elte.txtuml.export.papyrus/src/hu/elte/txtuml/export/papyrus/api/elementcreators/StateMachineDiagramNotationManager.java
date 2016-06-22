package hu.elte.txtuml.export.papyrus.api.elementcreators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.papyrus.uml.diagram.statemachine.providers.UMLElementTypes;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateMachineCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.part.UMLDiagramEditorPlugin;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.utils.Logger;

public class StateMachineDiagramNotationManager extends AbstractDiagramNotationManager {
	private static final PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
	
	private static final Rectangle defaultStateBounds = new Rectangle(0,0,50,50);
	
	private Map<EObject, Node> notationMap = new HashMap<>();
	
	public StateMachineDiagramNotationManager(Diagram diagram, TransactionalEditingDomain domain){
		super(diagram);
		this.domain = domain;
		initNotationMap();
	}
	
	private void initNotationMap(){
		if(this.diagram.getChildren().isEmpty()){
			Logger.sys.error("Created StateMachine Diagram has no generated content!");
			return;
		}
		
		Node stateMachineView = (Node) diagram.getChildren().get(0);
		EObject stateMachineModel = stateMachineView.getElement();
		this.notationMap.put(stateMachineModel, stateMachineView);
		
		List<Node> regionsForStateMachine = this.regionsForState(stateMachineView);
		if(regionsForStateMachine.size() != 1){
			Logger.sys.error("One and only one region is expected for the StateMachine!");
			return;
		}
		
		Node regionView = regionsForStateMachine.get(0); //expecting one and only one region
		EObject regionModel = regionView.getElement();
		this.notationMap.put(regionModel, regionView);
	}
	
	private List<Node> regionsForState(Node stateMachineView) {
		ArrayList<Node> regionsForState = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<Node> decorationNodes = stateMachineView.getChildren();
		for(Node decorationNode : decorationNodes){
			if(decorationNode.getType().equals(String.valueOf(StateMachineCompartmentEditPart.VISUAL_ID))
					|| decorationNode.getType().equals(String.valueOf(StateCompartmentEditPart.VISUAL_ID))){
				
				@SuppressWarnings("unchecked")
				List<Node> regions = decorationNode.getChildren();
				for(Node region : regions){
					regionsForState.add(region);
				}
			}
		}
		return regionsForState;
	}

	public void createStateForRegion(Region region, Vertex state, IProgressMonitor monitor){
		
		Node regionNode = this.notationMap.get(region);
		Node node = (Node) regionNode.getChildren().get(0); //a decorationNode between region and substates
		
		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.State_6000).getSemanticHint();
			ViewService.createNode(node, state, hint, StateMachineDiagramNotationManager.diagramPrefHint);
		};
		
		runInTransactionalCommand(runnable, "Creating State for Node "+node, monitor);
	}
}

