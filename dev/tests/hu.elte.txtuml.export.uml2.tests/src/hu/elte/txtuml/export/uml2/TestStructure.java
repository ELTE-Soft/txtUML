package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.LinkEndCreationData;
import org.eclipse.uml2.uml.LinkEndDestructionData;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestStructure {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ModelExportTestUtils.initialize();
	}

	@Test
	public void testAttribute() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.attribute");
		assertNotNull(model);
		Class testClass = (Class) model.getMember("TestClass");
		assertNotNull(testClass);
		Property a = testClass.getAttribute("a", null);
		assertNotNull(a);
		assertEquals(a.getType().getName(), "Integer");
		Property b = testClass.getAttribute("b", null);
		assertNotNull(b);
		assertEquals(b.getType().getName(), "Boolean");
		Property c = testClass.getAttribute("c", null);
		assertNotNull(c);
		assertEquals(c.getType().getName(), "String");
	}

	@Test
	public void testOperation() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.operation");
		assertNotNull(model);
		Class testClass = (Class) model.getMember("TestClass");
		assertNotNull(testClass);
		EList<Operation> ops = testClass.getOwnedOperations();
		assertEquals(2, ops.size());
		Operation op1 = testClass.getOperation("op1", null, null);
		assertNotNull(op1);
		EList<Parameter> params = op1.getOwnedParameters();
		assertEquals(3, params.size());
		for (Parameter param : params) {
			if (param.getType().getName().equals("Boolean")) {
				assertEquals("b", param.getName());
			} else if (param.getType().getName().equals("String")) {
				assertEquals("c", param.getName());
			} else if (param.getType().getName().equals("Integer")) {
				assertEquals(param.getDirection(), ParameterDirectionKind.RETURN_LITERAL);
			}
		}
		Operation op2 = testClass.getOwnedOperation("op2", null, null);
		params = op2.getOwnedParameters();
		assertEquals(0, params.size());
	}

	@Test
	public void testSignal() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.signal");
		assertNotNull(model);
		assertEquals(4, model.getOwnedMembers().size());

		Signal sig = (Signal) model.getMembers().get(0);
		assertNotNull(sig);
		assertEquals(3, sig.getOwnedAttributes().size());
		assertEquals("Sig", sig.getName());
		Property a = sig.getOwnedAttribute("val", null);
		assertNotNull(a);
		assertEquals("Integer", a.getType().getName());
		Property b = sig.getOwnedAttribute("b", null);
		assertNotNull(b);
		assertEquals("Boolean", b.getType().getName());
		Property c = sig.getOwnedAttribute("param", null);
		assertNotNull(c);
		assertEquals("String", c.getType().getName());

		SignalEvent ev = (SignalEvent) model.getMember("Sig_event");
		assertNotNull(ev);
		assertEquals(ev.getSignal(), sig);
	}

	@Test
	public void testAssociation() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.association");
		assertNotNull(model);
		assertEquals(8, model.getOwnedMembers().size());

		Class a = (Class) model.getMember("A");
		assertNotNull(a);

		Class b = (Class) model.getMember("B");
		assertNotNull(b);

		Association ab = (Association) model.getMember("AB1");
		assertNotNull(ab);
		assertEquals(2, ab.getOwnedEnds().size());
		Property aend = ab.getOwnedEnd("AEnd", null);
		assertEquals(a, aend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, aend.getAggregation());
		assertEquals(0, aend.getLower());
		assertEquals(1, aend.getUpper());
		assertEquals(true, aend.isNavigable());
		Property bend = ab.getOwnedEnd("BEnd", null);
		assertEquals(b, bend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, bend.getAggregation());
		assertEquals(0, bend.getLower());
		assertEquals(-1, bend.getUpper());
		assertEquals(true, bend.isNavigable());

		ab = (Association) model.getMember("AB2");
		assertNotNull(ab);
		assertEquals(2, ab.getOwnedEnds().size());
		aend = ab.getOwnedEnd("AEnd", null);
		assertEquals(a, aend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, aend.getAggregation());
		assertEquals(1, aend.getLower());
		assertEquals(1, aend.getUpper());
		assertEquals(true, aend.isNavigable());
		bend = ab.getOwnedEnd("BEnd", null);
		assertEquals(b, bend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, bend.getAggregation());
		assertEquals(1, bend.getLower());
		assertEquals(-1, bend.getUpper());
		assertEquals(true, bend.isNavigable());

		ab = (Association) model.getMember("AB3");
		assertNotNull(ab);
		assertEquals(2, ab.getOwnedEnds().size());
		aend = ab.getOwnedEnd("AEnd", null);
		assertEquals(a, aend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, aend.getAggregation());
		assertEquals(3, aend.getLower());
		assertEquals(4, aend.getUpper());
		assertEquals(true, aend.isNavigable());
		bend = ab.getOwnedEnd("BEnd", null);
		assertEquals(b, bend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, bend.getAggregation());
		assertEquals(0, bend.getLower());
		assertEquals(100, bend.getUpper());
		assertEquals(true, bend.isNavigable());

		ab = (Association) model.getMember("AB4");
		assertNotNull(ab);
		assertEquals(2, ab.getOwnedEnds().size());
		aend = ab.getOwnedEnd("AEnd", null);
		assertEquals(a, aend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, aend.getAggregation());
		assertEquals(0, aend.getLower());
		assertEquals(1, aend.getUpper());
		assertEquals(false, aend.isNavigable());
		bend = ab.getOwnedEnd("BEnd", null);
		assertEquals(b, bend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, bend.getAggregation());
		assertEquals(0, bend.getLower());
		assertEquals(-1, bend.getUpper());
		assertEquals(false, bend.isNavigable());

		ab = (Association) model.getMember("AB5");
		assertNotNull(ab);
		assertEquals(2, ab.getOwnedEnds().size());
		aend = ab.getOwnedEnd("AEnd", null);
		assertEquals(a, aend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, aend.getAggregation());
		assertEquals(1, aend.getLower());
		assertEquals(1, aend.getUpper());
		assertEquals(false, aend.isNavigable());
		bend = ab.getOwnedEnd("BEnd", null);
		assertEquals(b, bend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, bend.getAggregation());
		assertEquals(1, bend.getLower());
		assertEquals(-1, bend.getUpper());
		assertEquals(false, bend.isNavigable());

		ab = (Association) model.getMember("AB6");
		assertNotNull(ab);
		assertEquals(2, ab.getOwnedEnds().size());
		aend = ab.getOwnedEnd("AEnd", null);
		assertEquals(a, aend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, aend.getAggregation());
		assertEquals(3, aend.getLower());
		assertEquals(4, aend.getUpper());
		assertEquals(false, aend.isNavigable());
		bend = ab.getOwnedEnd("BEnd", null);
		assertEquals(b, bend.getType());
		assertEquals(AggregationKind.NONE_LITERAL, bend.getAggregation());
		assertEquals(0, bend.getLower());
		assertEquals(100, bend.getUpper());
		assertEquals(false, bend.isNavigable());
	}

	@Test
	public void testGeneralization() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.generalization");
		assertNotNull(model);
		Class b = (Class) model.getMember("B");
		assertNotNull(b);
		assertEquals(0, b.getOwnedAttributes().size());
		assertEquals(1, b.getSuperClasses().size());
		assertNotNull(b.getSuperClass("A"));
	}

	@Test
	public void testStateMachine() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils.export("hu.elte.txtuml.export.uml2.tests.models.sm");
		assertNotNull(model);
		Signal s = (Signal) model.getMember("TestSignal");
		Class c = (Class) model.getMember("TestClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());
		Region r = sm.getRegions().get(0);
		assertEquals(2, r.getSubvertices().size());
		Pseudostate a = (Pseudostate) r.getSubvertex("Init");
		assertNotNull(a);
		State b = (State) r.getSubvertex("S1");
		assertNotNull(b);
		assertEquals(2, r.getTransitions().size());
		Transition ab = r.getTransition("Init_S1");
		assertEquals(a, ab.getSource());
		assertEquals(b, ab.getTarget());
		assertEquals(0, ab.getTriggers().size());
		Transition bb = r.getTransition("S1_S1");
		assertEquals(b, bb.getSource());
		assertEquals(b, bb.getTarget());
		assertEquals(1, bb.getTriggers().size());
		SignalEvent ev = (SignalEvent) bb.getTriggers().get(0).getEvent();
		assertNotNull(ev);
		assertEquals(s, ev.getSignal());
	}

	@Test
	public void testOperationBehavior() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.operation_behavior");
		assertNotNull(model);
		Class testClass = (Class) model.getMember("TestClass");
		assertNotNull(testClass);
		EList<Operation> ops = testClass.getOwnedOperations();
		assertEquals(1, ops.size());
		Operation op = testClass.getOperation("op", null, null);
		assertNotNull(op);
		EList<Behavior> behaviors = op.getMethods();
		OpaqueBehavior behavior = null;
		for (int i = 0; i < behaviors.size(); ++i) {
			Behavior b = behaviors.get(i);
			if (b.getName().equals("op_opaqueBehavior") && b instanceof OpaqueBehavior) {
				behavior = (OpaqueBehavior) b;
				break;
			}
		}
		assertNotNull(behavior);
		assertEquals(testClass, behavior.getOwner());
		assertEquals(op, behavior.getSpecification());
		assertEquals(1, behavior.getLanguages().size());
		assertEquals("JtxtUML", behavior.getLanguages().get(0));
		assertEquals(1, behavior.getBodies().size());
		assertEquals("{a=5;}", behavior.getBodies().get(0).replaceAll("\\s", ""));
	}

	@Test
	public void testCreateActionBehavior() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.create_and_destroy");
		assertNotNull(model);
		Class c = (Class) model.getMember("TestClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State ObjectCreate = (State) r.getSubvertex("ObjectCreate");

		Activity behav = (Activity) ObjectCreate.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode bodyNode = (SequenceNode) nodesList.get(2);

		CreateObjectAction act = (CreateObjectAction) bodyNode.getExecutableNodes().get(2);

		assertEquals(c.getName(), act.getResult().getType().getName());
	}

	@Test
	public void testDestroyActionBehavior() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.create_and_destroy");
		assertNotNull(model);
		Class c = (Class) model.getMember("TestClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State ObjectDestroy = (State) r.getSubvertex("ObjectCreate");

		Activity behav = (Activity) ObjectDestroy.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(2);

		DestroyObjectAction act = (DestroyObjectAction) body.getExecutableNodes().get(4);

		assertEquals(c.getName(), act.getTarget().getType().getName());
	}

	@Test
	public void testLinkAction() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.link_and_unlink");
		assertNotNull(model);
		Class classA = (Class) model.getMember("A");
		assertNotNull(classA);
		StateMachine sm = (StateMachine) classA.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Class classB = (Class) model.getMember("B");
		assertNotNull(classB);

		Region r = sm.getRegions().get(0);

		State LinkUnlinkAction = (State) r.getSubvertex("LinkUnlinkAction");

		Activity behav = (Activity) LinkUnlinkAction.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode bodyNode = (SequenceNode) nodesList.get(2);

		CreateLinkAction act = (CreateLinkAction) bodyNode.getExecutableNodes().get(6);

		EList<?> list = act.getEndData();

		LinkEndCreationData leftEnd = (LinkEndCreationData) list.get(0);
		LinkEndCreationData rightEnd = (LinkEndCreationData) list.get(1);

		assertEquals(classA.getName(), leftEnd.getEnd().getType().getName());
		assertEquals(classB.getName(), rightEnd.getEnd().getType().getName());
	}

	@Test
	public void testUnLinkAction() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.link_and_unlink");
		assertNotNull(model);
		Class classA = (Class) model.getMember("A");
		assertNotNull(classA);
		StateMachine sm = (StateMachine) classA.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());
		
		Class classB = (Class) model.getMember("B");
		assertNotNull(classB);

		Region r = sm.getRegions().get(0);

		State LinkUnlinkAction = (State) r.getSubvertex("LinkUnlinkAction");

		Activity behav = (Activity) LinkUnlinkAction.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(2);

		DestroyLinkAction act = (DestroyLinkAction) body.getExecutableNodes().get(9);

		EList<?> list = act.getEndData();

		LinkEndDestructionData leftEnd = (LinkEndDestructionData) list.get(0);
		LinkEndDestructionData rightEnd = (LinkEndDestructionData) list.get(1);

		assertEquals(classA.getName(), leftEnd.getEnd().getType().getName());
		assertEquals(classB.getName(), rightEnd.getEnd().getType().getName());
	}

	@Test
	public void testSendAction() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.send");
		assertNotNull(model);
		Class classA = (Class) model.getMember("A");
		assertNotNull(classA);
		StateMachine sm = (StateMachine) classA.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());
		
		Class classB = (Class) model.getMember("B");
		assertNotNull(classB);

		Region r = sm.getRegions().get(0);

		State LinkUnlinkAction = (State) r.getSubvertex("SendAction");

		Activity behav = (Activity) LinkUnlinkAction.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(2);

		SendObjectAction act = (SendObjectAction) body.getExecutableNodes().get(10);

		assertEquals("Sig", act.getRequest().getType().getName());
		assertEquals(classB.getName(), act.getTarget().getType().getName());
	}

	@Test
	public void testStartAction() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils.export("hu.elte.txtuml.export.uml2.tests.models.start");
		assertNotNull(model);
		Class c = (Class) model.getMember("TestClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State StartStateTest = (State) r.getSubvertex("S1");

		Activity behav = (Activity) StartStateTest.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(2);

		StartClassifierBehaviorAction act = (StartClassifierBehaviorAction) body.getExecutableNodes().get(2);

		InputPin StartedClassPin = act.getInputs().get(0);

		assertEquals(c.getName(), StartedClassPin.getType().getName());
	}

	@Test
	public void testIfControl() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.if_control");
		assertNotNull(model);
		Class c = (Class) model.getMember("TestModelClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State StartStateTest = (State) r.getSubvertex("IfControl");

		Activity behav = (Activity) StartStateTest.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getOwnedNodes();

		assertEquals(3, nodesList.size());

		SequenceNode bodyNode = (SequenceNode) nodesList.get(2);

		ConditionalNode act = (ConditionalNode) bodyNode.getExecutableNodes().get(4);

		assertEquals(1, act.getClauses().get(0).getBodies().size());
		assertEquals(1, act.getClauses().get(1).getBodies().size());
	}

	@Test
	public void testForControl() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.for_control");
		assertNotNull(model);
		Class c = (Class) model.getMember("TestClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State StartStateTest = (State) r.getSubvertex("ForControl");

		Activity behav = (Activity) StartStateTest.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(2);

		LoopNode loop = (LoopNode) body.getExecutableNodes().get(2);
		
		SequenceNode loopBody = (SequenceNode) loop.getBodyParts().get(0);
		SequenceNode loopUpdate = (SequenceNode) loop.getBodyParts().get(1);
		
		assertEquals(2, loopBody.getExecutableNodes().size()); // read var, create obj
		assertEquals(3, loopUpdate.getExecutableNodes().size()); // read var, inc, write var
	}

	@Test
	public void testWhileControl() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.while_control");
		assertNotNull(model);
		Class c = (Class) model.getMember("TestClass");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State StartStateTest = (State) r.getSubvertex("WhileControl");

		Activity behav = (Activity) StartStateTest.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(3, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(2);

		LoopNode loop = (LoopNode) body.getExecutableNode("while gt(x,0)");
		
		SequenceNode loopBody = (SequenceNode) loop.getBodyParts().get(0);
		
		assertEquals(1, loop.getTests().size());
		
		assertNotNull(((SequenceNode) loop.getTest("cond")).getExecutableNode("gt(x,0)"));
		
		assertEquals("Boolean", loop.getDecider().getType().getName());
		assertEquals(3, loopBody.getExecutableNodes().size()); // read var, dec, write var
	}

	@Test
	@Ignore
	public void testForEachControl() throws Exception {
		org.eclipse.uml2.uml.Model model = ModelExportTestUtils
				.export("hu.elte.txtuml.export.uml2.tests.models.TestForEachControlModel");
		assertNotNull(model);
		Class c = (Class) model.getMember("A");
		assertNotNull(c);
		StateMachine sm = (StateMachine) c.getClassifierBehavior();
		assertNotNull(sm);
		assertEquals(1, sm.getRegions().size());

		Region r = sm.getRegions().get(0);

		State StartStateTest = (State) r.getSubvertex("ForEachControl");

		Activity behav = (Activity) StartStateTest.getEntry();
		assertNotNull(behav);

		EList<?> nodesList = behav.getNodes();

		assertEquals(5, nodesList.size());

		SequenceNode body = (SequenceNode) nodesList.get(1);

		ExpansionRegion forEachLoop = (ExpansionRegion) body.getExecutableNodes().get(1);
		SequenceNode forEachBody = (SequenceNode) forEachLoop.getNodes().get(1);
		System.out.println(forEachBody.getExecutableNodes());

		assertEquals(1, forEachBody.getExecutableNodes().size());
	}
}
