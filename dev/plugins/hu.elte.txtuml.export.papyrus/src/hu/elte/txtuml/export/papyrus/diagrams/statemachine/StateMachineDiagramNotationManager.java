package hu.elte.txtuml.export.papyrus.diagrams.statemachine;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.TransitionGuardEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.TransitionNameEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.TransitionStereotypeEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

public interface StateMachineDiagramNotationManager {

	enum ConnectionLabelType {
		Name(TransitionNameEditPart.VISUAL_ID), StereoType(TransitionStereotypeEditPart.VISUAL_ID), Guard(
				TransitionGuardEditPart.VISUAL_ID);

		private String visualID;

		public String getVisualID() {
			return visualID;
		}

		private ConnectionLabelType(String visualID) {
			this.visualID = visualID;
		}
	}

	void createStateForRegion(Region region, State state, Rectangle bounds, IProgressMonitor monitor);

	void createInitialStateForRegion(Region region, Pseudostate InitialState, Rectangle bounds,
			IProgressMonitor monitor);

	void createTransitionForRegion(Region region, Vertex source, Vertex target, Transition transition,
			List<Point> route, String sourceAnchor, String targetAnchor, IProgressMonitor monitor);

	void createRegionForState(State state, Region region, IProgressMonitor monitor);

	void changeBoundsOfElement(Element elem, Rectangle boundsForElement, IProgressMonitor monitor);

	/**
	 * Returns the bounds of element if found, default bounds otherwise
	 * 
	 * @param elem
	 * @param monitor
	 * @return
	 */
	Rectangle getBoundsOfElement(Element elem, IProgressMonitor monitor);

	void hideConnectionLabelOfTransition(Transition transition, ConnectionLabelType name);

}