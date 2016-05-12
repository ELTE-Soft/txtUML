package hu.elte.txtuml.export.uml2;

import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestControlStructures extends UMLExportTestBase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ModelExportTestUtils.initialize();
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
	public void testWhileLoop() throws Exception {
		Model model = model("hu.elte.txtuml.export.uml2.tests.models.while_control");
		SequenceNode body = loadActionCode(model, "TestClass", "test");
		LoopNode loopNode = node(body, 0, "while (i>0) { ... }", LoopNode.class);
		SequenceNode bodyNode = loopBody(loopNode, 0, null, SequenceNode.class);
		node(bodyNode, 0, "i=--i;", SequenceNode.class);
		loopCond(loopNode, "i>0", CallOperationAction.class);
	}

}
