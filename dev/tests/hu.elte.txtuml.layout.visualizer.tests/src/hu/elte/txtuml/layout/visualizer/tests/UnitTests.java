package hu.elte.txtuml.layout.visualizer.tests;

import hu.elte.txtuml.layout.visualizer.algorithms.links.ArrangeAssociationTest;
import hu.elte.txtuml.layout.visualizer.algorithms.links.GraphSearchTest;
import hu.elte.txtuml.layout.visualizer.algorithms.utils.DefaultStatementTest;
import hu.elte.txtuml.layout.visualizer.model.DiagramTest;
import hu.elte.txtuml.layout.visualizer.model.DirectionTest;
import hu.elte.txtuml.layout.visualizer.model.PointTest;
import hu.elte.txtuml.layout.visualizer.model.RectangleObjectTest;
import hu.elte.txtuml.layout.visualizer.statements.StatementTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	DefaultStatementTest.class,
	ArrangeAssociationTest.class,
	GraphSearchTest.class,
	DiagramTest.class,
	DirectionTest.class,
	PointTest.class,
	RectangleObjectTest.class,
	StatementTest.class
	})
public class UnitTests {

}
