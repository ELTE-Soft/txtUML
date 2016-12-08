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
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.export.papyrus.diagrams.AbstractDiagramNotationManager;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramNotationManager;
import hu.elte.txtuml.utils.Logger;

public class StateMachineDiagramNotationManagerImpl extends AbstractDiagramNotationManager
		implements StateMachineDiagramNotationManager {
	private static final PreferencesHint DIAGRAM_PREFERENCES_HINT = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;

	private static final Rectangle defaultStateBounds() {
		return new Rectangle(0, 0, 50, 50);
	}

	private Map<EObject, Node> nodeMap = new HashMap<>();
	private Map<EObject, Edge> edgeMap = new HashMap<>();

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
		this.nodeMap.put(stateMachineModel, stateMachineView);

		List<Node> regionsForStateMachine = regionsForState(stateMachineView);
		if (regionsForStateMachine.size() != 1) {
			Logger.sys.error("One and only one region is expected for the StateMachine!");
			return;
		}

		Node regionView = regionsForStateMachine.get(0); // expecting one and
															// only one region

		// bounds is set for StateMachines by default. This can cause conflicts
		// later, hence it's eliminated
		eliminateDefaultSizeConstraints();

		EObject regionModel = regionView.getElement();
		this.nodeMap.put(regionModel, regionView);
	}

	private void eliminateDefaultSizeConstraints() {
		Runnable runnable = () -> {
			Node stateMachineView = (Node) diagram.getChildren().get(0);
			List<Node> regionsForStateMachine = regionsForState(stateMachineView);
			Node regionView = regionsForStateMachine.get(0);
			stateMachineView.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
			((Node) stateMachineView.getChildren().get(0))
					.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
			((Node) stateMachineView.getChildren().get(1))
					.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
			regionView.setLayoutConstraint(NotationFactory.eINSTANCE.createBounds());
		};

		runInTransactionalCommand(runnable, "Eliminating default layout constraints.", null);
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

		Node regionNode = this.nodeMap.get(region);
		Node node = findCanvasOfRegion(regionNode);

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.State_6000).getSemanticHint();
			Node newNode = ViewService.createNode(node, state, hint, DIAGRAM_PREFERENCES_HINT);
			newNode.setLayoutConstraint(createBounds(bounds, defaultStateBounds()));

			this.nodeMap.put(state, newNode);
		};

		runInTransactionalCommand(runnable, "Creating State for Node " + node, monitor);

	}

	@Override
	public void createInitialStateForRegion(Region region, Pseudostate InitialState, Rectangle bounds,
			IProgressMonitor monitor) {
		Node regionNode = this.nodeMap.get(region);
		Node node = findCanvasOfRegion(regionNode);

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Pseudostate_8000).getSemanticHint();
			Node newNode = ViewService.createNode(node, InitialState, hint, DIAGRAM_PREFERENCES_HINT);
			newNode.setLayoutConstraint(createBounds(bounds, defaultStateBounds()));

			this.nodeMap.put(InitialState, newNode);
		};

		runInTransactionalCommand(runnable, "Creating Initialstate for Node " + node, monitor);
	}

	@Override
	public void createTransitionForRegion(Region region, Vertex source, Vertex target, Transition transition,
			List<Point> route, String sourceAnchor, String targetAnchor, IProgressMonitor monitor) {

		Node regionNode = this.nodeMap.get(region);
		Node node = findCanvasOfRegion(regionNode);

		View sourceView = getViewOfModel(source, node);
		View targetView = getViewOfModel(target, node);
		IElementType elementType = UMLElementTypes.Transition_7000;
		String hint = ((IHintedType) elementType).getSemanticHint();

		Runnable runnable = () -> {
			Edge edge = (Edge) ViewService.getInstance().createEdge(elementType, this.diagram, hint, ViewUtil.APPEND, DIAGRAM_PREFERENCES_HINT);
			edge.setElement(transition);
			edge.setSource(sourceView);
			edge.setTarget(targetView);
			edge.setBendpoints(createBendsPoints(route));
			createAnchorsForEdge(edge, sourceAnchor, targetAnchor);

			this.edgeMap.put(transition, edge);
		};

		runInTransactionalCommand(runnable, "Creating Transition  between " + source + " and " + target, monitor);
	}

	@Override
	public void createRegionForState(State state, Region region, IProgressMonitor monitor) {
		final int COMPOSITE_STATE_HEADER_HEIGHT = StateMachineDiagramPixelDimensionProvider.STATE_HEADER_HEIGHT;

		Node stateNode = this.nodeMap.get(state);
		Node node = findRegionCompartementOfState(stateNode);
		Node stateNameNode = findStateNameNodeOfState(stateNode);

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Region_3000).getSemanticHint();
			Node newNode = ViewService.createNode(node, region, hint, DIAGRAM_PREFERENCES_HINT);

			// The height of header must be set. Otherwise the view will be
			// corrupted.
			Bounds nameLayoutConstraint = NotationFactory.eINSTANCE.createBounds();
			nameLayoutConstraint.setHeight(COMPOSITE_STATE_HEADER_HEIGHT);
			stateNameNode.setLayoutConstraint(nameLayoutConstraint);

			this.nodeMap.put(region, newNode);
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

	@Override
	public void changeBoundsOfElement(Element elem, Rectangle bounds, IProgressMonitor monitor) {
		Node node = this.nodeMap.get(elem);
		if (node != null) {
			Runnable runnable = () -> {
				node.setLayoutConstraint(createBounds(bounds, defaultStateBounds()));
			};

			runInTransactionalCommand(runnable, "Creating Initialstate for Node " + node, monitor);
		} else {
			Logger.sys.error("Cannot change bounds of node. This element (" + elem + ") has no node!");
		}
	}

	@Override
	public Rectangle getBoundsOfElement(Element elem, IProgressMonitor monitor) {
		Node node = this.nodeMap.get(elem);
		if (node != null) {
			Bounds bounds = (Bounds) node.getLayoutConstraint();
			return new Rectangle(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		}
		return defaultStateBounds();
	}

	@Override
	public void hideConnectionLabelOfTransition(Transition transition, ConnectionLabelType connectionLabelType) {
		Runnable runnable = () ->{
			Edge node = this.edgeMap.get(transition);
			@SuppressWarnings("unchecked")
			List<Node> nodes = node.getPersistedChildren();
	
			for (Node child : nodes) {
				if (child.getType().equals(String.valueOf(connectionLabelType.getVisualID()))) {
					child.setVisible(false);
				}
			}
		};
		runInTransactionalCommand(runnable, "Hiding connection labels ", null);
	}
}
