package hu.elte.txtuml.xtxtuml.tests.parser;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassOrStateMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUDeleteObjectExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUInterface
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSendSignalExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
import java.util.List
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference
import org.eclipse.xtext.xbase.XAbstractFeatureCall
import org.eclipse.xtext.xbase.XAssignment
import org.eclipse.xtext.xbase.XBasicForLoopExpression
import org.eclipse.xtext.xbase.XBinaryOperation
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XBooleanLiteral
import org.eclipse.xtext.xbase.XCasePart
import org.eclipse.xtext.xbase.XConstructorCall
import org.eclipse.xtext.xbase.XDoWhileExpression
import org.eclipse.xtext.xbase.XExpression
import org.eclipse.xtext.xbase.XFeatureCall
import org.eclipse.xtext.xbase.XForLoopExpression
import org.eclipse.xtext.xbase.XIfExpression
import org.eclipse.xtext.xbase.XMemberFeatureCall
import org.eclipse.xtext.xbase.XNullLiteral
import org.eclipse.xtext.xbase.XNumberLiteral
import org.eclipse.xtext.xbase.XPostfixOperation
import org.eclipse.xtext.xbase.XReturnExpression
import org.eclipse.xtext.xbase.XStringLiteral
import org.eclipse.xtext.xbase.XSwitchExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.XWhileExpression
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1
import org.eclipse.xtext.xtype.XImportDeclaration

import static org.junit.Assert.*

class XtxtUMLParserTestUtils {

	val public static OPERATOR_LESS_THAN = "org.eclipse.xtext.xbase.lib.IntegerExtensions.operator_lessThan(int,int)";
	val public static OPERATOR_PLUS_PLUS = "org.eclipse.xtext.xbase.lib.IntegerExtensions.operator_plusPlus(int)";
	val public static OPERATOR_MINUS_MINUS = "org.eclipse.xtext.xbase.lib.IntegerExtensions.operator_minusMinus(int)";

	// Structure

	def modelDeclaration(TUFile file, String name, String modelName) {
		assertTrue(file instanceof TUModelDeclaration);
		assertEquals(name, file.name);
		assertEquals(modelName, (file as TUModelDeclaration).modelName);
	}

	def file(TUFile file, String name, List<Procedure1<XImportDeclaration>> importChecks,
		List<Procedure1<TUModelElement>> modelElementChecks) {
		assertFalse(file instanceof TUModelDeclaration);
		assertEquals(name, file.name);

		file.importSection?.importDeclarations.performChecks(importChecks);
		file.elements.performChecks(modelElementChecks);
	}

	def importDeclaration(XImportDeclaration importDeclaration, boolean isStatic, String name, boolean isWildcard) {
		assertFalse(importDeclaration.extension);
		assertEquals(isStatic, importDeclaration.static);
		assertEquals(name, importDeclaration.importedName);
		assertEquals(isWildcard, importDeclaration.wildcard);
	}

