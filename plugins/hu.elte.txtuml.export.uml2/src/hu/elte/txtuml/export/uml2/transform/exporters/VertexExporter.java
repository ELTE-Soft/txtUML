package hu.elte.txtuml.export.uml2.transform.exporters;

import hu.elte.txtuml.export.uml2.transform.backend.ExportException;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

public class VertexExporter {

	private final ModelExporter modelExporter;
	private final StateMachine stateMachine;
	private final Region region;

	public VertexExporter(ModelExporter modelExporter,StateMachine stateMachine, Region region) {
		this.modelExporter = modelExporter;
		this.stateMachine = stateMachine;
		this.region = region;
	}

	/**
	 * Exports the specified vertex.
	 * 
	 * @param vertexDeclaration
	 *            The type declaration of the txtUML vertex.
	 *
	 * @author Adam Ancsin
	 */
	public void exportVertex(TypeDeclaration vertexDeclaration) {
		Vertex vertex = createVertex(vertexDeclaration);

		if (ElementTypeTeller.isCompositeState(vertexDeclaration)) {
			exportSubRegion(vertexDeclaration, (State) vertex);
		}

		if (ElementTypeTeller.isState(vertexDeclaration)) {
// TODO: Uncomment this	when activity export gets fixed.	
//			exportStateEntryAction(vertexDeclaration, (State) vertex);
//			exportStateExitAction(vertexDeclaration, (State) vertex);
		}

		modelExporter.getMapping().put(
				SharedUtils.qualifiedName(vertexDeclaration), vertex);
	}

	/**
	 * Exports a sub-region (a region belonging to a state).
	 * 
	 * @param stateDeclaration
	 *            The type declaration of the state.
	 * @param state
	 *            The UML2 state.
	 *
	 * @author Adam Ancsin
	 */
	private void exportSubRegion(TypeDeclaration stateDeclaration, State state) {
		Region subRegion = state.createRegion(state.getName());
		modelExporter.getRegionExporter().exportRegion(stateDeclaration, stateMachine,
				subRegion);

		subRegion.setState(state);
	}

	/**
	 * Exports the entry action of a state.
	 * 
	 * @param stateDeclaration
	 *            The type declaration txtUML state.
	 * @param exportedState
	 *            The exported UML2 state.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("unused")
	private void exportStateEntryAction(TypeDeclaration stateDeclaration,
			State exportedState) {
		MethodDeclaration entryMethodDeclaration = SharedUtils
				.findMethodDeclarationByName(stateDeclaration, "entry");

		if (entryMethodDeclaration != null) {
			Activity activity = (Activity) exportedState.createEntry(
					exportedState.getName() + "_entry",
					UMLPackage.Literals.ACTIVITY);

			MethodBodyExporter.export(activity, modelExporter,
					entryMethodDeclaration);
		}
	}

	/**
	 * Exports the exit action of a state.
	 * 
	 * @param stateDeclaration
	 *            The type declaration txtUML state.
	 * @param exportedState
	 *            The exported UML2 state.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressWarnings("unused")
	private void exportStateExitAction(TypeDeclaration stateDeclaration,
			State exportedState) {
		MethodDeclaration exitMethodDeclaration = SharedUtils
				.findMethodDeclarationByName(stateDeclaration, "exit");

		if (exitMethodDeclaration != null) {
			Activity activity = (Activity) exportedState.createExit(
					exportedState.getName() + "_exit",
					UMLPackage.Literals.ACTIVITY);

			MethodBodyExporter.export(activity, modelExporter,
					exitMethodDeclaration);
		}
	}

	/**
	 * Creates a vertex of the right type (state/initial/choice) based on the
	 * given txtUML vertex type declaration.
	 * 
	 * @param vertexDeclaration
	 *            The type declaration of the vertex.
	 * @return The created vertex.
	 * @throws ExportException
	 *
	 * @author Adam Ancsin
	 */
	private Vertex createVertex(TypeDeclaration vertexDeclaration) {
		if (ElementTypeTeller.isInitialPseudoState(vertexDeclaration)) {
			return createInitial(vertexDeclaration);
		} else if (ElementTypeTeller.isChoicePseudoState(vertexDeclaration)) {
			return createChoice(vertexDeclaration);
		} else {
			return createVertex(vertexDeclaration, UMLPackage.Literals.STATE);
		}

	}

	private Vertex createVertex(TypeDeclaration vertexDeclaration, EClass type) {
		return region.createSubvertex(vertexDeclaration.getName()
				.getFullyQualifiedName(), type);
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
	private Pseudostate createInitial(TypeDeclaration vertexDeclaration) {
		return (Pseudostate) createVertex(vertexDeclaration,
				UMLPackage.Literals.PSEUDOSTATE);
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
		Pseudostate result = (Pseudostate) createVertex(vertexDeclaration,
				UMLPackage.Literals.PSEUDOSTATE);
		result.setKind(PseudostateKind.CHOICE_LITERAL);
		return result;
	}

}
