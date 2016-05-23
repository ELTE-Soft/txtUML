package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.AddStructuralFeatureValueAction;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.CreateLinkAction;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyLinkAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.ReadSelfAction;
import org.eclipse.uml2.uml.ReadStructuralFeatureAction;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.StartObjectBehaviorAction;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.TestIdentityAction;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestActionCode extends UMLExportTestBase {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ModelExportTestUtils.initialize();
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
		Activity guardActivity = getGuardActivity(tr2);
		node(guardActivity, 2, "return", ActivityParameterNode.class);
		SequenceNode guardBody = getActivityBody(guardActivity);
		node(guardBody, 0, "return false", SequenceNode.class);
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
	public void testToString() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.toString");

		SequenceNode body = loadActionCode(model, "TestClass", "testToString");
		SequenceNode toStringStmt = node(body, 0, "sut.toString;", SequenceNode.class);
		node(toStringStmt, 0, "sut", ReadVariableAction.class);
		node(toStringStmt, 1, "sut.toString", CallOperationAction.class);
	}	
	
	@Test
	public void testPrimitiveToString() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.toString");

		SequenceNode body = loadActionCode(model, "TestClass", "testPrimitiveToString");
		SequenceNode toStringStmt = node(body, 0, "3.toString;", SequenceNode.class);
		node(toStringStmt, 0, "3", ValueSpecificationAction.class);
		node(toStringStmt, 1, "3.toString", CallOperationAction.class);
	}
	
	@Test
	public void testAutoToString() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.toString");

		SequenceNode body = loadActionCode(model, "TestClass", "testAuto");
		node(body, 0, "\"a\"", ValueSpecificationAction.class);
		node(body, 1, "3", ValueSpecificationAction.class);
		node(body, 2, "3.toString", CallOperationAction.class);
		node(body, 3, "\"a\"+3.toString", CallOperationAction.class);
		node(body, 4, "a=\"a\"+3.toString", AddVariableValueAction.class);
	}	
	
	@Test
	public void testValueOf() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.valueOf");

		SequenceNode body = loadActionCode(model, "TestClass", "testValueOf");
		SequenceNode stmt = node(body, 0, "3;", SequenceNode.class);
		SequenceNode valueOfNode = node(stmt, 0, "3", SequenceNode.class);
		node(valueOfNode, 0, "3", ValueSpecificationAction.class);
	}	
	
	@Test
	public void testEquality() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.equalities");

		SequenceNode body = loadActionCode(model, "TestClass", "testEquality");
		node(body, 0, "\"Fdf\"", ValueSpecificationAction.class);
		node(body, 1, "\"Str\"", ValueSpecificationAction.class);
		node(body, 2, "\"Fdf\"==\"Str\"", TestIdentityAction.class);
		node(body, 3, "b=\"Fdf\"==\"Str\"", AddVariableValueAction.class);
	}	
	
	@Test
	public void testInequality() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.equalities");

		SequenceNode body = loadActionCode(model, "TestClass", "testInequality");
		node(body, 0, "\"Fdf\"", ValueSpecificationAction.class);
		node(body, 1, "\"Str\"", ValueSpecificationAction.class);
		node(body, 2, "\"Fdf\"==\"Str\"", TestIdentityAction.class);
		node(body, 3, "!\"Fdf\"==\"Str\"", CallOperationAction.class);
		node(body, 4, "b=!\"Fdf\"==\"Str\"", AddVariableValueAction.class);
	}	
	
	@Test
	public void testImplicitCtorInvoke() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.ctors");
		SequenceNode body = loadActionCode(model, "ClassExtending", "ClassExtending");
		node(body, 0, "ClassWithCtors()", CallOperationAction.class);
	}

}
