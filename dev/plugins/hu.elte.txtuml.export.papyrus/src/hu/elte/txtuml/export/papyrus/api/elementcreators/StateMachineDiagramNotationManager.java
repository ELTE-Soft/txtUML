package hu.elte.txtuml.export.papyrus.api.elementcreators;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.papyrus.uml.diagram.statemachine.providers.UMLElementTypes;
import org.eclipse.papyrus.uml.diagram.statemachine.part.UMLDiagramEditorPlugin;
import org.eclipse.uml2.uml.State;

public class StateMachineDiagramNotationManager extends AbstractDiagramNotationManager {
	private static final PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
	
	private static final Rectangle defaultStateBounds = new Rectangle(0,0,50,50);
	
	public StateMachineDiagramNotationManager(TransactionalEditingDomain domain){
		this.domain = domain;
	}
	
	public void createStateForRegion(Node node, State state, IProgressMonitor monitor){
		
		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.State_6000).getSemanticHint();
			ViewService.createNode(node, state, hint, StateMachineDiagramNotationManager.diagramPrefHint);
		};
		
		runInTransactionalCommand(runnable, "Creating State for Node "+node, monitor);
	}
}

