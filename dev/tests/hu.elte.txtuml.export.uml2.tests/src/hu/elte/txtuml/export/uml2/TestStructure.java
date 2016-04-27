package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ExpansionNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadSelfAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.ValueSpecificationAction;
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
		LoopNode loopNode = (LoopNode) node(body, 0, "for (i<limit) { ... }", LoopNode.class);
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
		ExpansionRegion exp = (ExpansionRegion) node(body, 0, "foreach (coll)", ExpansionRegion.class);
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
	
	// @Test
	// public void testOperation() throws Exception {
	// org.eclipse.uml2.uml.Model model = ModelExportTestUtils
	// .export("hu.elte.txtuml.export.uml2.tests.models.operation");
	// assertNotNull(model);
	// Class testClass = (Class) model.getMember("TestClass");
	// assertNotNull(testClass);
	// EList<Operation> ops = testClass.getOwnedOperations();
	// assertEquals(2, ops.size());
	// Operation op1 = testClass.getOperation("op1", null, null);
	// assertNotNull(op1);
	// EList<Parameter> params = op1.getOwnedParameters();
	// assertEquals(3, params.size());
	// for (Parameter param : params) {
	// if (param.getType().getName().equals("Boolean")) {
	// assertEquals("b", param.getName());
	// } else if (param.getType().getName().equals("String")) {
	// assertEquals("c", param.getName());
	// } else if (param.getType().getName().equals("Integer")) {
	// assertEquals(param.getDirection(),
	// ParameterDirectionKind.RETURN_LITERAL);
	// }
	// }
	// Operation op2 = testClass.getOwnedOperation("op2", null, null);
	// params = op2.getOwnedParameters();
	// assertEquals(0, params.size());
	// }
	//
	// @Test
	// public void testSignal() throws Exception {
	// org.eclipse.uml2.uml.Model model = ModelExportTestUtils
	// .export("hu.elte.txtuml.export.uml2.tests.models.signal");
	// assertNotNull(model);
	// assertEquals(4, model.getOwnedMembers().size());
	//
	// Signal sig = (Signal) model.getMember("Sig");
	// assertNotNull(sig);
	// assertEquals(3, sig.getOwnedAttributes().size());
	// assertEquals("Sig", sig.getName());
	// Property a = sig.getOwnedAttribute("val", null);
	// assertNotNull(a);
	// assertEquals("Integer", a.getType().getName());
	// Property b = sig.getOwnedAttribute("b", null);
	// assertNotNull(b);
	// assertEquals("Boolean", b.getType().getName());
	// Property c = sig.getOwnedAttribute("param", null);
	// assertNotNull(c);
	// assertEquals("String", c.getType().getName());
	//
	// SignalEvent ev = (SignalEvent) model.getMember("Sig_event");
	// assertNotNull(ev);
	// assertEquals(ev.getSignal(), sig);
	// }
	//
	//
	//

	//
	// @Test
	// public void testStateMachine() throws Exception {
	// org.eclipse.uml2.uml.Model model =
	// ModelExportTestUtils.export("hu.elte.txtuml.export.uml2.tests.models.sm");
	// assertNotNull(model);
	// Signal s = (Signal) model.getMember("TestSignal");
	// Class c = (Class) model.getMember("TestClass");
	// assertNotNull(c);
	// StateMachine sm = (StateMachine) c.getClassifierBehavior();
	// assertNotNull(sm);
	// assertEquals(1, sm.getRegions().size());
	// Region r = sm.getRegions().get(0);
	// assertEquals(2, r.getSubvertices().size());
	// Pseudostate a = (Pseudostate) r.getSubvertex("Init");
	// assertNotNull(a);
	// State b = (State) r.getSubvertex("S1");
	// assertNotNull(b);
	// assertEquals(2, r.getTransitions().size());
	// Transition ab = r.getTransition("Init_S1");
	// assertEquals(a, ab.getSource());
	// assertEquals(b, ab.getTarget());
	// assertEquals(0, ab.getTriggers().size());
	// Transition bb = r.getTransition("S1_S1");
	// assertEquals(b, bb.getSource());
	// assertEquals(b, bb.getTarget());
	// assertEquals(1, bb.getTriggers().size());
	// SignalEvent ev = (SignalEvent) bb.getTriggers().get(0).getEvent();
	// assertNotNull(ev);
	// assertEquals(s, ev.getSignal());
	// }
	//
	// @Test
	// public void testOperationBehavior() throws Exception {
	// org.eclipse.uml2.uml.Model model = ModelExportTestUtils
	// .export("hu.elte.txtuml.export.uml2.tests.models.operation_behavior");
	// assertNotNull(model);
	// Class testClass = (Class) model.getMember("TestClass");
	// assertNotNull(testClass);
	// EList<Operation> ops = testClass.getOwnedOperations();
	// assertEquals(1, ops.size());
	// Operation op = testClass.getOperation("op", null, null);
	// assertNotNull(op);
	// EList<Behavior> behaviors = op.getMethods();
	// OpaqueBehavior behavior = null;
	// for (int i = 0; i < behaviors.size(); ++i) {
	// Behavior b = behaviors.get(i);
	// if (b.getName().equals("op_opaqueBehavior") && b instanceof
	// OpaqueBehavior) {
	// behavior = (OpaqueBehavior) b;
	// break;
	// }
	// }
	// assertNotNull(behavior);
	// assertEquals(testClass, behavior.getOwner());
	// assertEquals(op, behavior.getSpecification());
	// assertEquals(1, behavior.getLanguages().size());
	// assertEquals("JtxtUML", behavior.getLanguages().get(0));
	// assertEquals(1, behavior.getBodies().size());
	// assertEquals("{a=5;}", behavior.getBodies().get(0).replaceAll("\\s",
	// ""));
	// }
	//
	// @Test
	// public void testCreateActionBehavior() throws Exception {
	// SequenceNode body = loadActionCode("create_and_destroy", "TestClass",
	// "ObjectCreate");
	//
	// CreateObjectAction act = (CreateObjectAction)
	// body.getExecutableNodes().get(2);
	//
	// assertEquals(cls.getName(), act.getResult().getType().getName());
	// }
	//
	// @Test
	// public void testDestroyActionBehavior() throws Exception {
	// SequenceNode body = loadActionCode("create_and_destroy", "TestClass",
	// "ObjectCreate");
	//
	// DestroyObjectAction act = (DestroyObjectAction)
	// body.getExecutableNode("delete cls");
	//
	// assertEquals(cls.getName(), act.getTarget().getType().getName());
	// }
	//
	// @Test
	// public void testLinkAction() throws Exception {
	// SequenceNode bodyNode = loadActionCode("link_and_unlink", "A",
	// "LinkUnlinkAction");
	//
	// Class classB = (Class) model.getMember("B");
	// assertNotNull(classB);
	//
	// CreateLinkAction act = (CreateLinkAction)
	// bodyNode.getExecutableNodes().get(6);
	//
	// EList<?> list = act.getEndData();
	//
	// LinkEndCreationData leftEnd = (LinkEndCreationData) list.get(0);
	// LinkEndCreationData rightEnd = (LinkEndCreationData) list.get(1);
	//
	// assertEquals(cls.getName(), leftEnd.getEnd().getType().getName());
	// assertEquals(classB.getName(), rightEnd.getEnd().getType().getName());
	// }
	//
	// @Test
	// public void testUnLinkAction() throws Exception {
	// SequenceNode body = loadActionCode("link_and_unlink", "A",
	// "LinkUnlinkAction");
	//
	// Class classB = (Class) model.getMember("B");
	// assertNotNull(classB);
	//
	// DestroyLinkAction act = (DestroyLinkAction)
	// body.getExecutableNodes().get(9);
	//
	// EList<?> list = act.getEndData();
	//
	// LinkEndDestructionData leftEnd = (LinkEndDestructionData) list.get(0);
	// LinkEndDestructionData rightEnd = (LinkEndDestructionData) list.get(1);
	//
	// assertEquals(cls.getName(), leftEnd.getEnd().getType().getName());
	// assertEquals(classB.getName(), rightEnd.getEnd().getType().getName());
	// }
	//
	// @Test
	// public void testSendAction() throws Exception {
	// SequenceNode body = loadActionCode("send", "A", "SendAction");
	//
	// Class classB = (Class) model.getMember("B");
	// assertNotNull(classB);
	//
	// SendObjectAction act = (SendObjectAction)
	// body.getExecutableNodes().get(10);
	//
	// assertEquals("Sig", act.getRequest().getType().getName());
	// assertEquals(classB.getName(), act.getTarget().getType().getName());
	// }
	//
	// @Test
	// public void testStartAction() throws Exception {
	// SequenceNode body = loadActionCode("start", "TestClass", "S1");
	//
	// StartClassifierBehaviorAction act = (StartClassifierBehaviorAction)
	// body.getExecutableNodes().get(3);
	//
	// InputPin StartedClassPin = act.getInputs().get(0);
	//
	// assertEquals(cls.getName(), StartedClassPin.getType().getName());
	// }
	//
	// @Test
	// public void testIfControl() throws Exception {
	// SequenceNode bodyNode = loadActionCode("if_control", "TestModelClass",
	// "IfControl");
	//
	// ConditionalNode act = (ConditionalNode)
	// bodyNode.getExecutableNodes().get(4);
	//
	// assertEquals(1, act.getClauses().get(0).getBodies().size());
	// assertEquals(1, act.getClauses().get(1).getBodies().size());
	// }
	//
	//
	// @Test
	// public void testIfThen() throws Exception {
	// SequenceNode bodyNode = loadActionCode("if_then_control",
	// "TestModelClass", "IfControl");
	//
	// ConditionalNode act = (ConditionalNode)
	// bodyNode.getExecutableNodes().get(4);
	//
	// assertEquals(1, act.getClauses().get(0).getBodies().size());
	// assertEquals(1, act.getClauses().get(1).getBodies().size());
	// }

	//
	// @Test
	// public void testWhileControl() throws Exception {
	// SequenceNode body = loadActionCode("while_control", "TestClass",
	// "WhileControl");
	//
	// LoopNode loop = (LoopNode) body.getExecutableNode("while gt(x,0)");
	//
	// SequenceNode loopBody = (SequenceNode) loop.getBodyParts().get(0);
	//
	// assertEquals(1, loop.getTests().size());
	//
	// assertNotNull(((SequenceNode)
	// loop.getTest("cond")).getExecutableNode("gt(x,0)"));
	//
	// assertEquals("Boolean", loop.getDecider().getType().getName());
	// assertEquals(3, loopBody.getExecutableNodes().size()); // read var, dec,
	// write var
	// }
	//
	// @Test
	// public void testDoCycle() throws Exception {
	// SequenceNode body = loadActionCode("do_while_control", "TestClass",
	// "DoWhileControl");
	//
	// LoopNode loop = (LoopNode) body.getExecutableNode("dowhile gt(x,0)");
	//
	// SequenceNode loopBody = (SequenceNode) loop.getBodyPart("body");
	//
	// assertEquals(1, loop.getTests().size());
	//
	// assertNotNull(((SequenceNode)
	// loop.getTest("cond")).getExecutableNode("gt(x,0)"));
	//
	// assertEquals("Boolean", loop.getDecider().getType().getName());
	// assertEquals(3, loopBody.getExecutableNodes().size()); // read var, dec,
	// write var
	// assertEquals(3, ((SequenceNode)
	// loop.getNode("init")).getExecutableNodes().size()); // the same as the
	// loop body
	// }
	//
	// @Test
	// public void testForEachControl() throws Exception {
	// SequenceNode body = loadActionCode("foreach_control", "TestClass",
	// "ForEachControl");
	//
	// ExpansionRegion loop = (ExpansionRegion)
	// body.getExecutableNode("foreach");
	//
	// assertEquals(3, loop.getNodes().size()); // input, read variable, create
	// object
	//
	// assertEquals("Collection",
	// loop.getInputElements().get(0).getType().getName());
	// }

	//
	// @Test
	// public void testLogAction() throws Exception {
	// SequenceNode body = loadActionCode("log", "TestClass", "S1");
	//
	// assertEquals(2, body.getExecutableNodes().size()); // String spec, log
	// action
	// }

	// @Test
	// public void testReturn() throws Exception {
	// model = ModelExportTestUtils
	// .export("hu.elte.txtuml.export.uml2.tests.models.return_stmt");
	// assertNotNull(model);
	// cls = (Class) model.getMember("TestModelClass");
	// assertNotNull(cls);
	// Activity act = (Activity) cls.getOwnedBehavior("returnOp");
	// assertNotNull(act);
	// ActivityNode finalNode = act.getNode("return_paramNode");
	// assertNotNull(finalNode.getIncoming("objectflow_from_10_to_return_paramNode"));
	// }

	private SequenceNode loadActionCode(Model model, String className, String operationName) throws Exception {
		Class cls = cls(model, className);
		Operation op = operation(cls, operationName);
		Activity behav = (Activity) op.getMethod(operationName);
		assertNotNull(behav);
		SequenceNode bodyNode = (SequenceNode) behav.getOwnedNode("#body");
		assertNotNull(bodyNode);
		return bodyNode;
	}

}
