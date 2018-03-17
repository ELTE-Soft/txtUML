package hu.elte.txtuml.xtxtuml.typesystem;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.AssociationEnd
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import java.util.ArrayList
import java.util.HashSet
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsTypeComputer
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.resource.BatchLinkableResource
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceFlags
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference
import org.eclipse.xtext.xbase.typesystem.references.ParameterizedTypeReference
import hu.elte.txtuml.xtxtuml.xtxtUML.TULinkExpression

class XtxtUMLTypeComputer extends XbaseWithAnnotationsTypeComputer {

	@Inject extension IJvmModelAssociations;
	@Inject extension IQualifiedNameProvider;

	def dispatch void computeTypes(TUClassPropertyAccessExpression accessExpr, ITypeComputationState state) {
		// Subexpressions have to be typed in a child computation state.
		// Expectations for children are enabled by default,
		// see val foo = if (bar) foobar else baz.
		val childState = state.withoutRootExpectation;

		// left child
		childState.computeTypes(accessExpr.left);

		// right child
		val rightChild = accessExpr.right;
		// if the reference couldn't be resolved
		if (rightChild?.name == null) {
			return;
		}

		switch (rightChild) {
			TUAssociationEnd: {
				val collectionOfAssocEndTypeRef =
					state.nullSafeJvmElementTypeRef(rightChild, AssociationEnd).allSuperTypes.
						findFirst[isType(AssociationEnd)] as ParameterizedTypeReference;

				state.acceptActualType(collectionOfAssocEndTypeRef.typeArguments.head);
			}
			TUPort: {
				state.acceptActualType(state.nullSafeJvmElementTypeRef(rightChild, Port));
			}
		}
	}

	/**
	 * Tries to acquire the associated JVM element of the given source element, considering
	 * the resource set of the given {@link ITypeComputationState}. An instance of
	 * <code>fallbackType</code> is returned if no such element has been found.
	 */
	def private nullSafeJvmElementTypeRef(ITypeComputationState state, EObject sourceElement, Class<?> fallbackType) {
		val inferredType = sourceElement.getPrimaryJvmElement as JvmType;
		return if (inferredType != null) {
			state.referenceOwner.toLightweightTypeReference(inferredType)
		} else {
			val newEquivalent = sourceElement.findNewJvmEquivalent(state.referenceOwner.contextResourceSet.resources);
			if (newEquivalent != null) {
				state.referenceOwner.toLightweightTypeReference(newEquivalent)
			} else {
				getTypeForName(fallbackType, state)
			}
		}
	}

	/**
	 * Returns a <i>JvmType</i> equivalent of <code>sourceElement</code> from the given <code>resourceSet</code>,
	 * or <code>null</code>, if no equivalent could be found. The call of this method might be expensive.
	 */
	def private findNewJvmEquivalent(EObject sourceElement, EList<Resource> resourceSet) {
		for (resource : resourceSet) {
			if (resource instanceof BatchLinkableResource) {
				val newEquivalent = resource.allContents.findFirst [
					it instanceof JvmGenericType && it.fullyQualifiedName == sourceElement.fullyQualifiedName
				];
				if (newEquivalent != null) {
					return newEquivalent as JvmGenericType;
				}
			}
		}

		return null;
	}

	def dispatch computeTypes(TUSignalAccessExpression sigExpr, ITypeComputationState state) {
		var container = sigExpr.eContainer;
		while (container != null && !(container instanceof TUEntryOrExitActivity) &&
			!(container instanceof TUTransition)) {
			container = container.eContainer;
		}

		var visitedStates = new HashSet<TUState>();
		var type = switch (container) {
			TUEntryOrExitActivity:
				if (container.eContainer instanceof TUState) {
					getCommonSignalSuperType(
						container.eContainer as TUState,
						state,
						container.entry,
						visitedStates
					)
				} else {
					getTypeForName(Signal, state)
				}
			TUTransition:
				getCommonSignalSuperType(container, state, visitedStates)
			default:
				getTypeForName(Signal, state)
		}

		state.acceptActualType(type);
	}

