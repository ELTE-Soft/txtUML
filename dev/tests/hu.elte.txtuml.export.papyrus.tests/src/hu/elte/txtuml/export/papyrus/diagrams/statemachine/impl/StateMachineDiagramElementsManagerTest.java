package hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.notation.Diagram;
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

import hu.elte.txtuml.export.papyrus.arrange.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.StateMachineDiagramNotationManager;

public class StateMachineDiagramElementsManagerTest {

	private StateMachineDiagramElementsManager instance;
	private Diagram diagram;
	private StateMachineDiagramElementsProvider provider;
	private StateMachineDiagramNotationManager notation;
	private IDiagramElementsArranger arranger;
	private IProgressMonitor monitor;
	private Region mainRegion;

	@Before
	public void setUp() throws Exception {
		this.diagram = mock(Diagram.class);
		this.provider = mock(StateMachineDiagramElementsProvider.class);
		this.notation = mock(StateMachineDiagramNotationManager.class);
		this.arranger = mock(IDiagramElementsArranger.class);
		this.monitor = new NullProgressMonitor();
		instance = new StateMachineDiagramElementsManager(diagram, provider, notation, arranger, monitor);

		// given for all testcases
		StateMachine sm = UMLFactory.eINSTANCE.createStateMachine();
		mainRegion = sm.createRegion("MainRegion");
		when(provider.getMainRegions()).thenReturn(Arrays.asList(mainRegion));
	}

	@Test
	public void testGetMainRegions() {
		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getMainRegions();
		verify(provider).getInitialStatesForRegion(mainRegion);
		verify(provider).getStatesForRegion(mainRegion);
	}

	@Test
	public void testInitialState() {
		// given
		Pseudostate initialState = mock(Pseudostate.class);
		when(provider.getInitialStatesForRegion(mainRegion)).thenReturn(Arrays.asList(initialState));

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getInitialStatesForRegion(mainRegion);
		verify(notation).createInitialStateForRegion(eq(mainRegion), eq(initialState), any(), eq(monitor));
	}

	@Test
	public void testFlatStateMachineWithBounds() {
		// given
		Pseudostate initialState = mock(Pseudostate.class);
		State state1 = mock(State.class);
		State state2 = mock(State.class);
		when(state1.isSimple()).then(i -> true);
		when(state2.isSimple()).then(i -> true);

		Rectangle boundsForInitial = new Rectangle(20, 20, 20, 20);
		Rectangle boundsForState1 = new Rectangle(50, 20, 100, 100);
		Rectangle boundsForState2 = new Rectangle(250, 20, 100, 100);

		when(provider.getInitialStatesForRegion(mainRegion)).thenReturn(Arrays.asList(initialState));
		when(provider.getStatesForRegion(mainRegion)).thenReturn(Arrays.asList(state1, state2));
		when(arranger.getBoundsForElement(initialState)).thenReturn(boundsForInitial);
		when(arranger.getBoundsForElement(state1)).thenReturn(boundsForState1);
		when(arranger.getBoundsForElement(state2)).thenReturn(boundsForState2);

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getInitialStatesForRegion(mainRegion);
		verify(notation).createInitialStateForRegion(mainRegion, initialState, boundsForInitial, monitor);
		verify(provider).getStatesForRegion(mainRegion);
		verify(notation).createStateForRegion(mainRegion, state1, boundsForState1, monitor);
		verify(notation).createStateForRegion(mainRegion, state2, boundsForState2, monitor);
	}

	@Test
	public void testAddTransitionToDiagram() {
		// given
		Pseudostate initial = (Pseudostate) mainRegion.createSubvertex("Initial Pseudostate",
				UMLPackage.eINSTANCE.getPseudostate());
		initial.setKind(PseudostateKind.INITIAL_LITERAL);
		State state = (State) mainRegion.createSubvertex("Simple State", UMLPackage.eINSTANCE.getState());
		Transition trans = mainRegion.createTransition("Transition");
		trans.setSource(initial);
		trans.setTarget(state);
		when(provider.getInitialStatesForRegion(mainRegion)).thenReturn(Arrays.asList(initial));
		when(provider.getTransitionsForRegion(mainRegion)).thenReturn(Arrays.asList(trans));
		when(provider.getStatesForRegion(mainRegion)).thenReturn(Arrays.asList(state));

		// when
		instance.addElementsToDiagram();

		// then
		verify(notation).createTransitionForRegion(eq(mainRegion), eq(initial), eq(state), eq(trans),
				anyListOf(Point.class), anyString(), anyString(), eq(monitor));
	}
	
	@Test
	public void testCompositeState() {
		// given
		State compositeState = (State) mainRegion.createSubvertex("Simple State", UMLPackage.eINSTANCE.getState());
		Region compositeRegion = compositeState.createRegion("CompositeRegion");
		
		Pseudostate initial = (Pseudostate) compositeRegion.createSubvertex("Initial Pseudostate",
				UMLPackage.eINSTANCE.getPseudostate());
		initial.setKind(PseudostateKind.INITIAL_LITERAL);
		State state = (State) compositeRegion.createSubvertex("Simple State", UMLPackage.eINSTANCE.getState());
		
		when(provider.getInitialStatesForRegion(mainRegion)).thenReturn(Arrays.asList());
		when(provider.getStatesForRegion(mainRegion)).thenReturn(Arrays.asList(compositeState));
		when(provider.getRegionsOfState(compositeState)).thenReturn(Arrays.asList(compositeRegion));
		when(provider.getInitialStatesForRegion(compositeRegion)).thenReturn(Arrays.asList(initial));
		when(provider.getStatesForRegion(compositeRegion)).thenReturn(Arrays.asList(state));

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getRegionsOfState(compositeState);
		verify(provider).getInitialStatesForRegion(compositeRegion);
		verify(provider).getStatesForRegion(compositeRegion);
		verify(notation).createInitialStateForRegion(eq(compositeRegion), eq(initial), any(), eq(monitor));
		verify(notation).createStateForRegion(eq(compositeRegion), eq(state), any(), eq(monitor));
	}
}
