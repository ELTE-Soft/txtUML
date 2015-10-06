package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.visitors.BlockVisitor;
import hu.elte.txtuml.export.uml2.transform.visitors.GuardVisitor;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.export.uml2.utils.StateMachineUtils;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.FinalNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

/**
 * Instances of this class are responsible for importing state machine regions.
 * @author Adam Ancsin
 *
 */
public class RegionElementImporter{

	private ModelImporter modelImporter;
	private Region region;
	
	/**
	 * Creates a <code>RegionElementImporter</code> instance.
	 * 
	 */
	public RegionElementImporter(ModelImporter modelImporter, Region region) {
		this.modelImporter = modelImporter;
		this.region = region;
	}
		
	/**
	 * Gets the UML2 region.
	 * 
	 * @return The UML2 region.
	 *
	 * @author Adam Ancsin
	 */
	public Region getRegion() {
		return region;
	}
	
	/**
	 * Imports a sub-region (a region belonging to a state).
	 * 
	 * @param stateDeclaration
	 *            The type declaration of the state.
	 * @param state
	 *            The UML2 state.
	 * @return The imported sub-region.
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	private Region importSubRegion(TypeDeclaration stateDeclaration, State state) 
			throws ImportException {
		Region subRegion = state.createRegion(state.getName());
		this.modelImporter.getRegionImporter().importRegion(stateDeclaration, subRegion);
	
		subRegion.setState(state);
		return subRegion;
	}
	
	/**
	 * Imports the specified vertex.
	 * 
	 * @param vertexDeclaration
	 *            The type declaration of the txtUML vertex.
	 * @return The imported UML2 vertex.
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	public Vertex importVertex(TypeDeclaration vertexDeclaration) 
			throws ImportException {
		Vertex vertex = createVertex(vertexDeclaration);
		
		if(ElementTypeTeller.isCompositeState(vertexDeclaration)) {
			Region subRegion = importSubRegion(vertexDeclaration, (State) vertex);
			if(subRegion.getSubvertices().size() != 0 && !StateMachineUtils.containsInitial(subRegion)) {
				/*ImportWarningProvider.createWarning(
						txtUMLVertexClass.getName() +
						" has one or more vertices but no initial pseudostate (state machine will not be created)"
					);*/
				return null;
			}
		}
		
		if(ElementTypeTeller.isState(vertexDeclaration)) {
			importStateEntryAction(vertexDeclaration, (State) vertex);
			importStateExitAction(vertexDeclaration, (State) vertex);
		}
		
		ModelImporter.mapping.put(SharedUtils.qualifiedName(vertexDeclaration), vertex);
		return vertex;
	}
	

	/**
	 * Imports the entry action of a state.
	 * 
	 * @param stateDeclaration
	 *            The type declaration txtUML state.
	 * @param importedState
	 *            The imported UML2 state.
	 * 
	 * @author Adam Ancsin
	 */
	private void importStateEntryAction	(TypeDeclaration stateDeclaration,
			State importedState) {
		MethodDeclaration entryMethodDeclaration = SharedUtils
				.findMethodDeclarationByName(stateDeclaration, "entry");

		if (entryMethodDeclaration != null) {
			Activity activity = (Activity) importedState.createEntry(
					importedState.getName() + "_entry",
					UMLPackage.Literals.ACTIVITY);
			
			InitialNode initialNode = (InitialNode)activity.createOwnedNode(
					"initial_" + activity.getName(), UMLPackage.Literals.INITIAL_NODE);
					
			MethodBodyImporter importer = new MethodBodyImporter(
					activity,
					this.modelImporter.getTypeImporter(),
					this.modelImporter.getImportedModel());
			importer.setLastNode(initialNode);
			
			importer.createControlFlowBetweenActivityNodes(initialNode, importer.getBodyNode() );
			
			BlockVisitor vis = new BlockVisitor( importer );
			entryMethodDeclaration.accept(vis);
			
			FinalNode finalNode = (FinalNode)activity.createOwnedNode(
					"final_" + activity.getName(), UMLPackage.Literals.ACTIVITY_FINAL_NODE);
			
			importer.createControlFlowBetweenActivityNodes(importer.getBodyNode(), finalNode);
		}
	}
	
	/**
	 * Imports the exit action of a state.
	 * 
	 * @param stateDeclaration
	 *            The type declaration txtUML state.
	 * @param importedState
	 *            The imported UML2 state.
	 * 
	 * @author Adam Ancsin
	 */
	private void importStateExitAction
		(TypeDeclaration stateDeclaration, State importedState)
	{
		MethodDeclaration exitMethodDeclaration = SharedUtils
				.findMethodDeclarationByName(stateDeclaration, "exit");

		if (exitMethodDeclaration != null) {
			/*Activity activity = (Activity)*/ importedState.createExit(
					importedState.getName() + "_exit",
					UMLPackage.Literals.ACTIVITY);
			//TODO: import body
		}
	}
	
	/**
	 * Creates a vertex of the right type (state/initial/choice) based on the
	 * given txtUML vertex type declaration.
	 * 
	 * @param vertexDeclaration
	 *            The type declaration of the vertex.
	 * @return The created vertex.
	 * @throws ImportException
	 *
	 * @author Adam Ancsin
	 */
	private Vertex createVertex(TypeDeclaration vertexDeclaration)
			throws ImportException {
		if (ElementTypeTeller.isInitialPseudoState(vertexDeclaration)) {
			if (StateMachineUtils.containsInitial(region))
				throw new ImportException(vertexDeclaration.getName()
						+ " has more than one initial pseudostates");

			return createInitial(vertexDeclaration);
		} else if (ElementTypeTeller.isChoicePseudoState(vertexDeclaration)) {
			return createChoice(vertexDeclaration);
		} else {
			return region.createSubvertex(vertexDeclaration.getName()
					.getFullyQualifiedName(), UMLPackage.Literals.STATE);
		}
			
	}
	
	/**
	 * Creates an UML2 initial pseudostate.
	 * 
	 * @param vertexDeclaration
	 *            The type declaration of the vertex.
	 * @return The created UML2 initial pseudostate.
	 *
	 * @author Adam Ancsin
	 */
	private Vertex createInitial(TypeDeclaration vertexDeclaration) {
		return region.createSubvertex(vertexDeclaration.getName()
				.getFullyQualifiedName(), UMLPackage.Literals.PSEUDOSTATE);
	}
	
	/**
	 * Creates an UML2 choice pseudostate.
	 * 
	 * @param vertexDeclaration
	 *            The type declaration of the vertex.
	 * @return The created UML2 choice pseudostate.
	 *
	 * @author Adam Ancsin
	 */
	private Pseudostate createChoice(TypeDeclaration vertexDeclaration) {
		Pseudostate result = (Pseudostate) region.createSubvertex(
				vertexDeclaration.getName().getFullyQualifiedName(),
				UMLPackage.Literals.PSEUDOSTATE);
		result.setKind(PseudostateKind.CHOICE_LITERAL);
		return result;
	}

	/**
	 * Imports a transition.
	 * 
	 * @param transitionDeclaration
	 *            The type declaration of the txtUML transition.
	 * @return The imported UML2 transition.
	 * 
	 * @author Adam Ancsin
	 */
	public Transition importTransition(TypeDeclaration transitionDeclaration) {
		String transitionName = transitionDeclaration.getName().getFullyQualifiedName();
		Vertex sourceVertex = obtainSourceVertexOfTransition(transitionDeclaration);
		Vertex targetVertex = obtainTargetVertexOfTransition(transitionDeclaration);
	
		Transition importedTransition = createTransitionBetweenVertices(
				transitionName, sourceVertex, targetVertex);

        importTrigger(transitionDeclaration,importedTransition);
        importEffectAction(transitionDeclaration, importedTransition);
        importGuard(transitionDeclaration,importedTransition);
        
        ModelImporter.mapping.put(SharedUtils.qualifiedName(transitionDeclaration), importedTransition);
        
        return importedTransition;
    }   
	
	private void importTrigger(TypeDeclaration transitionDeclaration,
			Transition importedTransition) {
		
		Expression triggerAnnotValue = SharedUtils.obtainSingleMemberAnnotationValue(
				transitionDeclaration,
				hu.elte.txtuml.api.model.Trigger.class);
		
		if(triggerAnnotValue instanceof TypeLiteral) {
			TypeLiteral typeLiteral = (TypeLiteral) triggerAnnotValue;
			
			String triggeringSignalName  = typeLiteral.getType().resolveBinding().getName();
			String triggeringEventName = triggeringSignalName + "_event";
	        Trigger trigger = importedTransition.createTrigger(triggeringSignalName);
	        trigger.setEvent((Event) this.modelImporter.getImportedModel().getPackagedElement(triggeringEventName));
		}
		
	}

	private Vertex obtainSourceVertexOfTransition(TypeDeclaration transitionDeclaration) {
		return obtainEndOfTransition(transitionDeclaration, From.class);
	}
	
	private Vertex obtainTargetVertexOfTransition(TypeDeclaration transitionDeclaration) {
		return obtainEndOfTransition(transitionDeclaration, To.class);
	}
	
	private Vertex obtainEndOfTransition(TypeDeclaration transitionDeclaration, Class<?> endAnnotationClass) {
		Expression value = SharedUtils.obtainSingleMemberAnnotationValue(transitionDeclaration, endAnnotationClass);
		if(value instanceof TypeLiteral) {
			TypeLiteral typeLiteral = (TypeLiteral) value;
			
			String endName = typeLiteral.getType().resolveBinding().getName();				
			return region.getSubvertex(endName);
		}
		return null;
	}
	
	
	/**
	 * Imports the effect action of a transition.
	 * 
	 * @param transitionDeclaration
	 *            The type declaration txtUML state.
	 * @param importedTransition
	 *            The imported UML2 transition.
	 * 
	 * @author Adam Ancsin
	 */
	private void importEffectAction
		(TypeDeclaration transitionDeclaration, Transition importedTransition) {
		MethodDeclaration effectMethodDeclaration = 
				SharedUtils.findMethodDeclarationByName(transitionDeclaration, "effect");
		if(effectMethodDeclaration != null) {
			/*Activity activity = (Activity)*/ importedTransition.createEffect(
					importedTransition.getName() + "_effect",
					UMLPackage.Literals.ACTIVITY);
			
			//TODO: import body
		}
	}
	
	/**
	 * Imports the guard constraint of a transition.
	 * 
	 * @param transitionDeclaration
	 *            The type declaration of the txtUML transition.
	 * @param importedTransition
	 *            The imported UML2 transition.
	 *
	 * @author Adam Ancsin
	 */
	private void importGuard(TypeDeclaration transitionDeclaration,
			org.eclipse.uml2.uml.Transition importedTransition)	{
		MethodDeclaration guardDeclaration = 
				SharedUtils.findMethodDeclarationByName(transitionDeclaration, "guard");
		
		if(guardDeclaration != null) {
			GuardVisitor guardVisitor = new GuardVisitor();
			guardDeclaration.accept(guardVisitor);
			String guardExpression = guardVisitor.getGuardExpression();

			OpaqueExpression opaqueExpression = UMLFactory.eINSTANCE.createOpaqueExpression();
			opaqueExpression.getBodies().add(guardExpression);

			Constraint constraint = UMLFactory.eINSTANCE.createConstraint();
			constraint.setSpecification(opaqueExpression);

			importedTransition.setGuard(constraint);
		}
	}
	
	/**
	 * Creates a UML2 transition from the source UML2 vertex to the target UML2
	 * vertex.
	 * 
	 * @param name
	 *            The name of the transition.
	 * @param source
	 *            The source UML2 vertex.
	 * @param target
	 *            The target UML2 vertex.
	 * @return The created UML2 transition.
	 *
	 * @author Adam Ancsin
	 */
	private org.eclipse.uml2.uml.Transition createTransitionBetweenVertices(String name,
			Vertex source, Vertex target) {
		org.eclipse.uml2.uml.Transition transition = this.region.createTransition(name);
        transition.setSource(source);
        transition.setTarget(target);
        return transition;
	}
}