	def execution(TUModelElement element, String name, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(element instanceof TUExecution);
		val execution = element as TUExecution;

		assertEquals(name, execution.name);
		(execution.body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def signal(TUModelElement element, String name, List<Procedure1<TUSignalAttribute>> attributeChecks) {
		assertTrue(element instanceof TUSignal);
		val signal = element as TUSignal;

		assertEquals(name, signal.name);
		signal.attributes.performChecks(attributeChecks);
	}

	def attribute(TUSignalAttribute attribute, TUVisibility visibility, String typeName, String name) {
		assertEquals(visibility, attribute.visibility);
		assertEquals(typeName, attribute.type.simpleName);
		assertEquals(name, attribute.name);
	}

	def class_(TUModelElement element, String name, String superName, List<Procedure1<TUClassMember>> memberChecks) {
		assertTrue(element instanceof TUClass);
		val clazz = element as TUClass;

		assertEquals(name, clazz.name);
		assertEquals(superName, clazz.superClass?.name);
		clazz.members.performChecks(memberChecks);
	}

	def attribute(TUClassMember member, TUVisibility visibility, String typeName, String name) {
		assertTrue(member instanceof TUAttribute);
		val attribute = member as TUAttribute;

		assertEquals(visibility, attribute.prefix.visibility);
		assertEquals(typeName, attribute.prefix.type.simpleName);
		assertEquals(name, attribute.name);
	}

	def operation(TUClassMember member, TUVisibility visibility, String typeName, String name,
		List<Procedure1<JvmFormalParameter>> parameterChecks, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(member instanceof TUOperation);
		val operation = member as TUOperation;

		assertEquals(visibility, operation.prefix.visibility);
		assertEquals(typeName, operation.prefix.type.simpleName);
		assertEquals(name, operation.name);

		operation.parameters.performChecks(parameterChecks);
		(operation.body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def parameter(JvmFormalParameter parameter, String typeName, String name) {
		assertEquals(typeName, parameter.parameterType.simpleName);
		assertEquals(name, parameter.name);
	}

	def constructor(TUClassMember member, TUVisibility visibility, String name,
		List<Procedure1<JvmFormalParameter>> parameterChecks, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(member instanceof TUConstructor);
		val ctor = member as TUConstructor;

		assertEquals(visibility, ctor.visibility);
		assertEquals(name, ctor.name);

		ctor.parameters.performChecks(parameterChecks);
		(ctor.body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def state(TUClassOrStateMember member, TUStateType type, String name,
		List<Procedure1<TUStateMember>> memberChecks) {
		assertTrue(member instanceof TUState);
		val state = member as TUState;

		assertEquals(type, state.type);
		assertEquals(name, state.name);
		state.members.performChecks(memberChecks);
	}

	def entryOrExit(TUStateMember member, boolean isEntry, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(member instanceof TUEntryOrExitActivity);
		val activity = member as TUEntryOrExitActivity;

		assertEquals(isEntry, activity.entry);
		(activity.body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def transition(TUClassOrStateMember member, String name, List<Procedure1<TUTransitionMember>> memberChecks) {
		assertTrue(member instanceof TUTransition);
		val transition = member as TUTransition;

		assertEquals(name, transition.name);
		transition.members.performChecks(memberChecks);
	}

	def trigger(TUTransitionMember member, String triggerName) {
		assertTrue(member instanceof TUTransitionTrigger);
		assertEquals(triggerName, (member as TUTransitionTrigger).trigger.name);
	}

	def vertex(TUTransitionMember member, boolean isFrom, String vertexName) {
		assertTrue(member instanceof TUTransitionVertex);
		val vertex = member as TUTransitionVertex;

		assertEquals(isFrom, vertex.from);
		assertEquals(vertexName, vertex.vertex.name);
	}

	def effect(TUTransitionMember member, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(member instanceof TUTransitionEffect);
		((member as TUTransitionEffect).body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def guard(TUTransitionMember member, Procedure1<XExpression> guardCheck) {
		assertTrue(member instanceof TUTransitionGuard);
		val guard = member as TUTransitionGuard;

		assertEquals(guardCheck == null, guard.^else);
		if (guardCheck != null) {
			guardCheck.apply(guard.expression);
		}
	}

	def association(TUModelElement element, boolean isComposition, String name,
		List<Procedure1<TUAssociationEnd>> endChecks) {
		assertTrue(element instanceof TUAssociation);
		val association = element as TUAssociation;
		if (isComposition) {
			assertTrue(association instanceof TUComposition);
		}

		assertEquals(name, association.name);
		association.ends.performChecks(endChecks);
	}

	def associationEnd(TUAssociationEnd end, TUVisibility visibility, boolean isHidden,
		Procedure1<TUMultiplicity> multiplicityCheck, boolean isContainer, String className, String name) {
		assertEquals(visibility, end.visibility);
		assertEquals(isHidden, end.notNavigable);

		assertEquals(multiplicityCheck == null, end.multiplicity == null);
		if (multiplicityCheck != null) {
			multiplicityCheck.apply(end.multiplicity);
		}

		assertEquals(isContainer, end.container);
		assertEquals(className, end.endClass.name);
		assertEquals(name, end.name);
	}

	def any(TUMultiplicity multiplicity) {
		assertTrue(multiplicity.any);
	}

	def exact(TUMultiplicity multiplicity, int value) {
		assertFalse(multiplicity.any);
		assertFalse(multiplicity.upperSet);
		assertEquals(value, multiplicity.lower);
	}

	def interval(TUMultiplicity multiplicity, int lower, Integer upper) {
		assertFalse(multiplicity.any);
		assertTrue(multiplicity.upperSet);
		assertEquals(lower, multiplicity.lower);

		assertEquals(upper == null, multiplicity.upperInf);
		if (upper != null) {
			assertEquals(upper, multiplicity.upper);
		}
	}

	def port(TUTransitionMember member, String portName) {
		assertTrue(member instanceof TUTransitionPort);
		assertEquals(portName, (member as TUTransitionPort).port.name);
	}

	def interface_(TUModelElement element, String name, List<Procedure1<TUReception>> receptionChecks) {
		assertTrue(element instanceof TUInterface);
		val interface_ = element as TUInterface;

		assertEquals(name, interface_.name);
		interface_.receptions.performChecks(receptionChecks);
	}

	def reception(TUReception reception, String signalName) {
		assertEquals(signalName, reception.signal.name);
	}

	def port(TUClassMember member, boolean isBehavior, String name, List<Procedure1<TUPortMember>> memberChecks) {
		assertTrue(member instanceof TUPort);
		val port = member as TUPort;

		assertEquals(isBehavior, port.behavior);
		assertEquals(name, port.name);
		port.members.performChecks(memberChecks);
	}

	def portMember(TUPortMember member, boolean isRequired, String interfaceName) {
		assertEquals(isRequired, member.required);
		assertEquals(interfaceName, member.interface.name);
	}

	def connector(TUModelElement element, boolean isDelegation, String name,
		List<Procedure1<TUConnectorEnd>> endChecks) {
		assertTrue(element instanceof TUConnector);
		val connector = element as TUConnector;

		assertEquals(isDelegation, connector.delegation);
		assertEquals(name, connector.name);
		connector.ends.performChecks(endChecks);
	}

	def connectorEnd(TUConnectorEnd end, String roleName, String portName, String name) {
		assertEquals(roleName, end.role.name);
		assertEquals(portName, end.port.name);
		assertEquals(name, end.name);
	}

	// Expressions

	def sendSignal(XExpression expression, Procedure1<XExpression> signalCheck, Procedure1<XExpression> targetCheck) {
		assertTrue(expression instanceof TUSendSignalExpression);
		val send = expression as TUSendSignalExpression;

		signalCheck.apply(send.signal);
		targetCheck.apply(send.target);
	}

	def propertyAccess(XExpression expression, Procedure1<XExpression> leftCheck, String propertyName) {
		assertTrue(expression instanceof TUClassPropertyAccessExpression);
		val access = expression as TUClassPropertyAccessExpression;

		leftCheck.apply(access.left);
		assertEquals(propertyName, access.right.name);
	}

	def signalAccess(XExpression expression) {
		assertTrue(expression instanceof TUSignalAccessExpression);
	}

	def delete(XExpression expression, Procedure1<XExpression> targetCheck) {
		assertTrue(expression instanceof TUDeleteObjectExpression);
		targetCheck.apply((expression as TUDeleteObjectExpression).object);
	}

	def if_(XExpression expression, Procedure1<XExpression> predicateCheck,
		List<Procedure1<XExpression>> thenExpressionChecks, Procedure1<XExpression> elseCheck,
		List<Procedure1<XExpression>> elseExpressionChecks) {
		if (elseCheck != null && elseExpressionChecks != null) {
			throw new IllegalArgumentException("elseCheck, elseExpressionChecks");
		}

		assertTrue(expression instanceof XIfExpression);
		val if_ = expression as XIfExpression;

		predicateCheck.apply(if_.^if);
		(if_.then as XBlockExpression).expressions.performChecks(thenExpressionChecks);

		if (elseCheck != null) {
			assertNotNull(if_.^else);
			elseCheck.apply(if_.^else);
		} else if (elseExpressionChecks != null) {
			assertTrue(if_.^else instanceof XBlockExpression);
			(if_.^else as XBlockExpression).expressions.performChecks(elseExpressionChecks);
		} else {
			assertNull(if_.^else);
		}
	}

	def switch_(XExpression expression, Procedure1<XExpression> switchCheck, List<Procedure1<XCasePart>> caseChecks,
		List<Procedure1<XExpression>> defaultExpressionChecks) {
		assertTrue(expression instanceof XSwitchExpression);
		val switch_ = expression as XSwitchExpression;

		switchCheck.apply(switch_.^switch);
		switch_.cases.performChecks(caseChecks);
		(switch_.^default as XBlockExpression)?.expressions.performChecks(defaultExpressionChecks);
	}

	def case_(XCasePart casePart, Procedure1<XExpression> caseCheck, List<Procedure1<XExpression>> expressionChecks) {
		caseCheck.apply(casePart.^case);
		assertEquals(expressionChecks == null, casePart.fallThrough);
		(casePart.then as XBlockExpression)?.expressions.performChecks(expressionChecks);
	}

	def forEachLoop(XExpression expression, Procedure1<JvmFormalParameter> parameterCheck,
		Procedure1<XExpression> forExpressionCheck, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(expression instanceof XForLoopExpression);
		val forEach = expression as XForLoopExpression;

		parameterCheck.apply(forEach.declaredParam);
		forExpressionCheck.apply(forEach.forExpression);
		(forEach.eachExpression as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def forLoop(XExpression expression, Procedure1<XExpression> initCheck, Procedure1<XExpression> conditionCheck,
		Procedure1<XExpression> updateCheck, List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(expression instanceof XBasicForLoopExpression);
		val forLoop = expression as XBasicForLoopExpression;

		if (initCheck == null) {
			assertTrue(forLoop.initExpressions.empty);
		} else {
			assertEquals(1, forLoop.initExpressions.size);
			initCheck.apply(forLoop.initExpressions.head);
		}

		assertEquals(conditionCheck == null, forLoop.expression == null);
		if (conditionCheck != null) {
			conditionCheck.apply(forLoop.expression);
		}

		if (updateCheck == null) {
			assertTrue(forLoop.updateExpressions.empty);
		} else {
			assertEquals(1, forLoop.updateExpressions.size);
			updateCheck.apply(forLoop.updateExpressions.head);
		}

		(forLoop.eachExpression as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def whileLoop(XExpression expression, Procedure1<XExpression> predicateCheck,
		List<Procedure1<XExpression>> expressionChecks) {
		assertTrue(expression instanceof XWhileExpression);
		val whileLoop = expression as XWhileExpression;

		predicateCheck.apply(whileLoop.predicate);
		(whileLoop.body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def doWhileLoop(XExpression expression, List<Procedure1<XExpression>> expressionChecks,
		Procedure1<XExpression> predicateCheck) {
		assertTrue(expression instanceof XDoWhileExpression);
		val doWhile = expression as XDoWhileExpression;

		predicateCheck.apply(doWhile.predicate);
		(doWhile.body as XBlockExpression).expressions.performChecks(expressionChecks);
	}

	def constructorCall(XExpression expression, String constructorName, String typeArgumentName,
		List<Procedure1<XExpression>> argumentChecks) {
		assertTrue(expression instanceof XConstructorCall);
		val ctorCall = expression as XConstructorCall;

		assertEquals(constructorName, ctorCall.constructor.simpleName);
		assertTrue(ctorCall.explicitConstructorCall);

		if (typeArgumentName == null) {
			assertTrue(ctorCall.typeArguments.empty);
		} else {
			assertEquals(1, ctorCall.typeArguments.size);
			assertEquals(typeArgumentName, ctorCall.typeArguments.head.simpleName);
		}

		ctorCall.arguments.performChecks(argumentChecks);
	}

	def variableDeclaration(XExpression expression, String typeName, String typeArgumentName, String name,
		Procedure1<XExpression> rightCheck) {
		assertTrue(expression instanceof XVariableDeclaration);
		val decl = expression as XVariableDeclaration;

		assertTrue(decl.writeable);
		assertEquals(typeName, decl.type.type.simpleName);
		assertEquals(name, decl.name);

		val type = decl.type as JvmParameterizedTypeReference;
		if (typeArgumentName == null) {
			assertTrue(type.arguments.empty);
		} else {
			assertEquals(1, type.arguments.size);
			assertEquals(typeArgumentName, type.arguments.head.simpleName);
		}

		assertEquals(rightCheck == null, decl.right == null);
		if (rightCheck != null) {
			rightCheck.apply(decl.right);
		}
	}

	def variable(XExpression expression, String name) {
		assertTrue(expression instanceof XFeatureCall);
		val featureCall = expression as XFeatureCall;

		assertEquals(name, featureCall.feature.simpleName);
		assertTrue(featureCall.typeArguments.empty);
		assertFalse(featureCall.explicitOperationCall);
		assertTrue(featureCall.featureCallArguments.empty);
		assertNull(featureCall.actualReceiver);
	}

	def assignment(XExpression expression, String featureName, Procedure1<XExpression> valueCheck) {
		assertTrue(expression instanceof XAssignment);
		val assign = expression as XAssignment;

		assertEquals(featureName, assign.feature.simpleName);
		valueCheck.apply(assign.value);
	}

	def featureCall(XExpression expression, Procedure1<XExpression> targetCheck, String featureName,
		List<Procedure1<XExpression>> argumentChecks) {
		assertTrue(expression instanceof XAbstractFeatureCall);
		val abstractFeatureCall = expression as XAbstractFeatureCall;

		assertEquals(featureName, abstractFeatureCall.feature.simpleName);
		assertTrue(abstractFeatureCall.typeArguments.empty);

		// code duplication is inevitable here, due to the type hierarchy of XAbstractFeatureCall
		if (targetCheck == null) {
			assertTrue(expression instanceof XFeatureCall);
			val featureCall = expression as XFeatureCall;

			assertEquals(argumentChecks != null, featureCall.explicitOperationCall);
			if (argumentChecks == null) {
				assertTrue(featureCall.featureCallArguments.empty);
				assertNotNull(featureCall.actualReceiver); // otherwise it could be a variable
			} else {
				featureCall.featureCallArguments.performChecks(argumentChecks);
			}
		} else {
			assertTrue(expression instanceof XMemberFeatureCall);
			val memberFeatureCall = expression as XMemberFeatureCall;

			targetCheck.apply(memberFeatureCall.memberCallTarget);
			assertEquals(argumentChecks != null, memberFeatureCall.explicitOperationCall);
			if (argumentChecks == null) {
				assertTrue(memberFeatureCall.memberCallArguments.empty);
			} else {
				memberFeatureCall.memberCallArguments.performChecks(argumentChecks);
			}
		}
	}

	def this_(XExpression expression) {
		assertTrue(expression instanceof XFeatureCall);
		val thiz = expression as XFeatureCall;

		assertEquals("this", thiz.concreteSyntaxFeatureName);
		assertTrue(thiz.typeArguments.empty);
		assertFalse(thiz.explicitOperationCall);
		assertTrue(thiz.featureCallArguments.empty);
		assertNotNull(thiz.actualArguments);
	}

	def binaryOperation(XExpression expression, String id, Procedure1<XExpression> leftCheck,
		Procedure1<XExpression> rightCheck) {
		assertTrue(expression instanceof XBinaryOperation);
		val binaryOp = expression as XBinaryOperation;

		assertEquals(id, binaryOp.feature.identifier);
		leftCheck.apply(binaryOp.leftOperand);
		rightCheck.apply(binaryOp.rightOperand);
	}

	def postfixOperation(XExpression expression, String id, Procedure1<XExpression> targetCheck) {
		assertTrue(expression instanceof XPostfixOperation);
		val postfixOp = expression as XPostfixOperation;

		assertEquals(id, postfixOp.feature.identifier);
		targetCheck.apply(postfixOp.operand);
	}

	def return_(XExpression expression, Procedure1<XExpression> returnCheck) {
		assertTrue(expression instanceof XReturnExpression);
		returnCheck.apply((expression as XReturnExpression).expression);
	}

	def number(XExpression expression, int value) {
		assertTrue(expression instanceof XNumberLiteral);
		assertEquals(value.toString, (expression as XNumberLiteral).value);
	}

	def string(XExpression expression, String value) {
		assertTrue(expression instanceof XStringLiteral);
		assertEquals(value, (expression as XStringLiteral).value);
	}

	def bool(XExpression expression, boolean value) {
		assertTrue(expression instanceof XBooleanLiteral);
		assertEquals(value, (expression as XBooleanLiteral).isIsTrue);
	}

	def null_(XExpression expression) {
		assertTrue(expression instanceof XNullLiteral);
	}

	// Helpers

	def private <T> performChecks(List<T> elements, List<Procedure1<T>> checks) {
		assertEquals(checks == null, elements == null);
		if (checks != null) {
			assertEquals(checks.size, elements.size);
			val checkIt = checks.iterator;
			val elementIt = elements.iterator;

			while (checkIt.hasNext) {
				checkIt.next.apply(elementIt.next);
			}
		}
	}

}
