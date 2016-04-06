package hu.elte.txtuml.export.papyrus.api;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.part.UMLDiagramEditorPlugin;
import org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.utils.Logger;

public class ClassDiagramElementCreator extends AbstractDiagramElementCreator {

	private static final PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;

	private static final Rectangle defaultClassBounds = new Rectangle(0, 0, 100, 100);

	public ClassDiagramElementCreator(TransactionalEditingDomain domain) {
		this.domain = domain;
	}

	public void createClassForDiagram(Diagram diagram, Class objectToDisplay, Rectangle bounds,
			IProgressMonitor monitor) {

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Class_2008).getSemanticHint();
			Node newNode = ViewService.createNode(diagram, objectToDisplay, hint,
					ClassDiagramElementCreator.diagramPrefHint);

			newNode.setLayoutConstraint(createBounds(bounds, defaultClassBounds));

			objectToDisplay.getAllAttributes().forEach((property) -> {
				createPropertyForNode(newNode, property, monitor);
			});

			objectToDisplay.getAllOperations().forEach((operation) -> {
				createOperationForNode(newNode, operation, monitor);
			});

		};
		runInTransactionalCommand(runnable, "Creating Class for diagram " + diagram.getName(), monitor);
	}

	public void createPropertyForNode(Node node, Property propertyToDisplay, IProgressMonitor monitor) {
		BasicCompartment comp = getPropertyCompartementOfNode(node);

		if (comp == null) {
			Logger.executor.info("Node " + node + " has no compartement for properties");
			return;
		}

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Property_3012).getSemanticHint();
			ViewService.createNode(comp, propertyToDisplay, hint, ClassDiagramElementCreator.diagramPrefHint);
		};

		runInTransactionalCommand(runnable, "Creating Property for Node " + node, monitor);
	}

	public void createOperationForNode(Node node, Operation operationToDisplay, IProgressMonitor monitor) {
		BasicCompartment comp = getOperationCompartementOfNode(node);

		if (comp == null) {
			Logger.executor.info("Node " + node + " has no compartement for operations");
			return;
		}

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Operation_3013).getSemanticHint();
			ViewService.createNode(comp, operationToDisplay, hint, ClassDiagramElementCreator.diagramPrefHint);
		};

		runInTransactionalCommand(runnable, "Creating Operation for Node " + node, monitor);
	}

	public void createSignalForDiagram(Diagram diagram, Signal signal, Rectangle bounds, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}

	public void createAssociationForNodes(Classifier source, Classifier target, Association assoc, Diagram diagram,
			List<Point> route, IProgressMonitor monitor) {

		View sourceView = getViewOfModel(source, diagram);
		View targetView = getViewOfModel(target, diagram);
		IElementType elementType = UMLElementTypes.Association_4001;
		String hint = ((IHintedType) elementType).getSemanticHint();

		Runnable runnable = () -> {
			Edge edge = (Edge) ViewService.getInstance().createEdge(elementType, diagram, hint, ViewUtil.APPEND,
					ClassDiagramElementCreator.diagramPrefHint);
			edge.setElement(assoc);
			edge.setSource(sourceView);
			edge.setTarget(targetView);
			edge.setBendpoints(createBendsPoints((Node) sourceView, (Node) targetView, route));
			System.out.println("");
		};

		runInTransactionalCommand(runnable, "Creating Assoc", monitor);
	}

	private RelativeBendpoints createBendsPoints(Node sourceNode, Node targetNode, List<Point> route) {
		Bounds slc = (Bounds) sourceNode.getLayoutConstraint();
		Bounds tlc = (Bounds) targetNode.getLayoutConstraint();
		Point sourceNodeCenter = new Point(slc.getX() + slc.getWidth() / 2, slc.getY() + slc.getHeight() / 2);
		Point targetNodeCenter = new Point(tlc.getX() + tlc.getWidth() / 2, tlc.getY() + slc.getHeight() / 2);

		RelativeBendpoints bendpoints = NotationFactory.eINSTANCE.createRelativeBendpoints();
		if (route != null) {
			List<RelativeBendpoint> relativePoints = route.stream()
					.map((p) -> new RelativeBendpoint(p.x - sourceNodeCenter.x, p.y - sourceNodeCenter.y,
							p.x - targetNodeCenter.x, p.y - targetNodeCenter.y))
					.collect(Collectors.toList());

			bendpoints.setPoints(relativePoints);
		}
		return bendpoints;
	}

	public void createGeneralizationForNodes(Node diagram, Classifier target, List<Point> route,
			IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}

	private static BasicCompartment getPropertyCompartementOfNode(Node node) {

		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		for (View child : children) {
			if (child.getType().equals(String.valueOf(ClassAttributeCompartmentEditPart.VISUAL_ID))) {
				return (BasicCompartment) child;
			}
		}
		return null;
	}

	private static BasicCompartment getOperationCompartementOfNode(Node node) {
		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		for (View child : children) {
			if (child.getType().equals(String.valueOf(ClassOperationCompartmentEditPart.VISUAL_ID))) {
				return (BasicCompartment) child;
			}
		}
		return null;
	}
}
