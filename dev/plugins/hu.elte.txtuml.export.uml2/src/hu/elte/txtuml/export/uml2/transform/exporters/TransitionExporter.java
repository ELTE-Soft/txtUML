package hu.elte.txtuml.export.uml2.transform.exporters;

import java.util.Arrays;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.utils.jdt.SharedUtils;

public class TransitionExporter {

	private final ModelExporter modelExporter;
	private final StateMachine stateMachine;
	private final Region region;

	public TransitionExporter(ModelExporter modelExporter,
			StateMachine stateMachine, Region region) {
		this.modelExporter = modelExporter;
		this.stateMachine = stateMachine;
		this.region = region;
	}

	/**
	 * Exports a transition.
	 * 
	 * @param transitionDeclaration
	 *            The type declaration of the txtUML transition.
	 * @return The exported UML2 transition.
	 */
	public Transition exportTransition(TypeDeclaration transitionDeclaration) {
		String transitionName = transitionDeclaration.getName()
				.getFullyQualifiedName();
		Vertex sourceVertex = obtainSourceVertexOfTransition(transitionDeclaration);
		Vertex targetVertex = obtainTargetVertexOfTransition(transitionDeclaration);

		Transition exportedTransition = createTransitionBetweenVertices(
				transitionName, sourceVertex, targetVertex);

		exportTrigger(transitionDeclaration, exportedTransition);
// TODO: Uncomment this	when activity export gets fixed.	
//		exportEffectAction(transitionDeclaration, exportedTransition);
//		exportGuard(transitionDeclaration, exportedTransition);

		modelExporter.getMapping().put(
				SharedUtils.qualifiedName(transitionDeclaration),
				exportedTransition);

		return exportedTransition;
	}

	private Vertex obtainSourceVertexOfTransition(
			TypeDeclaration transitionDeclaration) {
		return obtainEndOfTransition(transitionDeclaration, From.class);
	}

	private Vertex obtainTargetVertexOfTransition(
			TypeDeclaration transitionDeclaration) {
		return obtainEndOfTransition(transitionDeclaration, To.class);
	}

	private Vertex obtainEndOfTransition(TypeDeclaration transitionDeclaration,
			java.lang.Class<?> endAnnotationClass) {
		Expression value = SharedUtils.obtainSingleMemberAnnotationValue(
				transitionDeclaration, endAnnotationClass);
		if (value instanceof TypeLiteral) {
			TypeLiteral typeLiteral = (TypeLiteral) value;

			String endName = typeLiteral.getType().resolveBinding().getName();
			return region.getSubvertex(endName);
		}
		return null;
	}

	private void exportTrigger(TypeDeclaration transitionDeclaration,
			Transition exportedTransition) {

		Expression triggerAnnotValue = SharedUtils
				.obtainSingleMemberAnnotationValue(transitionDeclaration,
						hu.elte.txtuml.api.model.Trigger.class);

		if (triggerAnnotValue instanceof TypeLiteral) {
			TypeLiteral typeLiteral = (TypeLiteral) triggerAnnotValue;

			String triggeringSignalName = typeLiteral.getType()
					.resolveBinding().getName();
			String triggeringEventName = triggeringSignalName + "_event";
			Trigger trigger = exportedTransition
					.createTrigger(triggeringSignalName);
			trigger.setEvent((Event) modelExporter.getExportedModel()
					.getPackagedElement(triggeringEventName));
		}

	}

	/**
	 * Exports the effect action of a transition.
	 * 
	 * @param transitionDeclaration
	 *            The type declaration txtUML state.
	 * @param exportedTransition
	 *            The exported UML2 transition.
	 */
	@SuppressWarnings("unused")
	private void exportEffectAction(TypeDeclaration transitionDeclaration,
			Transition exportedTransition) {
		MethodDeclaration effectMethodDeclaration = SharedUtils
				.findMethodDeclarationByName(transitionDeclaration, "effect");

		if (effectMethodDeclaration != null) {
			Activity activity = (Activity) exportedTransition.createEffect(
					exportedTransition.getName() + "_effect",
					UMLPackage.Literals.ACTIVITY);

			MethodBodyExporter.export(activity, modelExporter,
					effectMethodDeclaration);
		}
	}

	/**
	 * Exports the guard constraint of a transition.
	 * 
	 * @param transitionDeclaration
	 *            The type declaration of the txtUML transition.
	 * @param exportedTransition
	 *            The exported UML2 transition.
	 */
	@SuppressWarnings("unused")
	private void exportGuard(TypeDeclaration transitionDeclaration,
			org.eclipse.uml2.uml.Transition exportedTransition) {
		MethodDeclaration guardDeclaration = SharedUtils
				.findMethodDeclarationByName(transitionDeclaration, "guard");

		if (guardDeclaration != null) {
			// TODO decide guard container
			Activity activity = (Activity) stateMachine.createOwnedBehavior(
					exportedTransition.getName() + "_guard",
					UMLPackage.Literals.ACTIVITY);

			Parameter ret = UMLFactory.eINSTANCE.createParameter();
			ret.setDirection(ParameterDirectionKind.RETURN_LITERAL);
			ret.setType(modelExporter.getTypeExporter().getBoolean());

			MethodBodyExporter.export(activity, modelExporter,
					guardDeclaration, Arrays.asList(ret));

			OpaqueExpression opaqueExpression = UMLFactory.eINSTANCE
					.createOpaqueExpression();
			opaqueExpression.setBehavior(activity);

			Constraint constraint = UMLFactory.eINSTANCE.createConstraint();
			constraint.setSpecification(opaqueExpression);

			exportedTransition.setGuard(constraint);
		}
	}

	/**
	 * Creates an UML2 transition from the source UML2 vertex to the target UML2
	 * vertex.
	 * 
	 * @param name
	 *            The name of the transition.
	 * @param source
	 *            The source UML2 vertex.
	 * @param target
	 *            The target UML2 vertex.
	 * @return The created UML2 transition.
	 */
	private Transition createTransitionBetweenVertices(String name,
			Vertex source, Vertex target) {
		Transition transition = this.region.createTransition(name);
		transition.setSource(source);
		transition.setTarget(target);
		return transition;
	}

}
