package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.*;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.eclipse.uml2.uml.VisibilityKind;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStructure extends UMLExportTestBase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ModelExportTestUtils.initialize();
	}

	@Test
	public void testAssociation() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.association");

		Class a = cls(model, "A");
		Class b = cls(model, "B");

		assoc(model, "Maybe_One_A_Many_B");
		assocEnd(a, b, "AEnd1", AggregationKind.NONE_LITERAL, 0, 1, true);
		assocEnd(b, a, "BEnd1", AggregationKind.NONE_LITERAL, 0, -1, true);

		assoc(model, "One_A_Some_B");
		assocEnd(a, b, "AEnd2", AggregationKind.NONE_LITERAL, 1, 1, true);
		assocEnd(b, a, "BEnd2", AggregationKind.NONE_LITERAL, 1, -1, true);

		assoc(model, "_3to4_A_0to100_B");
		assocEnd(a, b, "AEnd3", AggregationKind.NONE_LITERAL, 3, 4, true);
		assocEnd(b, a, "BEnd3", AggregationKind.NONE_LITERAL, 0, 100, true);

		Association as4 = assoc(model, "Maybe_One_Hidden_A_Many_Hidden_B");
		assocEnd(a, as4, "AEnd4", AggregationKind.NONE_LITERAL, 0, 1, false);
		assocEnd(b, as4, "BEnd4", AggregationKind.NONE_LITERAL, 0, -1, false);

		Association as5 = assoc(model, "One_Hidden_A_Some_Hidden_B");
		assocEnd(a, as5, "AEnd5", AggregationKind.NONE_LITERAL, 1, 1, false);
		assocEnd(b, as5, "BEnd5", AggregationKind.NONE_LITERAL, 1, -1, false);

		Association as6 = assoc(model, "_3to4_Hidden_A_0to100_Hidden_B");
		assocEnd(a, as6, "AEnd6", AggregationKind.NONE_LITERAL, 3, 4, false);
		assocEnd(b, as6, "BEnd6", AggregationKind.NONE_LITERAL, 0, 100, false);

		Association as7 = assoc(model, "One_Hidden_A_Many_B");
		assocEnd(a, as7, "AEnd7", AggregationKind.NONE_LITERAL, 1, 1, false);
		assocEnd(b, a, "BEnd7", AggregationKind.NONE_LITERAL, 0, -1, true);

		assoc(model, "Container_Many");
		assocEnd(a, b, "Cont_End", AggregationKind.NONE_LITERAL, 0, 1, true);
		assocEnd(b, a, "Contained_End_1", AggregationKind.COMPOSITE_LITERAL, 0, -1, true);

		Association as9 = assoc(model, "Hidden_Container_Many");
		assocEnd(a, as9, "Hidden_Cont_End", AggregationKind.NONE_LITERAL, 0, 1, false);
		assocEnd(b, a, "Contained_End_2", AggregationKind.COMPOSITE_LITERAL, 0, -1, true);

	}

	@Test
	public void testAttribute() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.attribute");

		Class testClass = cls(model, "TestClass");
		property(testClass, "byte_prim", "Integer");
		property(testClass, "byte_boxed", "Integer");
		property(testClass, "short_prim", "Integer");
		property(testClass, "short_boxed", "Integer");
		property(testClass, "int_prim", "Integer");
		property(testClass, "int_boxed", "Integer");
		property(testClass, "long_prim", "Integer");
		property(testClass, "long_boxed", "Integer");
		property(testClass, "bool_prim", "Boolean");
		property(testClass, "bool_boxed", "Boolean");
		property(testClass, "string", "String");
		property(testClass, "dt", "Reals");

		Property privateAttribute = property(testClass, "private_attribute", "Integer");
		assertEquals(VisibilityKind.PRIVATE_LITERAL, privateAttribute.getVisibility());
		Property protectedAttribute = property(testClass, "protected_attribute", "Integer");
		assertEquals(VisibilityKind.PROTECTED_LITERAL, protectedAttribute.getVisibility());
		Property publicAttribute = property(testClass, "public_attribute", "Integer");
		assertEquals(VisibilityKind.PUBLIC_LITERAL, publicAttribute.getVisibility());

		DataType reals = dataType(model, "Reals");
		property(reals, "double_prim", "Real");
		property(reals, "double_boxed", "Real");
		property(reals, "float_prim", "Real");
		property(reals, "float_boxed", "Real");
	}

	@Test
	public void testDefaultCtor() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "TestClass", "testDefaultCtor");
		checkDefaultCtor(body);
	}

	@Test
	public void testDefaultCtorAction() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "TestClass", "testDefaultCtorCreate");
		checkDefaultCtor(body);
	}

	@Test
	public void testParameteredCtor() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "TestClass", "testParameteredCtor");
		checkParameteredCtor(body);
	}

	@Test
	public void testParameteredCtorAction() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "TestClass", "testParameteredCtorCreate");
		checkParameteredCtor(body);
	}

	@Test
	public void testParameterlessCtor() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "TestClass", "testParameterlessCtor");
		checkParameterlessCtor(body);
	}

	@Test
	public void testParameterlessCtorAction() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "TestClass", "testParameterlessCtorCreate");
		checkParameterlessCtor(body);
	}

	@Test
	public void testGeneralization() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.generalization");
		Class abstractBase = cls(model, "AbstractBaseClass");
		Operation baseMethod = operation(abstractBase, "baseMethod");
		assertTrue(baseMethod.isAbstract());

		Class concreteBase = cls(model, "ConcreteBaseClass");
		checkSubclass(concreteBase, abstractBase);
		Operation baseMethodInConcBase = operation(concreteBase, "baseMethod");
		overrides(baseMethodInConcBase, baseMethod);
		assertFalse(baseMethodInConcBase.isAbstract());
		property(concreteBase, "baseField", "Integer");

		Class concreteSub = cls(model, "ConcreteSubclass");
		checkSubclass(concreteSub, concreteBase);

		Operation baseMethodInConcSub = operation(concreteSub, "baseMethod");
		Operation newMethod = operation(concreteSub, "newMethod");
		Activity activity = (Activity) newMethod.getMethods().get(0);
		SequenceNode actBody = (SequenceNode) activity.getNode("#body");
		SequenceNode callStmt = (SequenceNode) actBody.getNode("this.baseMethod();");
		CallOperationAction callExpr = (CallOperationAction) callStmt.getNode("this.baseMethod()");
		assertEquals(baseMethodInConcSub, callExpr.getOperation());
	}

	@Test
	public void testOperation() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.operation");
		Class cls = cls(model, "TestClass");
		Operation op1 = operation(cls, "op1");

		Parameter returnParam = op1.getOwnedParameters().get(0);
		assertEquals("return", returnParam.getName());
		assertEquals("Integer", returnParam.getType().getName());
		assertEquals(ParameterDirectionKind.RETURN_LITERAL, returnParam.getDirection());

		Parameter param1 = op1.getOwnedParameters().get(1);
		assertEquals("b", param1.getName());
		assertEquals("Boolean", param1.getType().getName());
		assertEquals(ParameterDirectionKind.IN_LITERAL, param1.getDirection());

		Parameter param2 = op1.getOwnedParameters().get(2);
		assertEquals("c", param2.getName());
		assertEquals("String", param2.getType().getName());
		assertEquals(ParameterDirectionKind.IN_LITERAL, param2.getDirection());

		Operation op2 = operation(cls, "op2");
		assertTrue(op2.getOwnedParameters().isEmpty());
		assertEquals(VisibilityKind.PUBLIC_LITERAL, op2.getVisibility());

		Operation op3 = operation(cls, "op3");
		assertEquals(VisibilityKind.PRIVATE_LITERAL, op3.getVisibility());
		Operation op4 = operation(cls, "op4");
		assertEquals(VisibilityKind.PROTECTED_LITERAL, op4.getVisibility());
	}

	@Test
	public void testPorts() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ports");
		Class cls = cls(model, "TestClass");
		Port behavPort = port(cls, "BehavPort");

		assertTrue(behavPort.isBehavior());
		assertEquals("Iface", getProvided(behavPort).getName());

		Port assemblyPort = port(cls, "AssemblyPort");
		assertFalse(assemblyPort.isBehavior());
		assertEquals("Iface", getProvided(assemblyPort).getName());

		// existance is checked
		port(cls, "MyInPort");
		port(cls, "MyOutPort");
	}

	@Test
	public void testSend() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.send");
		Class clsA = cls(model, "A");
		Property otherEnd = property(clsA, "B_end", "B");
		Class clsB = cls(model, "B");
		Property thisEnd = property(clsB, "A_end", "A");

		SequenceNode body = loadActionCode(model, "A", "test");
		SequenceNode sendStmt = node(body, 0, "send create Sig to select(a -> B_end);", SequenceNode.class);
		node(sendStmt, 0, "create Sig", SequenceNode.class);
		node(sendStmt, 1, "a", ReadVariableAction.class);
		ReadLinkAction readLink = node(sendStmt, 2, "a -> B_end", ReadLinkAction.class);
		node(sendStmt, 3, "select(a -> B_end)", CallOperationAction.class);
		node(sendStmt, 4, "send create Sig to select(a -> B_end)", SendObjectAction.class);
		assertEquals(thisEnd, readLink.getEndData().get(0).getEnd());
		assertNotNull(readLink.getEndData().get(0).getValue());
		assertEquals(otherEnd, readLink.getEndData().get(1).getEnd());
		assertEquals(null, readLink.getEndData().get(1).getValue());
	}

	@Test
	public void testSignal() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.signal");
		Signal sig = signal(model, "Sig");
		SignalEvent sigEvent = signalEvent(model, "Sig");
		assertEquals(sig, sigEvent.getSignal());
		Class sigFactory = cls(model, "#Sig_factory");
		Operation sigCtor = operation(sigFactory, "Sig");
		assertEquals(4, sigCtor.getOwnedParameters().size());

		SequenceNode body = loadActionCode(model, "A", "test");
		SequenceNode createNode = node(body, 0, "create Sig", SequenceNode.class);
		CreateObjectAction initiateNode = node(createNode, 0, "instantiate Sig", CreateObjectAction.class);
		assertEquals(sig, initiateNode.getClassifier());
		node(createNode, 1, "#temp=instantiate Sig", AddVariableValueAction.class);
		node(createNode, 2, "1", ValueSpecificationAction.class);
		node(createNode, 3, "true", ValueSpecificationAction.class);
		node(createNode, 4, "\"test\"", ValueSpecificationAction.class);
		node(createNode, 5, "#temp", ReadVariableAction.class);
		CallOperationAction ctorCall = node(createNode, 6, "Sig(Sig p0, Integer p1, Boolean p2, String p3)",
				CallOperationAction.class);
		assertEquals(sigCtor, ctorCall.getOperation());
		node(createNode, 7, "#temp", ReadVariableAction.class);
	}

	@Test
	public void testSM() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.sm");
		Class cls = cls(model, "TestClass");
		SignalEvent sig = signalEvent(model, "TestSignal");
		Region reg = region(cls);
		Pseudostate init = pseudoState(reg, "Init", PseudostateKind.INITIAL_LITERAL);
		State s1 = state(reg, "S1");
		transition(reg, init, s1, null);
		transition(reg, s1, s1, sig);
	}

}
