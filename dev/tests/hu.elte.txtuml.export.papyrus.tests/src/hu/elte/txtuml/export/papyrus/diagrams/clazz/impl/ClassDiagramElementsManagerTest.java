package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.UMLFactory;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.papyrus.arrange.ArrangeException;
import hu.elte.txtuml.export.papyrus.arrange.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.ClassDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.ClassDiagramNotationManager;


public class ClassDiagramElementsManagerTest {

	private ClassDiagramElementsManager instance;
	private Diagram diagram;
	private ClassDiagramElementsProvider provider;
	private ClassDiagramNotationManager notation;
	private IDiagramElementsArranger arranger;
	private IProgressMonitor monitor;

	@Before
	public void setUp() {
		this.diagram = mock(Diagram.class);
		this.provider = mock(ClassDiagramElementsProvider.class);
		this.notation = mock(ClassDiagramNotationManager.class);
		this.arranger = mock(IDiagramElementsArranger.class);
		this.monitor = new NullProgressMonitor();
		instance = new ClassDiagramElementsManager(diagram, provider, notation, arranger, monitor);
	}

	@Test
	public void testCallingArrange() {
		// when
		instance.addElementsToDiagram();

		// then
		try {
			verify(arranger).arrange(monitor);
		} catch (ArrangeException e) {
			// unreachable
			throw new RuntimeException("Impossible behaviour: mock arranger threw ArrangeException");
		}
	}

	@Test
	public void testAddClassesToDiagram() {
		// given
		Class class1 = mock(Class.class);
		Class class2 = mock(Class.class);
		Class class3 = mock(Class.class);
		List<Class> classes = Arrays.asList(class1, class2, class3);

		when(provider.getClasses()).thenReturn(classes);

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getClasses();
		verify(notation).createClassForDiagram(class1, null, monitor);
		verify(notation).createClassForDiagram(class2, null, monitor);
		verify(notation).createClassForDiagram(class3, null, monitor);
	}

	@Test
	public void testAddClassesWithBoundsToDiagram() {
		// given
		Class class1 = mock(Class.class);
		Class class2 = mock(Class.class);
		List<Class> classes = Arrays.asList(class1, class2);

		when(provider.getClasses()).thenReturn(classes);
		Rectangle boundsOfClass1 = new Rectangle(20, 20, 100, 100);
		Rectangle boundsOfClass2 = new Rectangle(120, 20, 100, 100);
		when(arranger.getBoundsForElement(class1)).thenReturn(boundsOfClass1);
		when(arranger.getBoundsForElement(class2)).thenReturn(boundsOfClass2);

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getClasses();
		verify(notation).createClassForDiagram(class1, boundsOfClass1, monitor);
		verify(notation).createClassForDiagram(class2, boundsOfClass2, monitor);
	}

	@Test
	public void testAddSignalsToDiagram() {
		// given
		Signal signal1 = mock(Signal.class);
		Signal signal2 = mock(Signal.class);
		List<Signal> signals = Arrays.asList(signal1, signal2);

		when(provider.getSignals()).thenReturn(signals);
		Rectangle boundsOfSignal1 = new Rectangle(20, 20, 80, 75);
		Rectangle boundsOfSignal2 = new Rectangle(120, 20, 80, 75);
		when(arranger.getBoundsForElement(signal1)).thenReturn(boundsOfSignal1);
		when(arranger.getBoundsForElement(signal2)).thenReturn(boundsOfSignal2);

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getClasses();
		verify(notation).createSignalForDiagram(signal1, boundsOfSignal1, monitor);
		verify(notation).createSignalForDiagram(signal2, boundsOfSignal2, monitor);
	}

	@Test
	public void testAddAssociationsToDiagram() {
		// given
		Model model = UMLFactory.eINSTANCE.createModel();
		Class class1 = model.createOwnedClass("Class1", false);
		Class class2 = model.createOwnedClass("Class2", false);
		Association assoc = class1.createAssociation(true, AggregationKind.NONE_LITERAL, "class1", 1, 1, class2, true,
				AggregationKind.NONE_LITERAL, "class2", 1, 1);

		when(provider.getClasses()).thenReturn(Arrays.asList(class1, class2));
		when(provider.getAssociations()).thenReturn(Arrays.asList(assoc));

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getClasses();
		verify(provider).getAssociations();
		verify(notation).createAssociationForNodes(eq(class2), eq(class1), eq(assoc),
				anyListOf(Point.class), anyString(), anyString(), eq(monitor));
	}
	
	@Test
	public void testAddGeneralizationToDiagram() {
		// given
		Model model = UMLFactory.eINSTANCE.createModel();
		Class superClass = model.createOwnedClass("SuperClass", false);
		Class subClass = model.createOwnedClass("Subclass", false);
		Generalization gen = subClass.createGeneralization(superClass);

		when(provider.getClasses()).thenReturn(Arrays.asList(superClass, subClass));
		when(provider.getGeneralizations()).thenReturn(Arrays.asList(gen));

		// when
		instance.addElementsToDiagram();

		// then
		verify(provider).getClasses();
		verify(provider).getGeneralizations();
		verify(notation).createGeneralizationForNodes(eq(gen), anyListOf(Point.class), anyString(), anyString(), eq(monitor));
	}
}
