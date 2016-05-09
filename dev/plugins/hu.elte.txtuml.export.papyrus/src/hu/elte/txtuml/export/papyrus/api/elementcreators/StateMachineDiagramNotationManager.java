package hu.elte.txtuml.export.papyrus.api.elementcreators;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.papyrus.uml.diagram.statemachine.providers.UMLElementTypes;
import org.eclipse.papyrus.uml.diagram.statemachine.part.UMLDiagramEditorPlugin;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;

public class StateMachineDiagramNotationManager extends AbstractDiagramNotationManager {
	private static final PreferencesHint diagramPrefHint = UMLDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT;
	
	private static final Rectangle defaultStateBounds = new Rectangle(0,0,50,50);
	
	private Map<EObject, Node> notationMap = new HashMap<>();
	
	public StateMachineDiagramNotationManager(Diagram diagram, TransactionalEditingDomain domain){
		super(diagram);
		this.domain = domain;
	}
	
	public void createStateForRegion(Region region, State state, IProgressMonitor monitor){
		
		Node node = this.notationMap.get(region);
		
		Runnable runnable = () -> {
			String hint = ((IHintedType) UMLElementTypes.State_6000).getSemanticHint();
			ViewService.createNode(node, state, hint, StateMachineDiagramNotationManager.diagramPrefHint);
		};
		
		runInTransactionalCommand(runnable, "Creating State for Node "+node, monitor);
	}
}

