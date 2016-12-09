package hu.elte.txtuml.export.javascript.json.model.smd;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

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

		RectangleObject rect = new RectangleObject("package.A.S1", new Point(1, 2));
		rect.setWidth(3);
		rect.setHeight(4);
		State state = new State(rect, s);

		Assert.assertEquals("package.A.S1", state.getId());
		Assert.assertEquals("S1", state.getName());
		Assert.assertEquals((Integer) 1, state.getPosition().getX());
		Assert.assertEquals((Integer) 2, state.getPosition().getY());
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

		RectangleObject recti = new RectangleObject("package.A.I", new Point(1, 2));
		recti.setWidth(3);
		recti.setHeight(4);

		RectangleObject rectc = new RectangleObject("package.A.C", new Point(5, 6));
		rectc.setWidth(7);
		rectc.setHeight(8);

		PseudoState statec = new PseudoState(rectc, c);
		PseudoState statei = new PseudoState(recti, i);

		Assert.assertEquals("package.A.I", statei.getId());
		Assert.assertEquals("package.A.C", statec.getId());

		Assert.assertEquals("I", statei.getName());
		Assert.assertEquals("C", statec.getName());

		Assert.assertEquals((Integer) 1, statei.getPosition().getX());
		Assert.assertEquals((Integer) 5, statec.getPosition().getX());

		Assert.assertEquals((Integer) 2, statei.getPosition().getY());
		Assert.assertEquals((Integer) 6, statec.getPosition().getY());

		Assert.assertEquals((Integer) 3, statei.getWidth());
		Assert.assertEquals((Integer) 7, statec.getWidth());

		Assert.assertEquals((Integer) 4, statei.getHeight());
		Assert.assertEquals((Integer) 8, statec.getHeight());

		Assert.assertEquals("initial", statei.getKind());
		Assert.assertEquals("choice", statec.getKind());

	}

	@Test
	public void testTransition() {
		LineAssociation la = new LineAssociation("package.A.S1S2", "package.A.S1", "package.A.S2");
		List<Point> route = Arrays.asList(new Point(1, 2), new Point(2, 2), new Point(2, 3), new Point(2, 4));
		List<Point> expectedRoute = Arrays.asList(new Point(2, 3));
		la.setRoute(route);

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

		List<Point> anchors = transition.getAnchors();
		List<Point> expectedAndchors = Arrays.asList(new Point(2, 2), new Point(2, 3));

		Assert.assertEquals(expectedAndchors.size(), anchors.size());
		for (int i = 0; i < expectedAndchors.size(); ++i) {
			Assert.assertEquals(expectedAndchors.get(i), anchors.get(i));
		}

		List<Point> actualRoute = transition.getRoute();

		Assert.assertEquals(expectedRoute.size(), actualRoute.size());
		for (int i = 0; i < expectedRoute.size(); ++i) {
			Assert.assertEquals(expectedRoute.get(i), actualRoute.get(i));
		}

		Event event = UMLFactory.eINSTANCE.createSignalEvent();
		event.setName("Sig1");
		t.createTrigger("trigger").setEvent(event);

		route = Arrays.asList(new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2), new Point(5, 2));
		expectedRoute = Arrays.asList(new Point(3, 2));
		la.setRoute(route);

		transition = new Transition(la, t);

		Assert.assertEquals("Sig1", transition.getTrigger());

		anchors = transition.getAnchors();
		expectedAndchors = Arrays.asList(new Point(2, 2), new Point(4, 2));

		Assert.assertEquals(expectedAndchors.size(), anchors.size());
		for (int i = 0; i < expectedAndchors.size(); ++i) {
			Assert.assertEquals(expectedAndchors.get(i), anchors.get(i));
		}

		actualRoute = transition.getRoute();

		Assert.assertEquals(expectedRoute.size(), actualRoute.size());
		for (int i = 0; i < expectedRoute.size(); ++i) {
			Assert.assertEquals(expectedRoute.get(i), actualRoute.get(i));
		}

		route = Arrays.asList(new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2));
		expectedRoute = Arrays.asList(new Point(3, 2));
		la.setRoute(route);

		transition = new Transition(la, t);

	}

	@Test
	public void testSMDiagram() {
		LineAssociation lais1 = new LineAssociation("package.A.IS1", "package.A.I", "package.A.S1");
		List<Point> route = Arrays.asList(new Point(2, 2), new Point(3, 2), new Point(4, 2));
		lais1.setRoute(route);

		RectangleObject rectI = new RectangleObject("package.A.I");
		RectangleObject rectS1 = new RectangleObject("package.A.S1");

		org.eclipse.uml2.uml.State s1 = UMLFactory.eINSTANCE.createState();
		s1.setContainer(region);
		s1.setName("S1");

		org.eclipse.uml2.uml.Pseudostate i = UMLFactory.eINSTANCE.createPseudostate();
		i.setContainer(region);
		i.setName("I");
		i.setKind(PseudostateKind.INITIAL_LITERAL);

		org.eclipse.uml2.uml.Transition is1 = UMLFactory.eINSTANCE.createTransition();
		is1.setContainer(region);

		Map<String, EObject> modelMap = new HashMap<String, EObject>();
		modelMap.put("package.A.I", i);
		modelMap.put("package.A.S1", s1);
		modelMap.put("package.A.IS1", is1);

		ModelMapProvider mockMap = mock(ModelMapProvider.class);
		when(mockMap.getByName(anyString())).thenAnswer(new Answer<EObject>() {
			@Override
			public EObject answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return modelMap.get((String) args[0]);
			}
		});

		Set<RectangleObject> nodes = new HashSet<RectangleObject>(Arrays.asList(rectS1, rectI));
		Set<LineAssociation> links = new HashSet<LineAssociation>(Arrays.asList(lais1));

		SMDiagram smd = new SMDiagram("package.diagram", nodes, links, mockMap, 0.5);

		Assert.assertEquals("A", smd.getMachineName());
		Assert.assertEquals("package.diagram", smd.getName());

		Assert.assertEquals(1, smd.getPseudoStates().size());
		Assert.assertEquals("package.A.I", smd.getPseudoStates().get(0).getId());
		Assert.assertEquals("I", smd.getPseudoStates().get(0).getName());

		Assert.assertEquals(1, smd.getStates().size());
		Assert.assertEquals("package.A.S1", smd.getStates().get(0).getId());
		Assert.assertEquals("S1", smd.getStates().get(0).getName());

		Assert.assertEquals(1, smd.getTransitions().size());
		Assert.assertEquals("package.A.IS1", smd.getTransitions().get(0).getId());
		Assert.assertEquals(null, smd.getTransitions().get(0).getTrigger());

		Assert.assertEquals(0.5, smd.getSpacing(), 0.001);
	}

}
