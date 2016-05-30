package hu.elte.txtuml.export.uml2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Clause;
import org.eclipse.uml2.uml.ConditionalNode;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.ExecutableNode;
import org.eclipse.uml2.uml.ExpansionNode;
import org.eclipse.uml2.uml.ExpansionRegion;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LoopNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.SequenceNode;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.ValueSpecificationAction;
import org.eclipse.uml2.uml.Vertex;

public class UMLExportTestBase {

	public UMLExportTestBase() {
		super();
	}

	protected Model model(String name) throws Exception {
		Model model = ModelExportTestUtils.export(name);
		assertNotNull(model);
		return model;
	}

	protected Class cls(Model model, String name) {
		Class cls = (Class) model.getMember(name);
		assertNotNull(cls);
		return cls;
	}

	protected Association assoc(org.eclipse.uml2.uml.Model model, String name) {
		Association assoc = (Association) model.getMember(name);
		assertNotNull(assoc);
		return assoc;
	}
	
	protected Port port(Class cls, String name) {
		Port port = (Port) cls.getOwnedPort(name, null);
		assertNotNull(port);
		return port;
	}
	
	protected Interface getProvided(Port port) {
		Interface actualIface = port.getProvideds().get(0);
		Interface formalIface = (Interface) actualIface.getGenerals().get(0);
		assertNotNull(formalIface);
		return formalIface;
	}

