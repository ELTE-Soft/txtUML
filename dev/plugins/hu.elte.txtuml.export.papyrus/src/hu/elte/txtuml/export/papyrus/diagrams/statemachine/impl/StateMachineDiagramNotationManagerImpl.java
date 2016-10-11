package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateMachineCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.StateNameEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.part.UMLDiagramEditorPlugin;
import org.eclipse.papyrus.uml.diagram.statemachine.providers.UMLElementTypes;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.export.papyrus.diagrams.AbstractDiagramNotationManager;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramNotationManager;
import hu.elte.txtuml.utils.Logger;

public class StateMachineDiagramNotationManagerImpl extends AbstractDiagramNotationManager implements StateMachineDiagramNotationManager {
	private static final PreferencesHint DIAGRAM_PREFERENCES_HINT = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;

	private static final Rectangle defaultStateBounds = new Rectangle(0, 0, 50, 50);

	private Map<EObject, Node> notationMap = new HashMap<>();

	public StateMachineDiagramNotationManagerImpl(Diagram diagram, TransactionalEditingDomain domain) {
		super(diagram);
		this.domain = domain;
		initNotationMap();
	}

	private void initNotationMap() {
		if (this.diagram.getChildren().isEmpty()) {
			Logger.sys.error("Created StateMachine Diagram has no generated content!");
			return;
		}

		Node stateMachineView = (Node) diagram.getChildren().get(0);
		EObject stateMachineModel = stateMachineView.getElement();
		this.notationMap.put(stateMachineModel, stateMachineView);

		List<Node> regionsForStateMachine = regionsForState(stateMachineView);
		if (regionsForStateMachine.size() != 1) {
			Logger.sys.error("One and only one region is expected for the StateMachine!");
			return;
		}

		Node regionView = regionsForStateMachine.get(0); // expecting one and
															// only one region
		EObject regionModel = regionView.getElement();
		this.notationMap.put(regionModel, regionView);
	}

	private static List<Node> regionsForState(Node stateMachineView) {
		ArrayList<Node> regionsForState = new ArrayList<>();
		Node regionCompartement = findRegionCompartementOfState(stateMachineView);

		@SuppressWarnings("unchecked")
		List<Node> regions = regionCompartement.getChildren();
		regionsForState.addAll(regions);
		return regionsForState;
	}

	@Override
	public void createStateForRegion(Region region, State state, Rectangle bounds, IProgressMonitor monitor) {

		Node regionNode = this.notationMap.get(region);
		Node node = findCanvasOfRegion(regionNode);

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.State_6000).getSemanticHint();
			Node newNode = ViewService.createNode(node, state, hint, DIAGRAM_PREFERENCES_HINT);
			newNode.setLayoutConstraint(createBounds(bounds, defaultStateBounds));

			this.notationMap.put(state, newNode);
		};

		runInTransactionalCommand(runnable, "Creating State for Node " + node, monitor);

	}

	@Override
	public void createInitialStateForRegion(Region region, Pseudostate InitialState, Rectangle bounds,
			IProgressMonitor monitor) {
		Node regionNode = this.notationMap.get(region);
		Node node = findCanvasOfRegion(regionNode);

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Pseudostate_8000).getSemanticHint();
			Node newNode = ViewService.createNode(node, InitialState, hint, DIAGRAM_PREFERENCES_HINT);
			newNode.setLayoutConstraint(createBounds(bounds, defaultStateBounds));
		};

		runInTransactionalCommand(runnable, "Creating Initialstate for Node " + node, monitor);
	}

	@Override
	public void createTransitionForRegion(Region region, Vertex source, Vertex target, Transition transition,
			List<Point> route, String sourceAnchor, String targetAnchor, IProgressMonitor monitor) {

		Node regionNode = this.notationMap.get(region);
		Node node = findCanvasOfRegion(regionNode);

		View sourceView = getViewOfModel(source, node);
		View targetView = getViewOfModel(target, node);
		IElementType elementType = UMLElementTypes.Transition_7000;
		String hint = ((IHintedType) elementType).getSemanticHint();

		Runnable runnable = () -> {
			Edge edge = (Edge) ViewService.getInstance().createEdge(elementType, this.diagram, hint, ViewUtil.APPEND,
					DIAGRAM_PREFERENCES_HINT);
			edge.setElement(transition);
			edge.setSource(sourceView);
			edge.setTarget(targetView);
			edge.setBendpoints(createBendsPoints(route));
			createAnchorsForEdge(edge, sourceAnchor, targetAnchor);
		};

		runInTransactionalCommand(runnable, "Creating Transition  between " + source + " and " + target, monitor);
	}

	@Override
	public void createRegionForState(State state, Region region, IProgressMonitor monitor) {
		final int COMPOSITE_STATE_HEADER_HEIGHT = 20;
		
		Node stateNode = this.notationMap.get(state);
		Node node = findRegionCompartementOfState(stateNode);
		Node stateNameNode = findStateNameNodeOfState(stateNode);
		
		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Region_3000).getSemanticHint();
			Node newNode = ViewService.createNode(node, region, hint, DIAGRAM_PREFERENCES_HINT);
			
			//The height of header must be set. Otherwise the view will be corrupted.
			Bounds nameLayoutConstraint = NotationFactory.eINSTANCE.createBounds();
			nameLayoutConstraint.setHeight(COMPOSITE_STATE_HEADER_HEIGHT);
			stateNameNode.setLayoutConstraint(nameLayoutConstraint);
			
			this.notationMap.put(region, newNode);
		};

		runInTransactionalCommand(runnable, "Creating Region for Node " + node, monitor);
	}

	private static Node findRegionCompartementOfState(Node stateNode) {
		@SuppressWarnings("unchecked")
		List<Node> decorationNodes = stateNode.getChildren();
		for (Node decorationNode : decorationNodes) {
			if (decorationNode.getType().equals(String.valueOf(StateMachineCompartmentEditPart.VISUAL_ID))
					|| decorationNode.getType().equals(String.valueOf(StateCompartmentEditPart.VISUAL_ID))) {
				return decorationNode;
			}
		}
		return null;
	}
	
	private static Node findStateNameNodeOfState(Node stateNode) {
		@SuppressWarnings("unchecked")
		List<Node> decorationNodes = stateNode.getChildren();
		for (Node decorationNode : decorationNodes) {
			if (decorationNode.getType().equals(String.valueOf(StateNameEditPart.VISUAL_ID))) {
				return decorationNode;
			}
		}
		return null;
	}

	private Node findCanvasOfRegion(Node regionNode) {
		return (Node) regionNode.getChildren().get(0); // a decorationNode
														// between region and
														// substates
	}
}
