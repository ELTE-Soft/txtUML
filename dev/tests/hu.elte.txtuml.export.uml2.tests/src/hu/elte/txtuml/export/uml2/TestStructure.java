package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ExpansionNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.ReadLinkAction;
import org.eclipse.uml2.uml.ReadSelfAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.StartObjectBehaviorAction;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.eclipse.uml2.uml.Vertex;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStructure {

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
		assocEnd(a, b, "Cont_End", AggregationKind.COMPOSITE_LITERAL, 0, 1, true);
		assocEnd(b, a, "Contained_End_1", AggregationKind.NONE_LITERAL, 0, -1, true);

		Association as9 = assoc(model, "Hidden_Container_Many");
		assocEnd(a, as9, "Hidden_Cont_End", AggregationKind.COMPOSITE_LITERAL, 0, 1, false);
		assocEnd(b, a, "Contained_End_2", AggregationKind.NONE_LITERAL, 0, -1, true);

	}

	private Model model(String name) throws Exception {
		Model model = ModelExportTestUtils.export(name);
		assertNotNull(model);
		return model;
	}

	private Class cls(Model model, String name) {
		Class cls = (Class) model.getMember(name);
		assertNotNull(cls);
		return cls;
	}

	private Association assoc(org.eclipse.uml2.uml.Model model, String name) {
		Association assoc = (Association) model.getMember(name);
		assertNotNull(assoc);
		return assoc;
	}

	private Property assocEnd(Class a, Classifier ab, String name, AggregationKind kind, int lowerBound, int upperBound,
			boolean isNavigable) {
		Property end;
		if (ab instanceof Association) {
			end = ((Association) ab).getOwnedEnd(name, null);
		} else {
			end = ((Class) ab).getOwnedAttribute(name, null);
		}
		assertEquals(a, end.getType());
		assertEquals(kind, end.getAggregation());
		assertEquals(lowerBound, end.getLower());
		assertEquals(upperBound, end.getUpper());
		assertEquals(isNavigable, end.isNavigable());
		return end;
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

		DataType reals = dataType(model, "Reals");
		property(reals, "double_prim", "Real");
		property(reals, "double_boxed", "Real");
		property(reals, "float_prim", "Real");
		property(reals, "float_boxed", "Real");
	}

	private DataType dataType(Model model, String name) {
		DataType dataType = (DataType) model.getMember(name);
		assertNotNull(dataType);
		return dataType;
	}

	private Property property(Classifier cls, String name, String typeName) {
		Property attribute = cls.getAttribute(name, null);
		assertNotNull(attribute);
		assertEquals(typeName, attribute.getType().getName());
		assertEquals(1, attribute.getLower());
		assertEquals(1, attribute.getUpper());
		assertEquals(true, attribute.isNavigable());
		return attribute;
	}

	private Operation operation(Classifier cls, String name) {
		Operation operation = cls.getOperation(name, null, null);
		assertNotNull(operation);
		return operation;
	}

	@Test
	public void testPrefixOperation() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.compound_ops");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode preStmtNode = (SequenceNode) body.getNode("this.fld=++this.fld;");
		SequenceNode preExprNode = (SequenceNode) preStmtNode.getNode("this.fld=++this.fld");
		node(preExprNode, 0, "this", ReadSelfAction.class);
		node(preExprNode, 1, "this.fld", ReadStructuralFeatureAction.class);
		node(preExprNode, 2, "1", ValueSpecificationAction.class);
		node(preExprNode, 3, "++this.fld", CallOperationAction.class);
		node(preExprNode, 4, "this.fld=++this.fld", AddStructuralFeatureValueAction.class);
		node(preExprNode, 5, "this.fld", ReadStructuralFeatureAction.class);
	}

	@Test
	public void testPostfixOperation() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.compound_ops");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode postStmtNode = (SequenceNode) body.getNode("this.fld=this.fld++;");
		SequenceNode postExprNode = (SequenceNode) postStmtNode.getNode("this.fld=this.fld++");
		node(postExprNode, 0, "this", ReadSelfAction.class);
		node(postExprNode, 1, "this.fld", ReadStructuralFeatureAction.class);
		node(postExprNode, 2, "#temp=this.fld", AddVariableValueAction.class);
		node(postExprNode, 3, "this.fld", ReadStructuralFeatureAction.class);
		node(postExprNode, 4, "1", ValueSpecificationAction.class);
		node(postExprNode, 5, "this.fld++", CallOperationAction.class);
		node(postExprNode, 6, "this.fld=this.fld++", AddStructuralFeatureValueAction.class);
		node(postExprNode, 7, "#temp", ReadVariableAction.class);
	}

	@Test
	public void testAssignment() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.compound_ops");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode assignStmtNode = (SequenceNode) body.getNode("this.fld=10;");
		SequenceNode assignExprNode = (SequenceNode) assignStmtNode.getNode("this.fld=10");
		node(assignExprNode, 0, "this", ReadSelfAction.class);
		node(assignExprNode, 1, "this.fld", ReadStructuralFeatureAction.class);
		node(assignExprNode, 2, "10", ValueSpecificationAction.class);
		node(assignExprNode, 3, "this.fld=10", AddStructuralFeatureValueAction.class);
		node(assignExprNode, 4, "this.fld", ReadStructuralFeatureAction.class);
	}

	@Test
	public void testCompoundAssignOperation() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.compound_ops");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode compoundStmtNode = (SequenceNode) body.getNode("this.fld=this.fld+10;");
		SequenceNode compoundExprNode = (SequenceNode) compoundStmtNode.getNode("this.fld=this.fld+10");
		node(compoundExprNode, 0, "this", ReadSelfAction.class);
		node(compoundExprNode, 1, "this.fld", ReadStructuralFeatureAction.class);
		node(compoundExprNode, 2, "10", ValueSpecificationAction.class);
		node(compoundExprNode, 3, "this.fld+10", CallOperationAction.class);
		node(compoundExprNode, 4, "this.fld=this.fld+10", AddStructuralFeatureValueAction.class);
		node(compoundExprNode, 5, "this.fld", ReadStructuralFeatureAction.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T node(StructuredActivityNode parent, int index, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getNodes().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@Test
	public void testCreateActionBehavior() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.create_and_destroy");
		SequenceNode body = loadActionCode(model, "TestClass", "testCreate");
		SequenceNode createNode = (SequenceNode) body.getNode("create TestClass");
		node(createNode, 0, "instantiate TestClass", CreateObjectAction.class);
		node(createNode, 1, "#temp=instantiate TestClass", AddVariableValueAction.class);
		node(createNode, 2, "#temp", ReadVariableAction.class);
		node(createNode, 3, "1", ValueSpecificationAction.class);
		node(createNode, 4, "2", ValueSpecificationAction.class);
		node(createNode, 5, "#temp.TestClass(Integer p0, Integer p1)", CallOperationAction.class);
		node(createNode, 6, "#temp", ReadVariableAction.class);
	}

	@Test
	public void testDestroyActionBehavior() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.create_and_destroy");
		SequenceNode body = loadActionCode(model, "TestClass", "testDestroy");
		SequenceNode createNode = (SequenceNode) body.getNode("delete cls;");
		node(createNode, 0, "cls", ReadVariableAction.class);
		node(createNode, 1, "delete cls", DestroyObjectAction.class);
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

	private void checkDefaultCtor(SequenceNode body) {
		SequenceNode createNode = (SequenceNode) body.getNode("create DefaultConstructible;");
		SequenceNode createExprNode = (SequenceNode) createNode.getNode("create DefaultConstructible");
		node(createExprNode, 0, "instantiate DefaultConstructible", CreateObjectAction.class);
		node(createExprNode, 1, "#temp=instantiate DefaultConstructible", AddVariableValueAction.class);
		node(createExprNode, 2, "#temp", ReadVariableAction.class);
		node(createExprNode, 3, "#temp.DefaultConstructible()", CallOperationAction.class);
		node(createExprNode, 4, "#temp", ReadVariableAction.class);
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

	private void checkParameteredCtor(SequenceNode body) {
		SequenceNode createNode = (SequenceNode) body.getNode("create ClassWithCtors;");
		SequenceNode createExprNode = (SequenceNode) createNode.getNode("create ClassWithCtors");
		node(createExprNode, 0, "instantiate ClassWithCtors", CreateObjectAction.class);
		node(createExprNode, 1, "#temp=instantiate ClassWithCtors", AddVariableValueAction.class);
		node(createExprNode, 2, "#temp", ReadVariableAction.class);
		node(createExprNode, 3, "1", ValueSpecificationAction.class);
		node(createExprNode, 4, "#temp.ClassWithCtors(Integer p0)", CallOperationAction.class);
		node(createExprNode, 5, "#temp", ReadVariableAction.class);
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

	private void checkParameterlessCtor(SequenceNode body) {
		SequenceNode createNode = (SequenceNode) body.getNode("create ClassWithCtors;");
		SequenceNode createExprNode = (SequenceNode) createNode.getNode("create ClassWithCtors");
		node(createExprNode, 0, "instantiate ClassWithCtors", CreateObjectAction.class);
		node(createExprNode, 1, "#temp=instantiate ClassWithCtors", AddVariableValueAction.class);
		node(createExprNode, 2, "#temp", ReadVariableAction.class);
		node(createExprNode, 3, "#temp.ClassWithCtors()", CallOperationAction.class);
		node(createExprNode, 4, "#temp", ReadVariableAction.class);
	}

	@Test
	public void testDoWhileLoop() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.do_while_control");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		LoopNode loopNode = (LoopNode) node(body, 0, "do { ... } while (x>0)", LoopNode.class);
		SequenceNode bodyNode = loopBody(loopNode, 0, null, SequenceNode.class);
		node(bodyNode, 0, "x=--x;", SequenceNode.class);
		loopCond(loopNode, "x>0", CallOperationAction.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T loopBody(LoopNode parent, int index, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getBodyParts().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@SuppressWarnings("unchecked")
	private <T> T loopCond(LoopNode parent, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getTests().get(0);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@Test
	public void testForLoop() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.for_control");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		LoopNode loopNode = node(body, 0, "for (i<limit) { ... }", LoopNode.class);
		SequenceNode bodyNode = loopBody(loopNode, 0, null, SequenceNode.class);
		node(bodyNode, 0, "this.sum=this.sum+i;", SequenceNode.class);
		SequenceNode increment = loopBody(loopNode, 1, "update", SequenceNode.class);
		node(increment, 0, "i=++i", SequenceNode.class);
		loopCond(loopNode, "i<limit", SequenceNode.class);
		SequenceNode setup = loopSetup(loopNode, "setup", SequenceNode.class);
		node(setup, 0, "0", ValueSpecificationAction.class);
		node(setup, 1, "i=0", AddVariableValueAction.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T loopSetup(LoopNode parent, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getSetupParts().get(0);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@Test
	public void testForEachLoop() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.foreach_control");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		ExpansionRegion exp = node(body, 0, "foreach (coll)", ExpansionRegion.class);
		inputElement(exp, 0, "coll_expansion");
		node(exp, 0, "coll", ReadVariableAction.class);
		SequenceNode inner = node(exp, 1, null, SequenceNode.class);
		node(inner, 0, "this.sum=this.sum+i;", SequenceNode.class);
	}

	private ExpansionNode inputElement(ExpansionRegion parent, int index, String name) {
		ExpansionNode node = parent.getInputElements().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		return node;
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

	private void checkSubclass(Class sub, Class generic) {
		assertNotNull(sub.getGeneralization(generic));
	}

	private void overrides(Operation overrider, Operation overridden) {
		assertTrue(overrider.getRedefinedOperations().contains(overridden));
	}

	@Test
	public void testIf() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.if_control");
		SequenceNode body = loadActionCode(model, "TestClass", "testIf");
		SequenceNode ifNode = node(body, 0, "if (test)", SequenceNode.class);
		node(ifNode, 0, "test", ReadVariableAction.class);
		node(ifNode, 1, "#if_cond=test", AddVariableValueAction.class);
		ConditionalNode condNode = node(ifNode, 2, null, ConditionalNode.class);
		clauseTest(condNode, 0, "#if_cond", ReadVariableAction.class);
		SequenceNode clauseBody = clauseBody(condNode, 0, null, SequenceNode.class);
		node(clauseBody, 0, "Action.log(\"then\");", SequenceNode.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T clauseTest(ConditionalNode condNode, int index, String name, java.lang.Class<T> type) {
		Clause clause = condNode.getClauses().get(index);
		assertNotNull(clause);
		ExecutableNode testNode = clause.getTests().get(0);
		assertNotNull(testNode);
		assertEquals(name, testNode.getName());
		assertTrue("Node is not a " + type.getName(), type.isInstance(testNode));
		return (T) testNode;
	}

	@SuppressWarnings("unchecked")
	private <T> T clauseBody(ConditionalNode condNode, int index, String name, java.lang.Class<T> type) {
		Clause clause = condNode.getClauses().get(index);
		assertNotNull(clause);
		ExecutableNode testNode = clause.getBodies().get(0);
		assertNotNull(testNode);
		assertEquals(name, testNode.getName());
		assertTrue("Node is not a " + type.getName(), type.isInstance(testNode));
		return (T) testNode;
	}

	@Test
	public void testIfElse() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.if_control");
		SequenceNode body = loadActionCode(model, "TestClass", "testIfElse");
		SequenceNode ifNode = node(body, 0, "if (test)", SequenceNode.class);
		node(ifNode, 0, "test", ReadVariableAction.class);
		node(ifNode, 1, "#if_cond=test", AddVariableValueAction.class);
		ConditionalNode condNode = node(ifNode, 2, null, ConditionalNode.class);
		clauseTest(condNode, 0, "#if_cond", ReadVariableAction.class);
		SequenceNode clauseBody = clauseBody(condNode, 0, null, SequenceNode.class);
		node(clauseBody, 0, "Action.log(\"then\");", SequenceNode.class);
		clauseTest(condNode, 1, "!#if_cond", CallOperationAction.class);
		SequenceNode elseBody = clauseBody(condNode, 1, null, SequenceNode.class);
		node(elseBody, 0, "Action.log(\"else\");", SequenceNode.class);
	}

	@Test
	public void testInlineIf() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.if_control");
		SequenceNode body = loadActionCode(model, "TestClass", "testInlineIf");
		SequenceNode ifNode = node(body, 0, "if (test)", SequenceNode.class);
		node(ifNode, 0, "test", ReadVariableAction.class);
		node(ifNode, 1, "#if_cond=test", AddVariableValueAction.class);
		ConditionalNode condNode = node(ifNode, 2, null, ConditionalNode.class);
		clauseTest(condNode, 0, "#if_cond", ReadVariableAction.class);
		clauseBody(condNode, 0, "Action.log(\"then\");", SequenceNode.class);
	}

	@Test
	public void testInlineIfElse() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.if_control");
		SequenceNode body = loadActionCode(model, "TestClass", "testInlineIfElse");
		SequenceNode ifNode = node(body, 0, "if (test)", SequenceNode.class);
		node(ifNode, 0, "test", ReadVariableAction.class);
		node(ifNode, 1, "#if_cond=test", AddVariableValueAction.class);
		ConditionalNode condNode = node(ifNode, 2, null, ConditionalNode.class);
		clauseTest(condNode, 0, "#if_cond", ReadVariableAction.class);
		clauseBody(condNode, 0, "Action.log(\"then\");", SequenceNode.class);
		clauseTest(condNode, 1, "!#if_cond", CallOperationAction.class);
		clauseBody(condNode, 1, "Action.log(\"else\");", SequenceNode.class);
	}

	@Test
	public void testLink() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.link_and_unlink");
		Class clsA = cls(model, "A");
		Property otherEnd = property(clsA, "OtherEnd", "B");
		Class clsB = cls(model, "B");
		Property thisEnd = property(clsB, "ThisEnd", "A");

		SequenceNode body = loadActionCode(model, "A", "testLink");
		SequenceNode linkStmt = node(body, 0, "link inst1 to inst2;", SequenceNode.class);
		node(linkStmt, 0, "inst1", ReadVariableAction.class);
		node(linkStmt, 1, "inst2", ReadVariableAction.class);
		CreateLinkAction linkNode = node(linkStmt, 2, "link inst1 to inst2", CreateLinkAction.class);
		assertEquals(thisEnd, linkNode.getEndData().get(0).getEnd());
		assertNotNull(linkNode.getEndData().get(0).getValue());
		assertEquals(otherEnd, linkNode.getEndData().get(1).getEnd());
		assertNotNull(linkNode.getEndData().get(1).getValue());
	}

	@Test
	public void testUnlink() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.link_and_unlink");
		Class clsA = cls(model, "A");
		Property otherEnd = property(clsA, "OtherEnd", "B");
		Class clsB = cls(model, "B");
		Property thisEnd = property(clsB, "ThisEnd", "A");

		SequenceNode body = loadActionCode(model, "A", "testUnlink");
		SequenceNode linkStmt = node(body, 0, "unlink inst1 from inst2;", SequenceNode.class);
		node(linkStmt, 0, "inst1", ReadVariableAction.class);
		node(linkStmt, 1, "inst2", ReadVariableAction.class);
		DestroyLinkAction linkNode = node(linkStmt, 2, "unlink inst1 from inst2", DestroyLinkAction.class);
		assertEquals(thisEnd, linkNode.getEndData().get(0).getEnd());
		assertNotNull(linkNode.getEndData().get(0).getValue());
		assertEquals(otherEnd, linkNode.getEndData().get(1).getEnd());
		assertNotNull(linkNode.getEndData().get(1).getValue());
	}

	@Test
	public void testLog() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.log");

		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode logStmt = node(body, 0, "Action.log(\"Hello\");", SequenceNode.class);
		node(logStmt, 0, "\"Hello\"", ValueSpecificationAction.class);
		node(logStmt, 1, "Action.log(\"Hello\")", CallOperationAction.class);
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

		SequenceNode body = loadActionCode(model, "A", "test");
		SequenceNode createNode = node(body, 0, "create Sig", SequenceNode.class);
		CreateObjectAction initiateNode = node(createNode, 0, "instantiate Sig", CreateObjectAction.class);
		assertEquals(sig, initiateNode.getClassifier());
		node(createNode, 1, "#temp=instantiate Sig", AddVariableValueAction.class);
		node(createNode, 2, "#temp", ReadVariableAction.class);
		node(createNode, 3, "1", ValueSpecificationAction.class);
		node(createNode, 4, "true", ValueSpecificationAction.class);
		node(createNode, 5, "\"test\"", ValueSpecificationAction.class);
		CallOperationAction ctorCall = node(createNode, 6, "#temp.Sig(Integer p0, Boolean p1, String p2)", CallOperationAction.class);
		assertEquals(sigCtor, ctorCall.getOperation());
		node(createNode, 7, "#temp", ReadVariableAction.class);
	}

	private Signal signal(Model model, String name) {
		Optional<NamedElement> sig = (Optional<NamedElement>) model.getMembers().stream()
				.filter(m -> m.getName().equals(name) && m instanceof Signal).findFirst();
		assertTrue(sig.isPresent());
		return (Signal) sig.get();
	}

	private SignalEvent signalEvent(Model model, String name) {
		Optional<NamedElement> sig = (Optional<NamedElement>) model.getMembers().stream()
				.filter(m -> m.getName().equals(name) && m instanceof SignalEvent).findFirst();
		assertTrue(sig.isPresent());
		return (SignalEvent) sig.get();
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

	private Transition transition(Region reg, Vertex v1, Vertex v2, Event event) {
		Optional<Transition> trans = reg.getTransitions().stream().filter(s -> s.getSource().equals(v1) && s.getTarget().equals(v2)).findFirst();
		if (event != null) {
			assertEquals(event, trans.get().getTriggers().get(0).getEvent());
		}
		return trans.get();
	}

	private Region region(Class cls) {
		StateMachine sm = (StateMachine) cls.getClassifierBehavior();
		Region reg = sm.getRegions().get(0);
		assertNotNull(reg);
		return reg;
	}
	
	private Pseudostate pseudoState(Region reg, String name, PseudostateKind kind) {
		Optional<Vertex> vertex = reg.getSubvertices().stream().filter(s -> s.getName().equals(name)).findFirst();
		assertTrue(vertex.isPresent());
		assertTrue("The vertex is not a PseudoState", vertex.get() instanceof Pseudostate);
		assertEquals(kind, ((Pseudostate) vertex.get()).getKind());
		return (Pseudostate) vertex.get();
	}
	
	private State state(Region reg, String name) {
		Optional<Vertex> vertex = reg.getSubvertices().stream().filter(s -> s.getName().equals(name)).findFirst();
		assertTrue(vertex.isPresent());
		assertTrue("The vertex is not a State", vertex.get() instanceof State);
		return (State) vertex.get();
	}
	
	@Test
	public void testSMActions() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.sm_actions");
		Class cls = cls(model, "TestClass");
		SignalEvent sig = signalEvent(model, "TestSignal");
		Region reg = region(cls);
		Pseudostate init = pseudoState(reg, "Init", PseudostateKind.INITIAL_LITERAL);
		State s1 = state(reg, "S1");
		SequenceNode entryBody = getActivityBody((Activity) s1.getEntry());
		node(entryBody, 0, "Action.log(\"entry S1\");", SequenceNode.class);
		SequenceNode exitBody = getActivityBody((Activity) s1.getExit());
		node(exitBody, 0, "Action.log(\"exit S1\");", SequenceNode.class);
		assertNotNull(s1.getEntry());
		Transition tr1 = transition(reg, init, s1, null);
		SequenceNode effectBody = getActivityBody((Activity) tr1.getEffect());
		node(effectBody, 0, "Action.log(\"Init -> S1\");", SequenceNode.class);
		Transition tr2 = transition(reg, s1, s1, sig);
		SequenceNode guardBody = getActivityBody(getGuardActivity(tr2));
		node(guardBody, 0, "return false", SequenceNode.class);
	}


	private Activity getGuardActivity(Transition tr2) {
		Constraint guard = tr2.getGuard();
		OpaqueExpression specification = (OpaqueExpression) guard.getSpecification();
		Activity activity = (Activity) specification.getBehavior();
		assertNotNull(activity);
		return activity;
	}

	private SequenceNode loadActionCode(Model model, String className, String operationName) throws Exception {
		Class cls = cls(model, className);
		Operation op = operation(cls, operationName);
		Activity behav = (Activity) op.getMethod(operationName);
		return getActivityBody(behav);
	}
	
	@Test
	public void testStart() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.start");

		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode logStmt = node(body, 0, "start inst;", SequenceNode.class);
		node(logStmt, 0, "inst", ReadVariableAction.class);
		node(logStmt, 1, "start inst", StartObjectBehaviorAction.class);
	}
	
	@Test
	public void testWhileLoop() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.while_control");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		LoopNode loopNode = node(body, 0, "while (i>0) { ... }", LoopNode.class);
		SequenceNode bodyNode = loopBody(loopNode, 0, null, SequenceNode.class);
		node(bodyNode, 0, "i=--i;", SequenceNode.class);
		loopCond(loopNode, "i>0", CallOperationAction.class);
	}

	private SequenceNode getActivityBody(Activity behav) {
		assertNotNull(behav);
		SequenceNode bodyNode = (SequenceNode) behav.getOwnedNode("#body");
		assertNotNull(bodyNode);
		return bodyNode;
	}
	
	

}
