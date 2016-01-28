package hu.elte.txtuml.xtxtuml.typesystem

import org.eclipse.xtext.xbase.annotations.typesystem.XbaseWithAnnotationsTypeComputer
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputationState
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.typesystem.conformance.ConformanceFlags
import org.eclipse.xtext.xbase.XVariableDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.xbase.typesystem.references.ParameterizedTypeReference
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference
import java.util.ArrayList
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import java.util.HashSet

class XtxtUMLTypeComputer extends XbaseWithAnnotationsTypeComputer {

	@Inject extension IJvmModelAssociations;

	def dispatch void computeTypes(RAlfAssocNavExpression navExpr, ITypeComputationState state) {
		// expectations for childs are enabled by default,
		// see val foo = if (bar) foobar else baz
		val childState = state.withoutRootExpectation;

		// left child
		childState.computeTypes(navExpr.left);
		
		// right child
		val rightChild = navExpr.right;
		// if the reference couldn't be resolved
		if (rightChild == null || rightChild.name == null) {
			return;
		}

		val assocEndType = rightChild.endClass.getPrimaryJvmElement as JvmType;
		val rightTypeRef = state.referenceOwner.toLightweightTypeReference(assocEndType);
		val collectionOfRightTypesRef = hu.elte.txtuml.api.model.Collection
			.getTypeForName(state).rawTypeReference;
			
		(collectionOfRightTypesRef as ParameterizedTypeReference).addTypeArgument(rightTypeRef);
		state.acceptActualType(collectionOfRightTypesRef);
	}

	def dispatch computeTypes(RAlfSignalAccessExpression sigExpr, ITypeComputationState state) {
		var container = sigExpr.eContainer;
		while (
			container != null &&
			!(container instanceof TUEntryOrExitActivity) &&
			!(container instanceof TUTransition)
		) {
			container = container.eContainer;
		}
		
		var visitedStates = new HashSet<TUState>();
		var type = switch (container) {
			TUEntryOrExitActivity
				: if (container.eContainer instanceof TUState) {
					getCommonSignalSuperType(
						container.eContainer as TUState, state,
						container.entry, visitedStates
					)
				} else {
					getTypeForName(Signal, state)
				}
				
			TUTransition
				: getCommonSignalSuperType(container, state, visitedStates)
			
			default
				: getTypeForName(Signal, state)
		}
		
		state.acceptActualType(type);
	}
	
	def private LightweightTypeReference getCommonSignalSuperType(
		TUTransition trans,
		ITypeComputationState cState,
		HashSet<TUState> visitedStates
	) {
		var trigger = (trans.members.findFirst[
			it instanceof TUTransitionTrigger
		] as TUTransitionTrigger)?.trigger;
		
		if (trigger != null) {
			val jvmSignal = trigger.getPrimaryJvmElement as JvmType;
			return if (jvmSignal != null) {
				cState.referenceOwner.toLightweightTypeReference(jvmSignal) 
			} else {
				getTypeForName(Signal, cState)
			}
		}
		
		var from = (trans.members.findFirst[
			it instanceof TUTransitionVertex && (it as TUTransitionVertex).from
		] as TUTransitionVertex)?.vertex;
	
		if (from != null && from.type == TUStateType.CHOICE) {
			return getCommonSignalSuperType(from, cState, true, visitedStates);
		}
	
		return getTypeForName(Signal, cState);
	}
	
	def private LightweightTypeReference getCommonSignalSuperType(
		TUState state,
		ITypeComputationState cState,
		boolean toState,
		HashSet<TUState> visitedStates
	) {
		if (!visitedStates.add(state)) {
			return getTypeForName(Signal, cState);
		}
		
		val siblingsAndSelf = switch (c : state.eContainer) {
			TUState : c.members
			TUClass : c.members
		}
		
		var signalCandidates = new ArrayList<LightweightTypeReference>();
		for (siblingOrSelf : siblingsAndSelf) {
			if (siblingOrSelf instanceof TUTransition &&
				((siblingOrSelf as TUTransition).members.findFirst[
					it instanceof TUTransitionVertex && toState != (it as TUTransitionVertex).from
				] as TUTransitionVertex)?.vertex == state
			) {
				signalCandidates.add(
					getCommonSignalSuperType(
						siblingOrSelf as TUTransition, cState, visitedStates
					)
				);
			}
		}
		
		return if (!signalCandidates.empty) {
			getCommonSuperType(signalCandidates, cState)
		} else {
			getTypeForName(Signal, cState)
		}
	}

	def dispatch computeTypes(RAlfDeleteObjectExpression deleteExpr, ITypeComputationState state) {
		state.computeTypes(deleteExpr.object);
		state.acceptActualType(state.getPrimitiveVoid);
	}

	def dispatch computeTypes(RAlfSendSignalExpression sendExpr, ITypeComputationState state) {
		state.computeTypes(sendExpr.signal);
		state.computeTypes(sendExpr.target);
		state.acceptActualType(state.getPrimitiveVoid);
	}

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
