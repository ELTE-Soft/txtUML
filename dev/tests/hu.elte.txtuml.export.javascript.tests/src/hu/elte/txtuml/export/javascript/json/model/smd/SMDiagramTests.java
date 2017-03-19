package hu.elte.txtuml.export.javascript.json.model.smd;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.diagrams.common.Rectangle;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

public class SMDiagramTests {
	private Model model;
	private Region region;

	@Before
	public void prepare() {
		model = UMLFactory.eINSTANCE.createModel();
		Class classA = model.createOwnedClass("A", false);
		StateMachine sm = (StateMachine) classA.createOwnedBehavior("ASM", UMLPackage.Literals.STATE_MACHINE);
		region = sm.createRegion("A");
	}

	@Test
	public void testState() {
		// given
		org.eclipse.uml2.uml.State s = UMLFactory.eINSTANCE.createState();
		s.setContainer(region);
		s.setName("S1");

		// when
		State state = new State(s, "package.A.S1");
		state.setLayout(new Rectangle(1, 2, 3, 4));

		// then
		Assert.assertEquals("package.A.S1", state.getId());
		Assert.assertEquals("S1", state.getName());
		Assert.assertEquals(1, state.getPosition().getX());
		Assert.assertEquals(2, state.getPosition().getY());
		Assert.assertEquals(3, state.getWidth().intValue());
		Assert.assertEquals(4, state.getHeight().intValue());
	}

	@Test
	public void testPseudoState() {
		// given
		org.eclipse.uml2.uml.Pseudostate inintialState = UMLFactory.eINSTANCE.createPseudostate();
		inintialState.setContainer(region);
		inintialState.setName("I");
		inintialState.setKind(PseudostateKind.INITIAL_LITERAL);

		org.eclipse.uml2.uml.Pseudostate choiceState = UMLFactory.eINSTANCE.createPseudostate();
		choiceState.setContainer(region);
		choiceState.setName("C");
		choiceState.setKind(PseudostateKind.CHOICE_LITERAL);

		// when
		PseudoState statec = new PseudoState(choiceState, "package.A.C");
		PseudoState statei = new PseudoState(inintialState, "package.A.I");
		statec.setLayout(new Rectangle(1, 2, 3, 4));

		// then
		Assert.assertEquals("package.A.I", statei.getId());
		Assert.assertEquals("package.A.C", statec.getId());

		Assert.assertEquals("I", statei.getName());
		Assert.assertEquals("C", statec.getName());

		Assert.assertEquals(PseudostateKind.INITIAL_LITERAL, statei.getKind());
		Assert.assertEquals(PseudostateKind.CHOICE_LITERAL, statec.getKind());

		Assert.assertEquals(1, statec.getPosition().getX());
		Assert.assertEquals(2, statec.getPosition().getY());
		Assert.assertEquals(3, statec.getWidth().intValue());
		Assert.assertEquals(4, statec.getHeight().intValue());
	}

	@Test
	public void testTransition() {
		// given
		LineAssociation la = new LineAssociation("package.A.S1S2", "package.A.S1", "package.A.S2");

		org.eclipse.uml2.uml.State s1 = UMLFactory.eINSTANCE.createState();
		s1.setContainer(region);
		s1.setName("S1");

		org.eclipse.uml2.uml.State s2 = UMLFactory.eINSTANCE.createState();
		s2.setContainer(region);
		s2.setName("S1");

		org.eclipse.uml2.uml.Transition t1 = UMLFactory.eINSTANCE.createTransition();
		t1.setContainer(region);

		org.eclipse.uml2.uml.Transition t2 = UMLFactory.eINSTANCE.createTransition();
		t2.setContainer(region);

		Event event = UMLFactory.eINSTANCE.createSignalEvent();
		event.setName("Sig1");
		t2.createTrigger("trigger").setEvent(event);

		// when
		Transition transition1 = new Transition(la, t1);
		Transition transition2 = new Transition(la, t2);

		// then
		Assert.assertEquals("package.A.S1S2", transition1.getId());
		Assert.assertEquals("package.A.S1", transition1.getFromID());
		Assert.assertEquals("package.A.S2", transition1.getToID());

		Assert.assertEquals("package.A.S1S2", transition2.getId());
		Assert.assertEquals("package.A.S1", transition2.getFromID());
		Assert.assertEquals("package.A.S2", transition2.getToID());

		Assert.assertEquals(null, transition1.getTrigger());
		Assert.assertEquals("Sig1", transition2.getTrigger());
	}
}
