package hu.elte.txtuml.layout.visualizer.algorithms.links;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.layout.visualizer.algorithms.links.ArrangeAssociations;
import hu.elte.txtuml.layout.visualizer.events.ProgressManager;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.exceptions.MyException;
import hu.elte.txtuml.layout.visualizer.exceptions.UnknownStatementException;
import hu.elte.txtuml.layout.visualizer.helpers.Options;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.OverlapArrangeMode;
import hu.elte.txtuml.layout.visualizer.model.Point;

public class ArrangeAssociationTest {

	private RectangleObject _A;
	private RectangleObject _B;
	private RectangleObject _C;
	
	private LineAssociation _A_B;
	private LineAssociation _A_C;
	
	private Options _option;
	
	@Before
	public void setUp() throws Exception {
		ProgressManager.start();
		
		_A = new RectangleObject("A", new Point(0, 0));
		_B = new RectangleObject("B", new Point(0, -1));
		_C = new RectangleObject("C", new Point(0, -2));
		
		_A_B = new LineAssociation("A_B", _A, _B);
		
		_A_C = new LineAssociation("A_C", _A, _C);		
		
		_option = new Options();
		_option.ArrangeOverlaps = OverlapArrangeMode.few;
		_option.CorridorRatio = 1.0;
		_option.DiagramType = DiagramType.Class;
		_option.Logging = false;
	}

	@Test
	public void OneStraightLinkTest() throws MyException {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_A);
		os.add(_B);
		
		HashSet<LineAssociation> as= new HashSet<LineAssociation>();
		as.add(_A_B);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		
		Integer gid = 1;
		
		LineAssociation _A_B_expected_4wide = new LineAssociation(_A_B);
		ArrayList<Point> abep = new ArrayList<Point>();
		abep.add(new Point(0,0));
		abep.add(new Point(2,-3));
		abep.add(new Point(2,-4));
		abep.add(new Point(2,-5));
		abep.add(new Point(2,-6));
		abep.add(new Point(2,-7));
		abep.add(new Point(2,-8));
		abep.add(new Point(0,-8));
		_A_B_expected_4wide.setRoute(abep);
		
		try {
			ArrangeAssociations aa = 
					new ArrangeAssociations(os, as, ss, gid, _option);
			for(LineAssociation a : aa.value())
			{
				if(a.getId().equals(_A_B_expected_4wide.getId()))
				{
					assertArrayEquals(_A_B_expected_4wide.getRoute().toArray(),
							a.getRoute().toArray());
				}
				else
				{
					fail("Test failed: not expected link.");
				}
			}
		} catch (CannotFindAssociationRouteException e) {
			fail("Test failed: " + e.getMessage());
		} catch (ConversionException | InternalException 
				| UnknownStatementException e) {
			throw e;
		}
	}
	
	@Test
	public void OneLinkModifiedEndsTest() throws MyException {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_A);
		os.add(_B);
		
		HashSet<LineAssociation> as= new HashSet<LineAssociation>();
		as.add(_A_B);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		ss.add(Statement.Parse("east(A_B,A)"));
		
		Integer gid = 1;
		
		LineAssociation _A_B_expected_4wide = new LineAssociation(_A_B);
		ArrayList<Point> abep = new ArrayList<Point>();
		abep.add(new Point(0,0));
		abep.add(new Point(3,-2));
		abep.add(new Point(4,-2));
		abep.add(new Point(4,-3));
		abep.add(new Point(4,-4));
		abep.add(new Point(4,-5));
		abep.add(new Point(4,-6));
		abep.add(new Point(4,-7));
		abep.add(new Point(4,-8));
		abep.add(new Point(4,-9));
		abep.add(new Point(3,-9));
		abep.add(new Point(0,-8));
		_A_B_expected_4wide.setRoute(abep);
		
		try {
			ArrangeAssociations aa = 
					new ArrangeAssociations(os, as, ss, gid, _option);
			for(LineAssociation a : aa.value())
			{
				if(a.getId().equals(_A_B_expected_4wide.getId()))
				{
					assertArrayEquals(_A_B_expected_4wide.getRoute().toArray(),
							a.getRoute().toArray());
				}
				else
				{
					fail("Test failed: not expected link.");
				}
			}
		} catch (CannotFindAssociationRouteException e) {
			fail("Test failed: " + e.getMessage());
		} catch (ConversionException | InternalException 
				| UnknownStatementException e) {
			throw e;
		}
	}
	
	@Test
	public void OneLinkOneObjectBetweenTest() throws MyException {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_A);
		os.add(_B);
		os.add(_C);
		
		HashSet<LineAssociation> as= new HashSet<LineAssociation>();
		as.add(_A_C);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		
		Integer gid = 1;
		
		LineAssociation _A_C_expected_4wide = new LineAssociation(_A_C);
		ArrayList<Point> abep = new ArrayList<Point>();
		abep.add(new Point(0,0));
		abep.add(new Point(3,-2));
		abep.add(new Point(4,-2));
		abep.add(new Point(4,-3));
		abep.add(new Point(4,-4));
		abep.add(new Point(4,-5));
		abep.add(new Point(4,-6));
		abep.add(new Point(4,-7));
		abep.add(new Point(4,-8));
		abep.add(new Point(4,-9));
		abep.add(new Point(4,-10));
		abep.add(new Point(4,-11));
		abep.add(new Point(4,-12));
		abep.add(new Point(4,-13));
		abep.add(new Point(4,-14));
		abep.add(new Point(4,-15));
		abep.add(new Point(4,-16));
		abep.add(new Point(4,-17));
		abep.add(new Point(3,-17));
		abep.add(new Point(0,-16));
		_A_C_expected_4wide.setRoute(abep);
		
		LineAssociation _A_C_expected_alternative_4wide = new LineAssociation(_A_C);
		abep = new ArrayList<Point>();
		abep.add(new Point(0,0));
		abep.add(new Point(0,-2));
		abep.add(new Point(-1,-2));
		abep.add(new Point(-1,-3));
		abep.add(new Point(-1,-4));
		abep.add(new Point(-1,-5));
		abep.add(new Point(-1,-6));
		abep.add(new Point(-1,-7));
		abep.add(new Point(-1,-8));
		abep.add(new Point(-1,-9));
		abep.add(new Point(-1,-10));
		abep.add(new Point(-1,-11));
		abep.add(new Point(-1,-12));
		abep.add(new Point(-1,-13));
		abep.add(new Point(-1,-14));
		abep.add(new Point(-1,-15));
		abep.add(new Point(-1,-16));
		abep.add(new Point(-1,-17));
		abep.add(new Point(0,-17));
		abep.add(new Point(0,-16));
		_A_C_expected_alternative_4wide.setRoute(abep);
		
		try {
			ArrangeAssociations aa = 
					new ArrangeAssociations(os, as, ss, gid, _option);
			for(LineAssociation a : aa.value())
			{
				if(a.getId().equals(_A_C_expected_4wide.getId()))
				{
					assertTrue(_A_C_expected_alternative_4wide.getRoute().equals(a.getRoute())
							|| _A_C_expected_4wide.getRoute().equals(a.getRoute()));
					//assertArrayEquals(_A_C_expected_3wide.getRoute().toArray(),
					//		a.getRoute().toArray());
				}
				else
				{
					fail("Test failed: not expected link.");
				}
			}
		} catch (CannotFindAssociationRouteException e) {
			fail("Test failed: " + e.getMessage());
		} catch (ConversionException | InternalException 
				| UnknownStatementException e) {
			throw e;
		}
	}

}
