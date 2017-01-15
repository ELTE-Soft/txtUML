package hu.elte.txtuml.export.papyrus.diagrams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.gmf.tooling.runtime.providers.DiagramElementTypes;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.TransactionalCommandRunner;
import hu.elte.txtuml.export.papyrus.utils.LayoutUtils;

public class AbstractDiagramNotationManager {

	protected TransactionalEditingDomain domain;

	protected List<DiagramElementTypes> types = new ArrayList<DiagramElementTypes>();

	protected Diagram diagram;

	protected AbstractDiagramNotationManager(Diagram diagram) {

		this.diagram = diagram;

		// Papyrus requires to create reference to the UMLElementTypes provider
		// for each diagram type unless an exception will be thrown because they
		// aren't set up
		LayoutUtils.getDisplay().syncExec(() -> {
			types.add(org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes.TYPED_INSTANCE);
			types.add(org.eclipse.papyrus.uml.diagram.statemachine.providers.UMLElementTypes.TYPED_INSTANCE);
			types.add(org.eclipse.papyrus.uml.diagram.activity.providers.UMLElementTypes.TYPED_INSTANCE);
			// TODO: Extend this list
		});
	}

	protected void runInTransactionalCommand(Runnable runnable, String commandName, IProgressMonitor monitor) {
		TransactionalCommandRunner.getInstance().runInTransactionalCommand(runnable, this.domain, commandName, monitor);
	}

	/**
	 * creates the appropriate bounds that can be added as
	 * {@link org.eclipse.gmf.runtime.notation.LayoutConstraint
	 * LayoutConstraint} to a {@link org.eclipse.gmf.runtime.notation.Node Node}
	 * 
	 * @param bounds
	 * @param defaultBounds
	 * @return
	 */
	protected Bounds createBounds(Rectangle bounds, Rectangle defaultBounds) {
		bounds = bounds == null ? defaultBounds : bounds;

		Bounds layoutConstraint = NotationFactory.eINSTANCE.createBounds();
		layoutConstraint.setX(bounds.x);
		layoutConstraint.setY(bounds.y);
		layoutConstraint.setWidth(bounds.width);
		layoutConstraint.setHeight(bounds.height);
		return layoutConstraint;
	}

	protected void createAnchorsForEdge(Edge edge, String sourceAnchor, String targetAnchor) {
		IdentityAnchor sourceanchor = NotationFactory.eINSTANCE.createIdentityAnchor();
		sourceanchor.setId(sourceAnchor);
		IdentityAnchor targetanchor = NotationFactory.eINSTANCE.createIdentityAnchor();
		targetanchor.setId(targetAnchor);
		edge.setSourceAnchor(sourceanchor);
		edge.setTargetAnchor(targetanchor);
	}

	protected RelativeBendpoints createBendsPoints(List<Point> route) {
		RelativeBendpoints bendpoints = NotationFactory.eINSTANCE.createRelativeBendpoints();
		if (route != null) {
			Point sourceAnchor = route.get(0);
			Point targetAnchor = route.get(route.size() - 1);
			List<RelativeBendpoint> relativePoints = route.stream()
					.map((p) -> new RelativeBendpoint(p.x - sourceAnchor.x, p.y - sourceAnchor.y, p.x - targetAnchor.x,
							p.y - targetAnchor.y))
					.collect(Collectors.toList());

			bendpoints.setPoints(relativePoints);
		}
		return bendpoints;
	}

	protected static View getViewOfModel(Element model, View container) {
		@SuppressWarnings("unchecked")
		EList<View> children = container.getChildren();

		for (View child : children) {
			if (child.getElement().equals(model))
				return child;
		}
		return null;
	}
}
