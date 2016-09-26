package hu.elte.txtuml.export.papyrus.diagrams.clazz;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Signal;

public interface ClassDiagramNotationManager {

	void createClassForDiagram(Class objectToDisplay, Rectangle bounds, IProgressMonitor monitor);

	void createSignalForDiagram(Signal signal, Rectangle bounds, IProgressMonitor monitor);

	void createAssociationForNodes(Classifier source, Classifier target, Association assoc, List<Point> route,
			String sourceAnchor, String targetAnchor, IProgressMonitor monitor);

	void createGeneralizationForNodes(Generalization generalization, List<Point> route, String sourceAnchor,
			String targetAnchor, IProgressMonitor monitor);

}