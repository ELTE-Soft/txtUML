package hu.elte.txtuml.export.papyrus.api;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.clazz.part.UMLDiagramEditorPlugin;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;

public class ClassDiagramElementCreator extends AbstractDiagramElementCreator{

	public ClassDiagramElementCreator(TransactionalEditingDomain domain) {
		this.domain = domain;
	}

	public void createClassForDiagram(Diagram diagram, org.eclipse.uml2.uml.Class objectToDisplay, IProgressMonitor monitor){
		Runnable runnable = new  Runnable() {
			@Override
			public void run() {
				PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
				String hint = "2008";
				Node newNode = ViewService.createNode(diagram, objectToDisplay, hint, diagramPrefHint);
				
				for(Property property : objectToDisplay.allAttributes()){
					createPropertyForNode(newNode, property, monitor);
				}
			}
		};
		runInTransactionalCommand(runnable, "Creating Class for diagram "+diagram.getName(), monitor);
	}
	
	public void createPropertyForNode(Node node, Property propertyToDisplay,IProgressMonitor monitor){
		
		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		BasicCompartment comp =  (BasicCompartment) children.get(2); //TODO get with type if possible
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
				String hint = "3012";
				ViewService.createNode(comp, propertyToDisplay, hint, diagramPrefHint);
			}
		};
		
		runInTransactionalCommand(runnable, "Creating Property for Node "+node, monitor);
	}

	public void createSignalForDiagram(Diagram diagram, Signal e, IProgressMonitor monitor) {
		// TODO Auto-generated method stub	
	}
}
