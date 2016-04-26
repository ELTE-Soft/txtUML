package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkEndCreationData;
import org.eclipse.uml2.uml.LinkEndDestructionData;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.ReadSelfAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStructure {

	private Model model;
	private Class cls;
	private Activity behav;
	private Operation op;
	private SequenceNode bodyNode;

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

	private Property assocEnd(Class a, Classifier ab, String name, AggregationKind kind, int lowerBound, int upperBound, boolean isNavigable) {
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
	public void testCompoundOps() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.compound_ops");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		SequenceNode stmtNode = (SequenceNode) body.getNode("this.fld=++this.fld;");
		SequenceNode exprNode = (SequenceNode) stmtNode.getNode("this.fld=++this.fld");
		node(exprNode, "this", ReadSelfAction.class);
		node(exprNode, "this.fld", ReadStructuralFeatureAction.class);
		node(exprNode, "1", ValueSpecificationAction.class);
		node(exprNode, "++this.fld", CallOperationAction.class);
		node(exprNode, "this.fld=++this.fld", AddStructuralFeatureValueAction.class);
	}
	
	private ActivityNode node(SequenceNode parent, String name, java.lang.Class<?> type) {
		ActivityNode node = parent.getNode(name);
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return node;
	}

//	@Test
//	public void testOperation() throws Exception {
//		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
//				.export("hu.elte.txtuml.export.uml2.tests.models.operation");
//		assertNotNull(model);
//		Class testClass = (Class) model.getMember("TestClass");
//		assertNotNull(testClass);
//		EList<Operation> ops = testClass.getOwnedOperations();
//		assertEquals(2, ops.size());
//		Operation op1 = testClass.getOperation("op1", null, null);
//		assertNotNull(op1);
//		EList<Parameter> params = op1.getOwnedParameters();
//		assertEquals(3, params.size());
//		for (Parameter param : params) {
//			if (param.getType().getName().equals("Boolean")) {
//				assertEquals("b", param.getName());
//			} else if (param.getType().getName().equals("String")) {
//				assertEquals("c", param.getName());
//			} else if (param.getType().getName().equals("Integer")) {
//				assertEquals(param.getDirection(), ParameterDirectionKind.RETURN_LITERAL);
//			}
//		}
//		Operation op2 = testClass.getOwnedOperation("op2", null, null);
//		params = op2.getOwnedParameters();
//		assertEquals(0, params.size());
//	}
//
//	@Test
//	public void testSignal() throws Exception {
//		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
//				.export("hu.elte.txtuml.export.uml2.tests.models.signal");
//		assertNotNull(model);
//		assertEquals(4, model.getOwnedMembers().size());
//
//		Signal sig = (Signal) model.getMember("Sig");
//		assertNotNull(sig);
//		assertEquals(3, sig.getOwnedAttributes().size());
//		assertEquals("Sig", sig.getName());
//		Property a = sig.getOwnedAttribute("val", null);
//		assertNotNull(a);
//		assertEquals("Integer", a.getType().getName());
//		Property b = sig.getOwnedAttribute("b", null);
//		assertNotNull(b);
//		assertEquals("Boolean", b.getType().getName());
//		Property c = sig.getOwnedAttribute("param", null);
//		assertNotNull(c);
//		assertEquals("String", c.getType().getName());
//
//		SignalEvent ev = (SignalEvent) model.getMember("Sig_event");
//		assertNotNull(ev);
//		assertEquals(ev.getSignal(), sig);
//	}
//
//
//
//	@Test
//	public void testGeneralization() throws Exception {
//		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
//				.export("hu.elte.txtuml.export.uml2.tests.models.generalization");
//		assertNotNull(model);
//		Class b = (Class) model.getMember("B");
//		assertNotNull(b);
//		assertEquals(0, b.getOwnedAttributes().size());
//		assertEquals(1, b.getSuperClasses().size());
//		assertNotNull(b.getSuperClass("A"));
//	}
//
//	@Test
//	public void testStateMachine() throws Exception {
//		org.eclipse.uml2.uml.Model model = ModelExportTestUtils.export("hu.elte.txtuml.export.uml2.tests.models.sm");
//		assertNotNull(model);
//		Signal s = (Signal) model.getMember("TestSignal");
//		Class c = (Class) model.getMember("TestClass");
//		assertNotNull(c);
//		StateMachine sm = (StateMachine) c.getClassifierBehavior();
//		assertNotNull(sm);
//		assertEquals(1, sm.getRegions().size());
//		Region r = sm.getRegions().get(0);
//		assertEquals(2, r.getSubvertices().size());
//		Pseudostate a = (Pseudostate) r.getSubvertex("Init");
//		assertNotNull(a);
//		State b = (State) r.getSubvertex("S1");
//		assertNotNull(b);
//		assertEquals(2, r.getTransitions().size());
//		Transition ab = r.getTransition("Init_S1");
//		assertEquals(a, ab.getSource());
//		assertEquals(b, ab.getTarget());
//		assertEquals(0, ab.getTriggers().size());
//		Transition bb = r.getTransition("S1_S1");
//		assertEquals(b, bb.getSource());
//		assertEquals(b, bb.getTarget());
//		assertEquals(1, bb.getTriggers().size());
//		SignalEvent ev = (SignalEvent) bb.getTriggers().get(0).getEvent();
//		assertNotNull(ev);
//		assertEquals(s, ev.getSignal());
//	}
//
//	@Test
//	public void testOperationBehavior() throws Exception {
//		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
//				.export("hu.elte.txtuml.export.uml2.tests.models.operation_behavior");
//		assertNotNull(model);
//		Class testClass = (Class) model.getMember("TestClass");
//		assertNotNull(testClass);
//		EList<Operation> ops = testClass.getOwnedOperations();
//		assertEquals(1, ops.size());
//		Operation op = testClass.getOperation("op", null, null);
//		assertNotNull(op);
//		EList<Behavior> behaviors = op.getMethods();
//		OpaqueBehavior behavior = null;
//		for (int i = 0; i < behaviors.size(); ++i) {
//			Behavior b = behaviors.get(i);
//			if (b.getName().equals("op_opaqueBehavior") && b instanceof OpaqueBehavior) {
//				behavior = (OpaqueBehavior) b;
//				break;
//			}
//		}
//		assertNotNull(behavior);
//		assertEquals(testClass, behavior.getOwner());
//		assertEquals(op, behavior.getSpecification());
//		assertEquals(1, behavior.getLanguages().size());
//		assertEquals("JtxtUML", behavior.getLanguages().get(0));
//		assertEquals(1, behavior.getBodies().size());
//		assertEquals("{a=5;}", behavior.getBodies().get(0).replaceAll("\\s", ""));
//	}
//
//	@Test
//	public void testCreateActionBehavior() throws Exception {
//		SequenceNode body = loadActionCode("create_and_destroy", "TestClass", "ObjectCreate");
//
//		CreateObjectAction act = (CreateObjectAction) body.getExecutableNodes().get(2);
//
//		assertEquals(cls.getName(), act.getResult().getType().getName());
//	}
//
//	@Test
//	public void testDestroyActionBehavior() throws Exception {
//		SequenceNode body = loadActionCode("create_and_destroy", "TestClass", "ObjectCreate");
//
//		DestroyObjectAction act = (DestroyObjectAction) body.getExecutableNode("delete cls");
//
//		assertEquals(cls.getName(), act.getTarget().getType().getName());
//	}
//
//	@Test
//	public void testLinkAction() throws Exception {
//		SequenceNode bodyNode = loadActionCode("link_and_unlink", "A", "LinkUnlinkAction");
//		
//		Class classB = (Class) model.getMember("B");
//		assertNotNull(classB);
//
//		CreateLinkAction act = (CreateLinkAction) bodyNode.getExecutableNodes().get(6);
//
//		EList<?> list = act.getEndData();
//
//		LinkEndCreationData leftEnd = (LinkEndCreationData) list.get(0);
//		LinkEndCreationData rightEnd = (LinkEndCreationData) list.get(1);
//
//		assertEquals(cls.getName(), leftEnd.getEnd().getType().getName());
//		assertEquals(classB.getName(), rightEnd.getEnd().getType().getName());
//	}
//
//	@Test
//	public void testUnLinkAction() throws Exception {
//		SequenceNode body = loadActionCode("link_and_unlink", "A", "LinkUnlinkAction");
//		
//		Class classB = (Class) model.getMember("B");
//		assertNotNull(classB);
//
//		DestroyLinkAction act = (DestroyLinkAction) body.getExecutableNodes().get(9);
//
//		EList<?> list = act.getEndData();
//
//		LinkEndDestructionData leftEnd = (LinkEndDestructionData) list.get(0);
//		LinkEndDestructionData rightEnd = (LinkEndDestructionData) list.get(1);
//
//		assertEquals(cls.getName(), leftEnd.getEnd().getType().getName());
//		assertEquals(classB.getName(), rightEnd.getEnd().getType().getName());
//	}
//
//	@Test
//	public void testSendAction() throws Exception {
//		SequenceNode body = loadActionCode("send", "A", "SendAction");
//		
//		Class classB = (Class) model.getMember("B");
//		assertNotNull(classB);
//
//		SendObjectAction act = (SendObjectAction) body.getExecutableNodes().get(10);
//
//		assertEquals("Sig", act.getRequest().getType().getName());
//		assertEquals(classB.getName(), act.getTarget().getType().getName());
//	}
//
//	@Test
//	public void testStartAction() throws Exception {
//		SequenceNode body = loadActionCode("start", "TestClass", "S1");
//
//		StartClassifierBehaviorAction act = (StartClassifierBehaviorAction) body.getExecutableNodes().get(3);
//
//		InputPin StartedClassPin = act.getInputs().get(0);
//
//		assertEquals(cls.getName(), StartedClassPin.getType().getName());
//	}
//
//	@Test
//	public void testIfControl() throws Exception {
//		SequenceNode bodyNode = loadActionCode("if_control", "TestModelClass", "IfControl");
//
//		ConditionalNode act = (ConditionalNode) bodyNode.getExecutableNodes().get(4);
//
//		assertEquals(1, act.getClauses().get(0).getBodies().size());
//		assertEquals(1, act.getClauses().get(1).getBodies().size());
//	}
//	
//	
//	@Test
//	public void testIfThen() throws Exception {
//		SequenceNode bodyNode = loadActionCode("if_then_control", "TestModelClass", "IfControl");
//
//		ConditionalNode act = (ConditionalNode) bodyNode.getExecutableNodes().get(4);
//
//		assertEquals(1, act.getClauses().get(0).getBodies().size());
//		assertEquals(1, act.getClauses().get(1).getBodies().size());
//	}
//
//	@Test
//	public void testForControl() throws Exception {
//		SequenceNode body = loadActionCode("for_control", "TestClass", "ForControl");
//
//		LoopNode loop = (LoopNode) body.getExecutableNodes().get(2);
//		
//		SequenceNode loopBody = (SequenceNode) loop.getBodyParts().get(0);
//		SequenceNode loopUpdate = (SequenceNode) loop.getBodyParts().get(1);
//		
//		assertEquals(2, loopBody.getExecutableNodes().size()); // read var, create obj
//		assertEquals(3, loopUpdate.getExecutableNodes().size()); // read var, inc, write var
//	}
//
//	@Test
//	public void testWhileControl() throws Exception {
//		SequenceNode body = loadActionCode("while_control", "TestClass", "WhileControl");
//
//		LoopNode loop = (LoopNode) body.getExecutableNode("while gt(x,0)");
//		
//		SequenceNode loopBody = (SequenceNode) loop.getBodyParts().get(0);
//		
//		assertEquals(1, loop.getTests().size());
//		
//		assertNotNull(((SequenceNode) loop.getTest("cond")).getExecutableNode("gt(x,0)"));
//		
//		assertEquals("Boolean", loop.getDecider().getType().getName());
//		assertEquals(3, loopBody.getExecutableNodes().size()); // read var, dec, write var
//	}
//
//	@Test
//	public void testDoCycle() throws Exception {
//		SequenceNode body = loadActionCode("do_while_control", "TestClass", "DoWhileControl");
//
//		LoopNode loop = (LoopNode) body.getExecutableNode("dowhile gt(x,0)");
//		
//		SequenceNode loopBody = (SequenceNode) loop.getBodyPart("body");
//		
//		assertEquals(1, loop.getTests().size());
//		
//		assertNotNull(((SequenceNode) loop.getTest("cond")).getExecutableNode("gt(x,0)"));
//		
//		assertEquals("Boolean", loop.getDecider().getType().getName());
//		assertEquals(3, loopBody.getExecutableNodes().size()); // read var, dec, write var
//		assertEquals(3, ((SequenceNode) loop.getNode("init")).getExecutableNodes().size()); // the same as the loop body
//	}
//
//	@Test
//	public void testForEachControl() throws Exception {
//		SequenceNode body = loadActionCode("foreach_control", "TestClass", "ForEachControl");
//
//		ExpansionRegion loop = (ExpansionRegion) body.getExecutableNode("foreach");
//		
//		assertEquals(3, loop.getNodes().size()); // input, read variable, create object
//		
//		assertEquals("Collection", loop.getInputElements().get(0).getType().getName());
//	}
//
//	@Test
//	public void testConstructor() throws Exception {
//		SequenceNode body = loadActionCode("ctors", "TestClass", "CtorCall");
//		
//		assertEquals(3, body.getExecutableNodes().size()); // value 10, create obj, call ctor 
//	}
//	
//	@Test
//	public void testLogAction() throws Exception {
//		SequenceNode body = loadActionCode("log", "TestClass", "S1");
//		
//		assertEquals(2, body.getExecutableNodes().size()); // String spec, log action
//	}
	
//	@Test
//	public void testReturn() throws Exception {
//		model = ModelExportTestUtils
//				.export("hu.elte.txtuml.export.uml2.tests.models.return_stmt");
//		assertNotNull(model);
//		cls = (Class) model.getMember("TestModelClass");
//		assertNotNull(cls);
//		Activity act = (Activity) cls.getOwnedBehavior("returnOp");
//		assertNotNull(act);
//		ActivityNode finalNode = act.getNode("return_paramNode");
//		assertNotNull(finalNode.getIncoming("objectflow_from_10_to_return_paramNode"));
//	}

	private SequenceNode loadActionCode(Model model, String className, String operationName) throws Exception {
		cls = cls(model, className);
		op = operation(cls, operationName);
		behav = (Activity) op.getMethod(operationName);
		assertNotNull(behav);
		bodyNode = (SequenceNode) behav.getOwnedNode("#body");
		assertNotNull(bodyNode);
		return bodyNode;
	}

}
