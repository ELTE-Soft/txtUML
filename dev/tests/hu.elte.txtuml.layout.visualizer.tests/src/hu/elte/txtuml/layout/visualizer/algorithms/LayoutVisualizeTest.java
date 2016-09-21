package hu.elte.txtuml.layout.visualizer.algorithms;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.layout.visualizer.exceptions.BoxArrangeConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.BoxOverlapConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.StatementsConflictException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.interfaces.IPixelDimensionProvider;
import hu.elte.txtuml.layout.visualizer.model.Diagram;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;
import hu.elte.txtuml.utils.Pair;

public class LayoutVisualizeTest {

	private class TestPixelProvider implements IPixelDimensionProvider {

		@Override
		public Pair<Width, Height> getPixelDimensionsFor(RectangleObject box) {
			if (!box.hasInner()) {
				return Pair.of(new Width(40), new Height(40));
			} else // if(box.hasInner())
			{
				Integer wpx = box.getInner().getWidth() * box.getInner().getPixelGridHorizontal();
				Integer hpx = box.getInner().getHeight() * box.getInner().getPixelGridVertical();

				return Pair.of(new Width(wpx), new Height(hpx));
			}
		}
		
	}
	
	private class TestObserver implements Observer {

		@Override
		public void update(Observable arg0, Object arg1) {
			//Do Nothing
		}
		
	}
	
	private Diagram _diagramA;
	private List<Statement> _statementsA;
	
	@Before
	public void setUp() {
		_diagramA = new Diagram(DiagramType.Class);
		
		_diagramA.Objects.add(new RectangleObject("A_A"));
		_diagramA.Objects.add(new RectangleObject("A_B"));
		_diagramA.Objects.add(new RectangleObject("A_C"));
		
		_diagramA.Assocs.add(new LineAssociation("A_R_A", "A_A", "A_A"));
		_diagramA.Assocs.add(new LineAssociation("A_L_AB", "A_A", "A_B"));
		_diagramA.Assocs.add(new LineAssociation("A_L_AC", "A_A", "A_C"));
		_diagramA.Assocs.add(new LineAssociation("A_R_C", "A_C", "A_C"));
		
		_statementsA = new ArrayList<Statement>();
		_statementsA.add(new Statement(StatementType.corridorsize, "0.5"));
		_statementsA.add(new Statement(StatementType.above, "A_A", "A_B"));
		_statementsA.add(new Statement(StatementType.above, "A_B", "A_C"));
		_statementsA.add(new Statement(StatementType.north, "A_R_A", "A_A", "Start"));
		_statementsA.add(new Statement(StatementType.east, "A_L_AC", "A_A"));
		_statementsA.add(new Statement(StatementType.south, "A_R_C", "A_C"));
	}
	
	@Test
	public void OkTest() {
		
		LayoutVisualize lv = new LayoutVisualize(new TestPixelProvider());
		lv.load(_diagramA);
		lv.addObserver(new TestObserver());
		
		try {
			lv.arrange(_statementsA);
			
			//Diagram result = lv.getDiagram();
			List<Statement> stats = lv.getStatements();
			
			assertTrue(stats.stream().allMatch(s -> !s.getType().equals(StatementType.corridorsize)));
			assertTrue(stats.stream().allMatch(s -> !s.getType().equals(StatementType.overlaparrange)));
			assertTrue(stats.stream().allMatch(s -> !s.getType().equals(StatementType.phantom)));
			assertTrue(stats.size() >= 2);
			
			List<Statement> astats = lv.getAssocStatements();
			assertTrue(astats.size() >= 3);
			
			
		} catch (InternalException | BoxArrangeConflictException | ConversionException
				| CannotFindAssociationRouteException | UnknownStatementException | BoxOverlapConflictException
				| StatementsConflictException e) {
			fail("Test failed!");
		}
	}
	
	@Test
	public void FailTest1() {
		
		LayoutVisualize lv = new LayoutVisualize(new TestPixelProvider());
		lv.load(_diagramA);
		lv.addObserver(new TestObserver());
		
		try {
			List<Statement> stats = new ArrayList<>(_statementsA);
			stats.add(new Statement(StatementType.below, "A_A", "A_B"));
			
			lv.arrange(stats);
			fail("Test failed!");
		} catch(BoxArrangeConflictException e) {
			assertTrue(true);
		} catch (InternalException | ConversionException
				| CannotFindAssociationRouteException | UnknownStatementException | BoxOverlapConflictException
				| StatementsConflictException e) {
			fail("Test failed!");
		}
	}
	
	@Test
	public void FailTest2() {
		
		LayoutVisualize lv = new LayoutVisualize(new TestPixelProvider());
		lv.load(_diagramA);
		lv.addObserver(new TestObserver());
		
		try {
			List<Statement> stats = new ArrayList<>(_statementsA);
			stats.add(new Statement(StatementType.priority, "A_L_AB", "2"));
			stats.add(new Statement(StatementType.priority, "A_L_AB", "3"));
			
			lv.arrange(stats);
			fail("Test failed!");
		} catch(StatementsConflictException e) {
			assertTrue(true);
		} catch (InternalException | BoxArrangeConflictException | ConversionException
				| CannotFindAssociationRouteException | UnknownStatementException | BoxOverlapConflictException
				 e) {
			fail("Test failed!");
		}
	}
}