	protected Property assocEnd(Class a, Classifier ab, String name, AggregationKind kind, int lowerBound, int upperBound,
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

	protected DataType dataType(Model model, String name) {
		DataType dataType = (DataType) model.getMember(name);
		assertNotNull(dataType);
		return dataType;
	}

	protected Property property(Classifier cls, String name, String typeName) {
		Property attribute = cls.getAttribute(name, null);
		assertNotNull(attribute);
		assertEquals(typeName, attribute.getType().getName());
		assertEquals(1, attribute.getLower());
		assertEquals(1, attribute.getUpper());
		assertEquals(true, attribute.isNavigable());
		return attribute;
	}

	protected Operation operation(Classifier cls, String name) {
		Operation operation = cls.getOperation(name, null, null);
		assertNotNull(operation);
		return operation;
	}

	@SuppressWarnings("unchecked")
	protected <T> T node(StructuredActivityNode parent, int index, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getNodes().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@SuppressWarnings("unchecked")
	protected <T> T node(Activity parent, int index, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getNodes().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}
	
	protected void checkDefaultCtor(SequenceNode body) {
		SequenceNode createNode = (SequenceNode) body.getNode("create DefaultConstructible;");
		SequenceNode createExprNode = (SequenceNode) createNode.getNode("create DefaultConstructible");
		node(createExprNode, 0, "instantiate DefaultConstructible", CreateObjectAction.class);
		node(createExprNode, 1, "#temp=instantiate DefaultConstructible", AddVariableValueAction.class);
		node(createExprNode, 2, "#temp", ReadVariableAction.class);
		node(createExprNode, 3, "#temp.DefaultConstructible()", CallOperationAction.class);
		node(createExprNode, 4, "#temp", ReadVariableAction.class);
	}

	protected void checkParameteredCtor(SequenceNode body) {
		SequenceNode createNode = (SequenceNode) body.getNode("create ClassWithCtors;");
		SequenceNode createExprNode = (SequenceNode) createNode.getNode("create ClassWithCtors");
		node(createExprNode, 0, "instantiate ClassWithCtors", CreateObjectAction.class);
		node(createExprNode, 1, "#temp=instantiate ClassWithCtors", AddVariableValueAction.class);
		node(createExprNode, 2, "#temp", ReadVariableAction.class);
		node(createExprNode, 3, "1", ValueSpecificationAction.class);
		node(createExprNode, 4, "#temp.ClassWithCtors(Integer p0)", CallOperationAction.class);
		node(createExprNode, 5, "#temp", ReadVariableAction.class);
	}

	protected void checkParameterlessCtor(SequenceNode body) {
		SequenceNode createNode = (SequenceNode) body.getNode("create ClassWithCtors;");
		SequenceNode createExprNode = (SequenceNode) createNode.getNode("create ClassWithCtors");
		node(createExprNode, 0, "instantiate ClassWithCtors", CreateObjectAction.class);
		node(createExprNode, 1, "#temp=instantiate ClassWithCtors", AddVariableValueAction.class);
		node(createExprNode, 2, "#temp", ReadVariableAction.class);
		node(createExprNode, 3, "#temp.ClassWithCtors()", CallOperationAction.class);
		node(createExprNode, 4, "#temp", ReadVariableAction.class);
	}

	@SuppressWarnings("unchecked")
	protected <T> T loopBody(LoopNode parent, int index, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getBodyParts().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@SuppressWarnings("unchecked")
	protected <T> T loopCond(LoopNode parent, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getTests().get(0);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	@SuppressWarnings("unchecked")
	protected <T> T loopSetup(LoopNode parent, String name, java.lang.Class<T> type) {
		ActivityNode node = parent.getSetupParts().get(0);
		assertEquals(name, node.getName());
		assertNotNull(node);
		assertTrue("Node is not a " + type.getName(), type.isInstance(node));
		return (T) node;
	}

	protected ExpansionNode inputElement(ExpansionRegion parent, int index, String name) {
		ExpansionNode node = parent.getInputElements().get(index);
		assertEquals(name, node.getName());
		assertNotNull(node);
		return node;
	}

	protected void checkSubclass(Class sub, Class generic) {
		assertNotNull(sub.getGeneralization(generic));
	}

	protected void overrides(Operation overrider, Operation overridden) {
		assertTrue(overrider.getRedefinedOperations().contains(overridden));
	}

	@SuppressWarnings("unchecked")
	protected <T> T clauseTest(ConditionalNode condNode, int index, String name, java.lang.Class<T> type) {
		Clause clause = condNode.getClauses().get(index);
		assertNotNull(clause);
		ExecutableNode testNode = clause.getTests().get(0);
		assertNotNull(testNode);
		assertEquals(name, testNode.getName());
		assertTrue("Node is not a " + type.getName(), type.isInstance(testNode));
		return (T) testNode;
	}

	@SuppressWarnings("unchecked")
	protected <T> T clauseBody(ConditionalNode condNode, int index, String name, java.lang.Class<T> type) {
		Clause clause = condNode.getClauses().get(index);
		assertNotNull(clause);
		ExecutableNode testNode = clause.getBodies().get(0);
		assertNotNull(testNode);
		assertEquals(name, testNode.getName());
		assertTrue("Node is not a " + type.getName(), type.isInstance(testNode));
		return (T) testNode;
	}

	protected Signal signal(Model model, String name) {
		Optional<NamedElement> sig = (Optional<NamedElement>) model.getMembers().stream()
				.filter(m -> m.getName().equals(name) && m instanceof Signal).findFirst();
		assertTrue(sig.isPresent());
		return (Signal) sig.get();
	}

	protected SignalEvent signalEvent(Model model, String name) {
		Optional<NamedElement> sig = (Optional<NamedElement>) model.getMembers().stream()
				.filter(m -> m.getName().equals(name) && m instanceof SignalEvent).findFirst();
		assertTrue(sig.isPresent());
		return (SignalEvent) sig.get();
	}

	protected Transition transition(Region reg, Vertex v1, Vertex v2, Event event) {
		Optional<Transition> trans = reg.getTransitions().stream().filter(s -> s.getSource().equals(v1) && s.getTarget().equals(v2)).findFirst();
		if (event != null) {
			assertEquals(event, trans.get().getTriggers().get(0).getEvent());
		}
		return trans.get();
	}

	protected Region region(Class cls) {
		StateMachine sm = (StateMachine) cls.getClassifierBehavior();
		Region reg = sm.getRegions().get(0);
		assertNotNull(reg);
		return reg;
	}

	protected Pseudostate pseudoState(Region reg, String name, PseudostateKind kind) {
		Optional<Vertex> vertex = reg.getSubvertices().stream().filter(s -> s.getName().equals(name)).findFirst();
		assertTrue(vertex.isPresent());
		assertTrue("The vertex is not a PseudoState", vertex.get() instanceof Pseudostate);
		assertEquals(kind, ((Pseudostate) vertex.get()).getKind());
		return (Pseudostate) vertex.get();
	}

	protected State state(Region reg, String name) {
		Optional<Vertex> vertex = reg.getSubvertices().stream().filter(s -> s.getName().equals(name)).findFirst();
		assertTrue(vertex.isPresent());
		assertTrue("The vertex is not a State", vertex.get() instanceof State);
		return (State) vertex.get();
	}

	protected Activity getGuardActivity(Transition tr2) {
		Constraint guard = tr2.getGuard();
		OpaqueExpression specification = (OpaqueExpression) guard.getSpecification();
		Activity activity = (Activity) specification.getBehavior();
		assertNotNull(activity);
		return activity;
	}

	protected SequenceNode loadActionCode(Model model, String className, String operationName) throws Exception {
		Class cls = cls(model, className);
		Operation op = operation(cls, operationName);
		Activity behav = (Activity) op.getMethod(operationName);
		return getActivityBody(behav);
	}

	protected SequenceNode getActivityBody(Activity behav) {
		assertNotNull(behav);
		SequenceNode bodyNode = (SequenceNode) behav.getOwnedNode("#body");
		assertNotNull(bodyNode);
		return bodyNode;
	}

}