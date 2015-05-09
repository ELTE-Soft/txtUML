package hu.elte.txtuml.export.uml2.transform;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.SendSignalAction;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.api.*;
import hu.elte.txtuml.api.blocks.BlockBody;
import hu.elte.txtuml.api.blocks.Condition;
import hu.elte.txtuml.api.blocks.ParameterizedBlockBody;
import hu.elte.txtuml.export.uml2.transform.backend.DummyInstanceCreator;
import hu.elte.txtuml.export.uml2.transform.backend.ImportException;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceInformation;
import hu.elte.txtuml.export.uml2.transform.backend.InstanceManager;
import hu.elte.txtuml.export.uml2.transform.backend.UMLPrimitiveTypesProvider;
import hu.elte.txtuml.export.uml2.utils.ElementFinder;

public class ActionImporterTest {

	private ModelElement ownerInstance;
	private Class<?> txtUMLOwnerClass;
	private org.eclipse.uml2.uml.Class ownerClass;
	private org.eclipse.uml2.uml.Model model;
	private org.eclipse.uml2.uml.Signal signal;
	
	private static Object getStaticField(String fieldName, Class<?> ownerClass)
			throws IllegalArgumentException, IllegalAccessException
	{
		Field field = ElementFinder.findField(fieldName, ownerClass);
		field.setAccessible(true);
		Object ret = field.get(null);
		field.setAccessible(false);
		return ret;
	}
	
