package hu.elte.txtuml.export.papyrus.diagrams.clazz.impl;

import static org.junit.Assert.*;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.export.papyrus.arrange.IDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.ClassDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.ClassDiagramNotationManager;

public class ClassDiagramElementsManagerTest {

	private ClassDiagramElementsManager instance;
	private Diagram diagram;
	private ClassDiagramElementsProvider provider;
	private ClassDiagramNotationManager notation;
	private IDiagramElementsArranger arranger;
	
	@Before
	public void setUp(){
		instance = new ClassDiagramElementsManager(diagram, provider, notation, arranger);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
