package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.export.papyrus.arrange.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.AbstractDiagramElementsManager;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramNotationManager;

/**
 * An abstract class for adding/removing elements to StateMachineDiagrams.
 */
public class StateMachineDiagramElementsManager extends AbstractDiagramElementsManager {

	protected StateMachineDiagramNotationManager notationManager;
	protected StateMachineDiagramElementsProvider elementsProvider;

	/**
	 * The Constructor
	 * 
	 * @param modelManager
	 *            - The ModelManager which serves the model elements
	 * @param diagramEditPart
	 *            - The DiagramEditPart of the diagram which is to be handled
	 */
	public StateMachineDiagramElementsManager(Diagram diagram, StateMachineDiagramElementsProvider provider,
			StateMachineDiagramNotationManager notation, IDiagramElementsArranger arranger, IProgressMonitor monitor) {
		super(diagram, monitor);
		this.notationManager = notation;
		this.arranger = arranger;
		this.elementsProvider = provider;

		arrangeWithErrorHandling();
	}

	public StateMachineDiagramElementsManager(Diagram diagram, StateMachineDiagramElementsProvider provider,
			StateMachineDiagramNotationManager notation, IDiagramElementsArranger arranger) {
		this(diagram, provider, notation, arranger, new NullProgressMonitor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.
	 * AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram() {
		Collection<Region> regions = this.elementsProvider.getMainRegions();
		addSubelementsRecursively(regions);
		addPaddingToStateMachine(regions);
	}

	private void addPaddingToStateMachine(Collection<Region> mainRegions) {
		StateMachine sm = this.elementsProvider.getMainElement();
		this.notationManager.changeBoundsOfElement(sm, this.arranger.getBoundsForElement(sm), this.monitor);
		mainRegions.forEach(region -> {
			this.elementsProvider.getElementsOfRegion(region).forEach(element -> {

				Rectangle boundsForElement = this.notationManager.getBoundsOfElement(element, this.monitor);
				boundsForElement.x = boundsForElement.x
						+ StateMachineDiagramPixelDimensionProvider.DEFAULT_ELEMENT_BORDER;
				boundsForElement.y = boundsForElement.y
						+ StateMachineDiagramPixelDimensionProvider.DEFAULT_ELEMENT_BORDER;
				this.notationManager.changeBoundsOfElement(element, boundsForElement, this.monitor);
			});
		});
	}

	private void addSubelementsRecursively(Collection<Region> regions) {
		regions.stream().forEach(region -> {
			this.elementsProvider.getStatesForRegion(region).forEach(state -> {
				this.notationManager.createStateForRegion(region, state, this.arranger.getBoundsForElement(state),
						this.monitor);
				if (!state.isSimple()) {
					state.getRegions().forEach(reg -> {
						this.notationManager.createRegionForState(state, reg, this.monitor);
					});
					addSubelementsRecursively(this.elementsProvider.getRegionsOfState(state));
				}
			});
			
			this.elementsProvider.getInitialStatesForRegion(region).forEach(initialState -> {
				this.notationManager.createInitialStateForRegion(region, initialState,
						this.arranger.getBoundsForElement(initialState), monitor);
			});
			
			this.elementsProvider.getChioceNodesForRegion(region).forEach(choiceNode -> {
				this.notationManager.createChoiceNodeForRegion(region, choiceNode,
						this.arranger.getBoundsForElement(choiceNode), monitor);
			});
			
			this.elementsProvider.getForkNodesForRegion(region).forEach(forkNode -> {
				this.notationManager.createForkNodeForRegion(region, forkNode,
						this.arranger.getBoundsForElement(forkNode), monitor);
			});

			this.elementsProvider.getJoinNodesForRegion(region).forEach(joinNode -> {
				this.notationManager.createJoinNodeForRegion(region, joinNode,
						this.arranger.getBoundsForElement(joinNode), monitor);
			});

			this.elementsProvider.getJunctionNodesForRegion(region).forEach(junctionNode -> {
				this.notationManager.createJunctionNodeForRegion(region, junctionNode,
						this.arranger.getBoundsForElement(junctionNode), monitor);
			});

			this.elementsProvider.getTerminateNodesForRegion(region).forEach(terminateNode -> {
				this.notationManager.createTerminateNodeForRegion(region, terminateNode,
						this.arranger.getBoundsForElement(terminateNode), monitor);
			});

			this.elementsProvider.getEntryPointForRegion(region).forEach(entryPoint -> {
				this.notationManager.createEntryPointForRegion(region, entryPoint,
						this.arranger.getBoundsForElement(entryPoint), monitor);
			});
			
			this.elementsProvider.getExitPointForRegion(region).forEach(exitPoint -> {
				this.notationManager.createExitPointForRegion(region, exitPoint,
						this.arranger.getBoundsForElement(exitPoint), monitor);
			});
			
			this.elementsProvider.getTransitionsForRegion(region).forEach(transition -> {

				Vertex source = transition.getSource();
				Vertex target = transition.getTarget();

				this.notationManager.createTransitionForRegion(region, source, target, transition,
						this.arranger.getRouteForConnection(transition),
						this.arranger.getSourceAnchorForConnection(transition),
						this.arranger.getTargetAnchorForConnection(transition), this.monitor);
				this.notationManager.hideConnectionLabelOfTransition(transition,
						StateMachineDiagramNotationManager.ConnectionLabelType.Name);
			});
		});
	}

}
