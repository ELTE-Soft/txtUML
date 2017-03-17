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
		org.eclipse.uml2.uml.State s = UMLFactory.eINSTANCE.createState();
		s.setContainer(region);
		s.setName("S1");

		State state = new State(s, "package.A.S1");

		Assert.assertEquals("package.A.S1", state.getId());
		Assert.assertEquals("S1", state.getName());

		state.setLayout(new Rectangle(1, 2, 3, 4));

		Assert.assertEquals(1, state.getPosition().getX());
		Assert.assertEquals(2, state.getPosition().getY());
		Assert.assertEquals((Integer) 3, state.getWidth());
		Assert.assertEquals((Integer) 4, state.getHeight());

	}

	@Test
	public void testPseudoState() {
		org.eclipse.uml2.uml.Pseudostate i = UMLFactory.eINSTANCE.createPseudostate();
		i.setContainer(region);
		i.setName("I");
		i.setKind(PseudostateKind.INITIAL_LITERAL);

		org.eclipse.uml2.uml.Pseudostate c = UMLFactory.eINSTANCE.createPseudostate();
		c.setContainer(region);
		c.setName("C");
		c.setKind(PseudostateKind.CHOICE_LITERAL);

		PseudoState statec = new PseudoState(c, "package.A.C");
		PseudoState statei = new PseudoState(i, "package.A.I");

		Assert.assertEquals("package.A.I", statei.getId());
		Assert.assertEquals("package.A.C", statec.getId());

		Assert.assertEquals("I", statei.getName());
		Assert.assertEquals("C", statec.getName());

		statec.setLayout(new Rectangle(1, 2, 3, 4));

		Assert.assertEquals(1, statec.getPosition().getX());
		Assert.assertEquals(2, statec.getPosition().getY());
		Assert.assertEquals((Integer) 3, statec.getWidth());
		Assert.assertEquals((Integer) 4, statec.getHeight());

		Assert.assertEquals(PseudostateKind.INITIAL_LITERAL, statei.getKind());
		Assert.assertEquals(PseudostateKind.CHOICE_LITERAL, statec.getKind());

	}

	@Test
	public void testTransition() {
		LineAssociation la = new LineAssociation("package.A.S1S2", "package.A.S1", "package.A.S2");

		org.eclipse.uml2.uml.State s1 = UMLFactory.eINSTANCE.createState();
		s1.setContainer(region);
		s1.setName("S1");

		org.eclipse.uml2.uml.State s2 = UMLFactory.eINSTANCE.createState();
		s2.setContainer(region);
		s2.setName("S1");

		org.eclipse.uml2.uml.Transition t = UMLFactory.eINSTANCE.createTransition();
		t.setContainer(region);

		Transition transition = new Transition(la, t);

		Assert.assertEquals("package.A.S1S2", transition.getId());
		Assert.assertEquals("package.A.S1", transition.getFromID());
		Assert.assertEquals("package.A.S2", transition.getToID());
		Assert.assertEquals(null, transition.getTrigger());

		Event event = UMLFactory.eINSTANCE.createSignalEvent();
		event.setName("Sig1");
		t.createTrigger("trigger").setEvent(event);

		transition = new Transition(la, t);

		Assert.assertEquals("Sig1", transition.getTrigger());

	}

}
