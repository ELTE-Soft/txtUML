package hu.elte.txtuml.export.papyrus.api;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.clazz.part.UMLDiagramEditorPlugin;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;

public class ClassDiagramElementCreator extends AbstractDiagramElementCreator {
	
	private static final Rectangle defaultClassBounds = new Rectangle(0, 0, 100, 100);

	public ClassDiagramElementCreator(TransactionalEditingDomain domain) {
		this.domain = domain;
	}

	public void createClassForDiagram(Diagram diagram, Class objectToDisplay, Rectangle bounds,
			IProgressMonitor monitor) {
		
		Runnable runnable = () -> {
			PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
			String hint = "2008";
			Node newNode = ViewService.createNode(diagram, objectToDisplay, hint, diagramPrefHint);
			
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

		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		BasicCompartment comp = (BasicCompartment) children.get(2); // TODO get
																	// with type
																	// if
																	// possible
																	// 7017

		Runnable runnable = () -> {
			PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
			String hint = "3012";
			ViewService.createNode(comp, propertyToDisplay, hint, diagramPrefHint);
		};

		runInTransactionalCommand(runnable, "Creating Property for Node " + node, monitor);
	}

	public void createOperationForNode(Node node, Operation operationToDisplay, IProgressMonitor monitor) {

		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		BasicCompartment comp = (BasicCompartment) children.get(3); // TODO get
																	// with type
																	// if
																	// possible
																	// 7018

		Runnable runnable = () -> {
			PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
			String hint = "3013";
			ViewService.createNode(comp, operationToDisplay, hint, diagramPrefHint);
		};

		runInTransactionalCommand(runnable, "Creating Operation for Node " + node, monitor);
	}

	public void createSignalForDiagram(Diagram diagram, Signal signal, Rectangle bounds, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}
}