	private static Object invokeStaticMethod(String methodName, Class<?> ownerClass, Object... args) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method method = ElementFinder.findMethod(methodName, ownerClass);
		method.setAccessible(true);
		Object ret = method.invoke(null, args);
		method.setAccessible(false);
		return ret;
	}
	
	@Before
	public void setUp() throws Exception 
	{
		AbstractImporter.modelClass = TestModel.class;
		invokeStaticMethod("initModelImport", ModelImporter.class,"gen");
		model = (org.eclipse.uml2.uml.Model) getStaticField("currentModel", ModelImporter.class);
		ownerClass = model.createOwnedClass("Class1", false);
		signal = (org.eclipse.uml2.uml.Signal) model.createOwnedType("Signal1", UMLPackage.Literals.SIGNAL);
		signal.createOwnedAttribute("field1", UMLPrimitiveTypesProvider.getInteger());
		signal.createOwnedAttribute("field2", UMLPrimitiveTypesProvider.getBoolean());
		signal.createOwnedAttribute("field3", UMLPrimitiveTypesProvider.getString());
		txtUMLOwnerClass = TestModel.Class1.class;
		InstanceManager.createClassAndFieldInstancesAndInitClassAndFieldInstancesMap(txtUMLOwnerClass);
		Method method = ElementFinder.findMethod("method", txtUMLOwnerClass);
		Operation operation = (Operation) 
				invokeStaticMethod("importOperationSkeleton", ModelImporter.class, ownerClass, txtUMLOwnerClass, method);
		
		Activity activity = (Activity) ownerClass.createOwnedBehavior("method",UMLPackage.Literals.ACTIVITY);
		activity.setSpecification(operation);
		ownerInstance = (ModelElement) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		invokeStaticMethod("initMethodImport", MethodImporter.class, model, activity, method, ownerInstance);
	}

	@After
	public void tearDown() throws Exception
	{
		invokeStaticMethod("endMethodImport", MethodImporter.class,ownerInstance);
		invokeStaticMethod("endModelImport", ModelImporter.class);
	}

	@Test
	public void testImportStartObjectAction()
	{
		ModelClass instance = (ModelClass) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance, InstanceInformation.create("instance"));
		ActionImporter.importStartObjectAction(instance);

		// initial and final nodes, 3 parameter nodes (2 args and a return) + a start classifier behavior action
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 6); 
		StartClassifierBehaviorAction scba = (StartClassifierBehaviorAction)
				ActionImporter.currentActivity.getOwnedNode("startClassifierBehavior_instance");
		
		ValuePin valuePin = (ValuePin) scba.getInputs().get(0);
		OpaqueExpression opaqueExpr = (OpaqueExpression) valuePin.getValue();
		
		assertEquals(opaqueExpr.getBodies().get(0),"instance"); //started instance is "instance"
		assertEquals(scba.getIncomings().size(),1); //1 incoming edge to start classifier behavior action
		assertEquals(scba.getOutgoings().size(),0); //0 outgoing edge from start classifier behavior action yet
	}

	public void testImportLinkAction(boolean create) throws ImportException
	{
		new AssociationImporter(TestModel.Assoc.class, model).importAssociation();
		ModelClass instance1 = (ModelClass) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance1, InstanceInformation.create("instance1"));
		
		ModelClass instance2 = (ModelClass) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance2, InstanceInformation.create("instance2"));
		
		String actionName;
		if(create)
		{
			actionName = "link_instance1_and_instance2";
			ActionImporter.importCreateLinkAction(TestModel.Assoc.End1.class, instance1, TestModel.Assoc.End2.class, instance2);
		}
		else
		{
			actionName = "unlink_instance1_and_instance2";
			ActionImporter.importDestroyLinkAction(TestModel.Assoc.End1.class, instance1, TestModel.Assoc.End2.class, instance2);
		}
			
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 6); 
		LinkAction linkAction = (LinkAction)
				ActionImporter.currentActivity.getOwnedNode(actionName);
		
		ValuePin valuePin1 = (ValuePin) linkAction.getInputs().get(0);
		String expr1 = ( (OpaqueExpression) valuePin1.getValue() ).getBodies().get(0);
		ValuePin valuePin2 = (ValuePin) linkAction.getInputs().get(1);
		String expr2 = ( (OpaqueExpression) valuePin2.getValue() ).getBodies().get(0);
		
		assertEquals(linkAction.getInputs().size(),2);
		assertTrue(
				(expr1.equals("instance1") && expr2.equals("instance2") ) ||
				(expr1.equals("instance2") && expr2.equals("instance1") )
			);
		assertEquals(valuePin1.getType(),ownerClass);
		assertEquals(valuePin2.getType(),ownerClass);
		assertEquals(linkAction.getIncomings().size(),1); //1 incoming edge to link action
		assertEquals(linkAction.getOutgoings().size(),0); //0 outgoing edge from link action yet
	}
	@Test
	public void testImportCreateLinkAction() throws ImportException {
		
		testImportLinkAction(true);
	}

	@Test
	public void testImportDestroyLinkAction() throws ImportException {
		testImportLinkAction(false);
	}

	@Test
	public void testImportSendSignalAction() 
	{
		TestModel.Signal1 signalToSend = new TestModel().new Signal1(ModelInt.ONE, ModelBool.FALSE, ModelString.EMPTY); 
		ModelClass instance = (ModelClass) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance, InstanceInformation.create("instance"));
		
		ActionImporter.importSendSignalAction(instance, signalToSend);
		
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 6); 
		SendSignalAction sendAction = (SendSignalAction)
				ActionImporter.currentActivity.getOwnedNode("send_Signal1_to_instance");
		
		ValuePin targetPin = (ValuePin) sendAction.getTarget();
		OpaqueExpression targetExpr = (OpaqueExpression) targetPin.getValue();

		assertEquals(sendAction.getSignal(),signal);
		assertEquals(targetExpr.getBodies().get(0), "instance");
		assertEquals(targetExpr.getType(),ownerClass);
		assertEquals(targetPin.getType(),ownerClass);
		assertEquals(sendAction.getIncomings().size(),1); 
		assertEquals(sendAction.getOutgoings().size(),0); 
		assertEquals(sendAction.getArguments().size(),3);
		
		ValuePin arg1Pin = (ValuePin) sendAction.getArgument("field1", UMLPrimitiveTypesProvider.getInteger());
		OpaqueExpression arg1Expr = (OpaqueExpression) arg1Pin.getValue();
		assertEquals(arg1Expr.getBodies().get(0),"1");
		
		ValuePin arg2Pin = (ValuePin) sendAction.getArgument("field2", UMLPrimitiveTypesProvider.getBoolean());
		OpaqueExpression arg2Expr = (OpaqueExpression) arg2Pin.getValue();
		assertEquals(arg2Expr.getBodies().get(0),"false");
		
		ValuePin arg3Pin = (ValuePin) sendAction.getArgument("field3", UMLPrimitiveTypesProvider.getString());
		LiteralString arg3Expr = (LiteralString) arg3Pin.getValue();
		assertEquals(arg3Expr.getValue(),"");
	}

	@Test
	public void testImportWhileStatement() {
		/*While(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> {}
		)
		*/
		ActionImporter.importWhileStatement(
				new TestModelAccessories().cond, 
				() -> {}
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 6);
		
		DecisionNode decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision1");
		
		assertEquals(decisionNode.getIncomings().size(),2);
		assertEquals(decisionNode.getOutgoings().size(),1);
		
		ActivityEdge edge = decisionNode.getOutgoings().get(0);
		OpaqueExpression edgeGuard = (OpaqueExpression) edge.getGuard();
	
		assertEquals(edge.getTarget(), decisionNode);
		String edgeGuardExpr = edgeGuard.getBodies().get(0);
		
		assertEquals(edgeGuardExpr, "true and (1 > 0)");
				
		
		/*While(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> Action.send(new Class1(),new Signal1(ModelInt.ZERO, ModelBool.FALSE,ModelString.EMPTY))
		)
		 */
		ActionImporter.importWhileStatement(
				new TestModelAccessories().cond, 
				new TestModelAccessories().body1
			);
		
		assertEquals(decisionNode.getOutgoings().size(),2);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 10);
		edge = decisionNode.getOutgoings().get(1);
		edgeGuard = (OpaqueExpression) edge.getGuard();
		edgeGuardExpr = edgeGuard.getBodies().get(0);
		assertEquals(edgeGuardExpr, "else");
		
		decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision2");
		assertEquals(edge.getTarget(), decisionNode);
		assertEquals(decisionNode.getIncomings().size(),2);
		assertEquals(decisionNode.getOutgoings().size(),1);
		
		edge = decisionNode.getOutgoings().get(0);
		edgeGuard = (OpaqueExpression) edge.getGuard();
	
		assertEquals(edge.getTarget().eClass(), UMLPackage.Literals.CREATE_OBJECT_ACTION);
		edgeGuardExpr = edgeGuard.getBodies().get(0);
		
		assertEquals(edgeGuardExpr, "true and (1 > 0)");
	}

	@Test
	public void testImportIfStatementConditionBlockBody() {
		
		/*If(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> {}
		)
		*/
		ActionImporter.importIfStatement(
				new TestModelAccessories().cond, 
				() -> {}
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 7);
		
		DecisionNode decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision1");
		
		assertEquals(decisionNode.getIncomings().size(),1);
		assertEquals(decisionNode.getOutgoings().size(),2);
		
		ActivityEdge edge1 = decisionNode.getOutgoings().get(0);
		OpaqueExpression edge1guard = (OpaqueExpression) edge1.getGuard();
		ActivityEdge edge2 = decisionNode.getOutgoings().get(1);
		OpaqueExpression edge2guard = (OpaqueExpression) edge2.getGuard();
		assertEquals(edge1.getTarget().eClass(), UMLPackage.Literals.MERGE_NODE);
		assertEquals(edge2.getTarget().eClass(), UMLPackage.Literals.MERGE_NODE);
		String edge1guardExpr = edge1guard.getBodies().get(0);
		String edge2guardExpr = edge2guard.getBodies().get(0);
		
		assertTrue(
				(edge1guardExpr.equals("true and (1 > 0)") && edge2guardExpr.equals("else")) ||
				(edge1guardExpr.equals("else") && edge2guardExpr.equals("true and (1 > 0)"))
			);
		
		/*If(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> Action.send(new Class1(),new Signal1(ModelInt.ZERO, ModelBool.FALSE,ModelString.EMPTY))
		)
		 */
		ActionImporter.importIfStatement(
				new TestModelAccessories().cond, 
				new TestModelAccessories().body1
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 12);
		
		decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision2");
		
		assertEquals(decisionNode.getIncomings().size(),1);
		assertEquals(decisionNode.getOutgoings().size(),2);
		
		edge1 = decisionNode.getOutgoings().get(0);
		edge1guard = (OpaqueExpression) edge1.getGuard();
		edge1guardExpr = edge1guard.getBodies().get(0);
		edge2 = decisionNode.getOutgoings().get(1);
		edge2guard = (OpaqueExpression) edge2.getGuard();
		edge2guardExpr = edge2guard.getBodies().get(0);
		
		boolean cond1 = edge1guardExpr.equals("true and (1 > 0)") &&
						edge2guardExpr.equals("else") && 
						edge1.getTarget().eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION) &&
						edge2.getTarget().eClass().equals(UMLPackage.Literals.MERGE_NODE);
		
		boolean cond2 = edge2guardExpr.equals("true and (1 > 0)") &&
					edge1guardExpr.equals("else") && 
					edge2.getTarget().eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION) &&
					edge1.getTarget().eClass().equals(UMLPackage.Literals.MERGE_NODE);
		
		assertTrue( cond1 || cond2);
	}

	@Test
	public void testImportIfStatementConditionBlockBodyBlockBody() {
		/*If(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> {},
			() -> {}
		)
		*/
		ActionImporter.importIfStatement(
				new TestModelAccessories().cond, 
				() -> {},
				() -> {}
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 7);
		
		DecisionNode decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision1");
		
		assertEquals(decisionNode.getIncomings().size(),1);
		assertEquals(decisionNode.getOutgoings().size(),2);
		
		ActivityEdge edge1 = decisionNode.getOutgoings().get(0);
		OpaqueExpression edge1guard = (OpaqueExpression) edge1.getGuard();
		ActivityEdge edge2 = decisionNode.getOutgoings().get(1);
		OpaqueExpression edge2guard = (OpaqueExpression) edge2.getGuard();
		assertEquals(edge1.getTarget().eClass(), UMLPackage.Literals.MERGE_NODE);
		assertEquals(edge2.getTarget().eClass(), UMLPackage.Literals.MERGE_NODE);
		
		String edge1guardExpr = edge1guard.getBodies().get(0);
		String edge2guardExpr = edge2guard.getBodies().get(0);
		
		assertTrue(
				(edge1guardExpr.equals("true and (1 > 0)") && edge2guardExpr.equals("else")) ||
				(edge1guardExpr.equals("else") && edge2guardExpr.equals("true and (1 > 0)"))
			);
		
		/*If(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> {}
			() -> Action.send(new Class1(),new Signal1(ModelInt.ZERO, ModelBool.FALSE,ModelString.EMPTY))
		)
		 */
		ActionImporter.importIfStatement(
				new TestModelAccessories().cond, 
				() -> {},
				new TestModelAccessories().body1
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 12);
		
		decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision2");
		
		assertEquals(decisionNode.getIncomings().size(),1);
		assertEquals(decisionNode.getOutgoings().size(),2);
		
		edge1 = decisionNode.getOutgoings().get(0);
		edge1guard = (OpaqueExpression) edge1.getGuard();
		edge1guardExpr = edge1guard.getBodies().get(0);
		edge2 = decisionNode.getOutgoings().get(1);
		edge2guard = (OpaqueExpression) edge2.getGuard();
		edge2guardExpr = edge2guard.getBodies().get(0);
		
		boolean cond1 = edge1guardExpr.equals("true and (1 > 0)") &&
						edge2guardExpr.equals("else") && 
						edge1.getTarget().eClass().equals(UMLPackage.Literals.MERGE_NODE) &&
						edge2.getTarget().eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION);

		boolean cond2 = edge1guardExpr.equals("else") && 
						edge2guardExpr.equals("true and (1 > 0)") &&
						edge1.getTarget().eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION) &&
						edge2.getTarget().eClass().equals(UMLPackage.Literals.MERGE_NODE)
						;

		assertTrue( cond1 || cond2);
		
		/*If(
			() -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO),
			() -> Action.send(new Class1(),new Signal1(ModelInt.ZERO, ModelBool.FALSE,ModelString.EMPTY)),
			() -> Action.delete(instance)
		)*/
		
		TestModel.Class1 instance = (TestModel.Class1) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance, InstanceInformation.create("instance"));
		ActionImporter.importIfStatement(
				new TestModelAccessories().cond, 
				new TestModelAccessories().body1,
				new TestModelAccessories().body2(instance)
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 18);
		
		decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision3");
		
		assertEquals(decisionNode.getIncomings().size(),1);
		assertEquals(decisionNode.getOutgoings().size(),2);
		
		edge1 = decisionNode.getOutgoings().get(0);
		edge1guard = (OpaqueExpression) edge1.getGuard();
		edge1guardExpr = edge1guard.getBodies().get(0);
		edge2 = decisionNode.getOutgoings().get(1);
		edge2guard = (OpaqueExpression) edge2.getGuard();
		edge2guardExpr = edge2guard.getBodies().get(0);
		
		cond1 = edge1guardExpr.equals("true and (1 > 0)") &&
				edge2guardExpr.equals("else") && 
				edge1.getTarget().eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION) &&
				edge2.getTarget().eClass().equals(UMLPackage.Literals.DESTROY_OBJECT_ACTION);

		cond2 = edge1guardExpr.equals("else") && 
				edge2guardExpr.equals("true and (1 > 0)") &&
				edge1.getTarget().eClass().equals(UMLPackage.Literals.DESTROY_OBJECT_ACTION) &&
				edge2.getTarget().eClass().equals(UMLPackage.Literals.CREATE_OBJECT_ACTION);
			
		assertTrue( cond1 || cond2);
	}

	@Test
	public void testImportForStatement() {
		TestModel.Class1 instance = (TestModel.Class1) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance, InstanceInformation.create("instance"));
		/*
		 * For(ModelInt.ZERO, new ModelInt(5), i -> Action.send(instance, new Signal1(i, ModelBool.FALSE, ModelString.EMPTY));
		 */
		ActionImporter.importForStatement(
				ModelInt.ZERO, 
				new TestModelAccessories().forTo(), 
				new TestModelAccessories().forBody(instance)
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(),9);
		
		DecisionNode decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision1");
		
		assertEquals(decisionNode.getIncomings().size(),2);
		assertEquals(decisionNode.getOutgoings().size(),1);
		
		assertEquals(decisionNode.getIncomings().get(0).getSource().eClass(),UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		assertEquals(decisionNode.getIncomings().get(1).getSource().eClass(),UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		ActivityEdge edge = decisionNode.getOutgoings().get(0);
		OpaqueExpression edgeGuard = (OpaqueExpression) edge.getGuard();
	
		assertEquals(edge.getTarget().eClass(), UMLPackage.Literals.SEND_SIGNAL_ACTION);
		
		String edgeGuardExpr = edgeGuard.getBodies().get(0);
		
		assertTrue(edgeGuardExpr.startsWith("inst_"));
		assertTrue(edgeGuardExpr.endsWith("<=5"));
		assertEquals(ActionImporter.unfinishedDecisionNodes.peek(), decisionNode);

		/*
		 * For(ModelInt.ZERO, new ModelInt(5), i -> {});
		 */
		
		ActionImporter.importForStatement(
				ModelInt.ZERO, 
				new TestModelAccessories().forTo(), 
				i -> {}
			);
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(),12);
		
		assertEquals(decisionNode.getOutgoings().size(),2);
		edge = decisionNode.getOutgoings().get(1);
		edgeGuard = (OpaqueExpression) edge.getGuard();
		edgeGuardExpr = edgeGuard.getBodies().get(0);
		assertEquals(edgeGuardExpr, "else");
		
		decisionNode = (DecisionNode) ActionImporter.currentActivity.getOwnedNode("decision2");
		
		assertEquals(decisionNode.getIncomings().size(),2);
		assertEquals(decisionNode.getOutgoings().size(),1);
		
		assertEquals(decisionNode.getIncomings().get(0).getSource().eClass(),UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		assertEquals(decisionNode.getIncomings().get(1).getSource().eClass(),UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		edge = decisionNode.getOutgoings().get(0);
		edgeGuard = (OpaqueExpression) edge.getGuard();
	
		assertEquals(edge.getTarget().eClass(), UMLPackage.Literals.ADD_VARIABLE_VALUE_ACTION);
		
		edgeGuardExpr = edgeGuard.getBodies().get(0);
		
		assertTrue(edgeGuardExpr.startsWith("inst_"));
		assertTrue(edgeGuardExpr.endsWith("<=5"));
		assertEquals(ActionImporter.unfinishedDecisionNodes.peek(), decisionNode);	
	}

	@Test
	public void testImportDeleteObjectAction() {
		ModelClass instance = (ModelClass) DummyInstanceCreator.createDummyInstance(txtUMLOwnerClass);
		InstanceManager.createLocalInstancesMapEntry(instance, InstanceInformation.create("instance"));
		ActionImporter.importDeleteObjectAction(instance);

		// initial and final nodes, 3 parameter nodes (2 args and a return) + a start classifier behavior action
		assertEquals(ActionImporter.currentActivity.getOwnedNodes().size(), 6); 
		DestroyObjectAction destroyAction=	(DestroyObjectAction) 
				ActionImporter.currentActivity.getOwnedNode("delete_instance");
		
		ValuePin valuePin = (ValuePin) destroyAction.getTarget();

		OpaqueExpression opaqueExpr = (OpaqueExpression) valuePin.getValue();
		
		assertEquals(opaqueExpr.getBodies().get(0),"instance"); //started instance is "instance"
		assertEquals(destroyAction.getIncomings().size(),1); //1 incoming edge to start classifier behavior action
		assertEquals(destroyAction.getOutgoings().size(),0); //0 outgoing edge from start classifier behavior action yet
	}

	class TestModel extends Model
	{
		class Signal1 extends Signal
		{
			Signal1(ModelInt f1, ModelBool f2, ModelString f3)
			{
				field1 = f1;
				field2 = f2;
				field3 = f3;
			}
			ModelInt field1;
			ModelBool field2;
			ModelString field3;
		}
		
		class Class1 extends ModelClass
		{
			ModelBool method(ModelInt arg0, ModelInt arg1)
			{
				return ModelBool.FALSE;
			}
		}
		
		class Assoc extends Association
		{
			class End1 extends One<Class1> {}
			class End2 extends One<Class1> {}
		}
	}
	
	//needed for test methods because of AspectJ weaving
	class TestModelAccessories extends Model
	{
		Condition cond = () -> ModelBool.TRUE.and(ModelInt.ONE.isMore(ModelInt.ZERO));
		BlockBody body1 = () -> Action.send(
				new TestModel().new Class1(),
				new TestModel().new Signal1(ModelInt.ZERO, ModelBool.FALSE,ModelString.EMPTY)
			);
		BlockBody body2(TestModel.Class1 instance)
		{
			return () -> Action.delete(instance);
		}
		ParameterizedBlockBody<ModelInt> forBody(TestModel.Class1 instance)
		{
			return i -> Action.send(instance, new TestModel().new Signal1(i,ModelBool.FALSE,ModelString.EMPTY));
		}
		ModelInt forTo()
		{
			return new ModelInt(5);
		}
	}
}
