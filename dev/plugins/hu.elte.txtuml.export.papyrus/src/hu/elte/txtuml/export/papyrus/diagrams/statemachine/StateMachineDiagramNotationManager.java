package hu.elte.txtuml.export.papyrus.diagrams.statemachine;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

public interface StateMachineDiagramNotationManager{

	void createStateForRegion(Region region, State state, Rectangle bounds, IProgressMonitor monitor);

	void createInitialStateForRegion(Region region, Pseudostate InitialState, Rectangle bounds,
			IProgressMonitor monitor);

	void createTransitionForRegion(Region region, Vertex source, Vertex target, Transition transition,
			List<Point> route, String sourceAnchor, String targetAnchor, IProgressMonitor monitor);

	void createRegionForState(State state, Region region, IProgressMonitor monitor);

	void changeBoundsOfElement(Element elem, Rectangle boundsForElement, IProgressMonitor monitor);

}