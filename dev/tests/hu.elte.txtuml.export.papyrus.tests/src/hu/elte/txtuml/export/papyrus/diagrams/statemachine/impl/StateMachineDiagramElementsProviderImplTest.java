package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.diagrams.common.layout.IDiagramElementsMapper;
import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class StateMachineDiagramElementsProviderImplTest {

	private static final String CLASS_OF_STATEMACHINE = "Class1";

	private StateMachine sm;
	private Region mainRegion;
	private State compositeState;
	private Region compositeRegion;

	private StateMachineDiagramElementsProviderImpl instance;

	private State state1;
	private Pseudostate initial1;
	private Transition trans1;
	private Transition trans2;
	private Pseudostate initial2;
	private State state2;
	private Transition trans3;

	@Before
	public void setUp() throws Exception {
		sm = UMLFactory.eINSTANCE.createStateMachine();
		mainRegion = sm.createRegion("mainRegion");

		initial1 = (Pseudostate) mainRegion.createSubvertex("Initial Pseudostate",
				UMLPackage.eINSTANCE.getPseudostate());
		initial1.setKind(PseudostateKind.INITIAL_LITERAL);

		state1 = (State) mainRegion.createSubvertex("Simple State", UMLPackage.eINSTANCE.getState());

		compositeState = (State) mainRegion.createSubvertex("Simple State", UMLPackage.eINSTANCE.getState());
		compositeRegion = compositeState.createRegion("CompositeRegion");

		trans1 = mainRegion.createTransition("Transition");
		trans1.setSource(initial1);
		trans1.setTarget(compositeState);

		trans2 = mainRegion.createTransition("Transition");
		trans2.setSource(compositeState);
		trans2.setTarget(state1);

		initial2 = (Pseudostate) compositeRegion.createSubvertex("Initial Pseudostate",
				UMLPackage.eINSTANCE.getPseudostate());
		initial2.setKind(PseudostateKind.INITIAL_LITERAL);

		state2 = (State) compositeRegion.createSubvertex("Simple State", UMLPackage.eINSTANCE.getState());

		trans3 = compositeRegion.createTransition("Transition");
		trans3.setSource(initial2);
		trans3.setTarget(state2);

		// create instance
		IDiagramElementsMapper mapper = mock(IDiagramElementsMapper.class);
		when(mapper.getNodes()).thenReturn(Arrays.asList(initial1, initial2, state1, state2, compositeState));
		when(mapper.getConnections()).thenReturn(Arrays.asList(trans1, trans2, trans3));
		Class class1 = UMLFactory.eINSTANCE.createClass();
		class1.setClassifierBehavior(sm);
		when(mapper.findNode(CLASS_OF_STATEMACHINE)).thenReturn(class1);

		DiagramExportationReport report = new DiagramExportationReport();
		report.setReferencedElementName(CLASS_OF_STATEMACHINE);
		instance = new StateMachineDiagramElementsProviderImpl(report, mapper);
	}

	@Test
	public void testGetStatesForMainRegion() {
		// when
		Collection<State> result = instance.getStatesForRegion(mainRegion);

		// then
		assertThat(result, hasItems((State) compositeState, (State) state1));
	}

	@Test
	public void testGetStatesForCompositeRegion() {
		// when
		Collection<State> result = instance.getStatesForRegion(compositeRegion);

		// then
		assertThat(result, hasItems((State) state2));
	}

	@Test
	public void testGetMainRegions() {
		// when
		Collection<Region> result = instance.getMainRegions();

		// then
		assertThat(result, hasItems(mainRegion));
	}

	@Test
	public void testGetRegionsOfCompositeState() {
		// when
		Collection<Region> result = instance.getRegionsOfState(compositeState);

		// then
		assertThat(result, hasItems(compositeRegion));
	}

	@Test
	public void testGetInitialStatesForMainRegion() {
		// when
		Collection<Pseudostate> result = instance.getInitialStatesForRegion(mainRegion);

		// then
		assertThat(result, hasItems((Pseudostate) initial1));
	}

	@Test
	public void testGetInitialStatesForCompositeRegion() {
		// when
		Collection<Pseudostate> result = instance.getInitialStatesForRegion(compositeRegion);

		// then
		assertThat(result, hasItems((Pseudostate) initial2));
	}

	@Test
	public void testGetTransitionsForMainRegion() {
		// when
		Collection<Transition> result = instance.getTransitionsForRegion(mainRegion);

		// then
		assertThat(result, hasItems(trans1, trans2));
	}

	@Test
	public void testGetTransitionsForCompositeRegion() {
		// when
		Collection<Transition> result = instance.getTransitionsForRegion(compositeRegion);

		// then
		assertThat(result, hasItems(trans3));
	}
}
