package hu.elte.txtuml.export.papyrus.api.elementcreators;

import java.util.List;

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
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.part.UMLDiagramEditorPlugin;
import org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.utils.Logger;

public class ClassDiagramNotationManager extends AbstractDiagramNotationManager {

	private static final PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;

	private static final Rectangle defaultClassBounds = new Rectangle(0, 0, 100, 100);

	public ClassDiagramNotationManager(TransactionalEditingDomain domain) {
		this.domain = domain;
	}

	public void createClassForDiagram(Diagram diagram, Class objectToDisplay, Rectangle bounds,
			IProgressMonitor monitor) {

		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.Class_2008).getSemanticHint();
			Node newNode = ViewService.createNode(diagram, objectToDisplay, hint,
					ClassDiagramNotationManager.diagramPrefHint);

			newNode.setLayoutConstraint(createBounds(bounds, defaultClassBounds));

			objectToDisplay.getAttributes().forEach((property) -> {
				createPropertyForNode(newNode, property, monitor);
			});

			objectToDisplay.getOperations().forEach((operation) -> {
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
			ViewService.createNode(comp, propertyToDisplay, hint, ClassDiagramNotationManager.diagramPrefHint);
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
			ViewService.createNode(comp, operationToDisplay, hint, ClassDiagramNotationManager.diagramPrefHint);
		};

		runInTransactionalCommand(runnable, "Creating Operation for Node " + node, monitor);
	}

	public void createSignalForDiagram(Diagram diagram, Signal signal, Rectangle bounds, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}

	public void createAssociationForNodes(Classifier source, Classifier target, Association assoc, List<Point> route,
			String sourceAnchor, String targetAnchor, Diagram diagram, IProgressMonitor monitor) {

		View sourceView = getViewOfModel(source, diagram);
		View targetView = getViewOfModel(target, diagram);
		IElementType elementType = UMLElementTypes.Association_4001;
		String hint = ((IHintedType) elementType).getSemanticHint();

		Runnable runnable = () -> {
			Edge edge = (Edge) ViewService.getInstance().createEdge(elementType, diagram, hint, ViewUtil.APPEND,
					ClassDiagramNotationManager.diagramPrefHint);
			edge.setElement(assoc);
			edge.setSource(sourceView);
			edge.setTarget(targetView);
			edge.setBendpoints(createBendsPoints(route));
			createAnchorsForEdge(edge, sourceAnchor, targetAnchor);
		};

		runInTransactionalCommand(runnable, "Creating Assoc", monitor);
	}

	public void createGeneralizationForNodes(Generalization generalization, List<Point> route, String sourceAnchor,
			String targetAnchor, Diagram diagram, IProgressMonitor monitor) {
		Classifier subclass = generalization.getSpecific();
		Classifier baseclass = generalization.getGeneral();

		View sourceView = getViewOfModel(subclass, diagram);
		View targetView = getViewOfModel(baseclass, diagram);
		IElementType elementType = UMLElementTypes.Generalization_4002;
		String hint = ((IHintedType) elementType).getSemanticHint();

		Runnable runnable = () -> {
			Edge edge = (Edge) ViewService.getInstance().createEdge(elementType, diagram, hint, ViewUtil.APPEND,
					ClassDiagramNotationManager.diagramPrefHint);
			edge.setElement(generalization);
			edge.setSource(sourceView);
			edge.setTarget(targetView);
			edge.setBendpoints(createBendsPoints(route));
			// Somehow the source and target nodes are swapped in case of
			// generalization
			createAnchorsForEdge(edge, targetAnchor, sourceAnchor);
		};

		runInTransactionalCommand(runnable, "Creating Generalization", monitor);
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