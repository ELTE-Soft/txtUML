package hu.elte.txtuml.export.papyrus.api;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.BasicCompartment;
import org.eclipse.gmf.runtime.notation.Compartment;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.gmfdiag.css.CSSBasicCompartmentImpl;
import org.eclipse.papyrus.uml.diagram.clazz.part.UMLDiagramEditorPlugin;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.utils.Logger;

public class ClassDiagramElementUtils {

	public static void createClassForDiagram(Diagram diagram, org.eclipse.uml2.uml.Class objectToDisplay,
			TransactionalEditingDomain domain, IProgressMonitor monitor){
		Runnable runnable = new  Runnable() {
			@Override
			public void run() {
				PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
				String hint = "2008";
				Node newNode = ViewService.createNode(diagram, objectToDisplay, hint, diagramPrefHint);
				
				for(Property property : objectToDisplay.allAttributes()){
					
					createPropertyForNode(newNode, property, domain, monitor);
				}
			}
		};
		runInTransactionalCommand(runnable, domain, "Creating Class for diagram "+diagram.getName(), monitor);
	}
	
	public static void createPropertyForNode(Node node, Property propertyToDisplay,
			TransactionalEditingDomain domain, IProgressMonitor monitor){
		
		@SuppressWarnings("unchecked")
		EList<View> children = node.getChildren();
		BasicCompartment comp =  (BasicCompartment) children.get(2); //TODO get with type if possible
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
				String hint = "3012";
				Node newPropery = ViewService.createNode(comp, propertyToDisplay, hint, diagramPrefHint);
			}
		};
		
		runInTransactionalCommand(runnable, domain, "Creating Property for Node "+node, monitor);
	}
	private static void runInTransactionalCommand(Runnable runnable,
			TransactionalEditingDomain domain, String commandName, IProgressMonitor monitor){
		
		ICommand cmd =new AbstractTransactionalCommand(domain, commandName, null) {
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
				runnable.run();
				return CommandResult.newOKCommandResult();
			}
		};
		
		try {
			cmd.execute(monitor, null);
		} catch (ExecutionException e) {
			Logger.executor.error("Could not execute command "+cmd+" ("+commandName+")");
		}
	}
}