	/**
	 * Computes the common signal supertype accessible
	 * from the effect of the given transition.
	 */
	def private LightweightTypeReference getCommonSignalSuperType(
		TUTransition trans,
		ITypeComputationState computationState,
		HashSet<TUState> visitedStates
	) {
		var trigger = (trans.members.findFirst [
			it instanceof TUTransitionTrigger
		] as TUTransitionTrigger)?.trigger;

		if (trigger != null) {
			return computationState.nullSafeJvmElementTypeRef(trigger, Signal);
		}

		var from = (trans.members.findFirst [
			it instanceof TUTransitionVertex && (it as TUTransitionVertex).from
		] as TUTransitionVertex)?.vertex;

		if (from != null && from.type == TUStateType.CHOICE) {
			return getCommonSignalSuperType(from, computationState, true, visitedStates);
		}

		return getTypeForName(Signal, computationState);
	}

	/**
	 * Computes the common signal supertype accessible from the entry or exit activity of the given state.
	 * @param toState indicates whether the entry activity should be considered instead of the exit one
	 */
	def private LightweightTypeReference getCommonSignalSuperType(
		TUState state,
		ITypeComputationState computationState,
		boolean toState,
		HashSet<TUState> visitedStates
	) {
		if (!visitedStates.add(state)) {
			return getTypeForName(Signal, computationState);
		}

		val siblingsAndSelf = switch (c : state.eContainer) {
			TUState: c.members
			TUClass: c.members
		}

		var signalCandidates = new ArrayList<LightweightTypeReference>();
		for (siblingOrSelf : siblingsAndSelf) {
			if (siblingOrSelf instanceof TUTransition && ((siblingOrSelf as TUTransition).members.findFirst [
				it instanceof TUTransitionVertex && toState != (it as TUTransitionVertex).from
			] as TUTransitionVertex)?.vertex?.fullyQualifiedName == state.fullyQualifiedName) {
				signalCandidates.add(
					getCommonSignalSuperType(
						siblingOrSelf as TUTransition,
						computationState,
						visitedStates
					)
				);
			}
		}

		return if (!signalCandidates.empty) {
			getCommonSuperType(signalCandidates, computationState)
		} else {
			getTypeForName(Signal, computationState)
		}
	}

	def dispatch computeTypes(TUDeleteObjectExpression deleteExpr, ITypeComputationState state) {
		state.acceptActualType(state.getPrimitiveVoid);
		state.withoutRootExpectation.computeTypes(deleteExpr.object);
	}

	def dispatch computeTypes(TUSendSignalExpression sendExpr, ITypeComputationState state) {
		state.acceptActualType(state.getPrimitiveVoid);
		val childState = state.withoutRootExpectation;

		childState.computeTypes(sendExpr.signal);
		childState.computeTypes(sendExpr.target);
	}
	
	def dispatch computeTypes(TULinkExpression linkExpr, ITypeComputationState state) {
		state.acceptActualType(state.getPrimitiveVoid);
		val childState = state.withoutRootExpectation;

		childState.computeTypes(linkExpr.leftObject);
		childState.computeTypes(linkExpr.rightObject);
	}

	/**
	 * Overrides the default implementation such that it doesn't delegate child
	 * expression types to their enclosing block expression. Instead, the type
	 * of the given block will be accepted as <code>void</code>.
	 */
	override dispatch computeTypes(XBlockExpression block, ITypeComputationState state) {
		val children = block.expressions;
		if (!children.isEmpty) {
			state.withinScope(block);
		}

		for (child : children) {
			val expressionState = state.withoutExpectation; // no expectation
			expressionState.computeTypes(child);
			if (child instanceof XVariableDeclaration) {
				addLocalToCurrentScope(child as XVariableDeclaration, state);
			}
		}

		for (expectation : state.expectations) {
			val expectedType = expectation.expectedType;
			if (expectedType != null) {
				expectation.acceptActualType(expectedType, ConformanceFlags.CHECKED_SUCCESS);
			} else {
				expectation.acceptActualType(
					expectation.referenceOwner.newAnyTypeReference,
					ConformanceFlags.UNCHECKED
				);
			}
		}
	}

}
