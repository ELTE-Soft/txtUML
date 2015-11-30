package hu.elte.txtuml.layout.visualizer.tests;

import hu.elte.txtuml.layout.visualizer.tests.algorithms.DefaultStatementTest;
import hu.elte.txtuml.layout.visualizer.tests.algorithms.links.GraphSearchTest;
import hu.elte.txtuml.layout.visualizer.tests.model.DirectionTest;
import hu.elte.txtuml.layout.visualizer.tests.model.PointTest;
import hu.elte.txtuml.layout.visualizer.tests.statements.StatementTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	DefaultStatementTest.class,
	GraphSearchTest.class,
	DirectionTest.class,
	PointTest.class,
	StatementTest.class
	})
public class UnitTests {

}
