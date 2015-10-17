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
		if (rightChild.name == null) {
			return;
		}

		val assocEndType = rightChild.endClass.getPrimaryJvmElement as JvmType;
		val rightTypeRef = state.referenceOwner.toLightweightTypeReference(assocEndType);
		val collectionOfRightTypesRef = hu.elte.txtuml.api.model.Collection
			.getTypeForName(state).rawTypeReference;
			
		(collectionOfRightTypesRef as ParameterizedTypeReference).addTypeArgument(rightTypeRef);
		state.acceptActualType(collectionOfRightTypesRef);
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