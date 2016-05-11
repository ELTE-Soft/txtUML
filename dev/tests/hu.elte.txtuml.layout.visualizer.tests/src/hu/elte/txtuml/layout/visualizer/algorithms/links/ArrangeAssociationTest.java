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
import hu.elte.txtuml.layout.visualizer.model.Direction;
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
		
		try {
			ArrangeAssociations aa = 
					new ArrangeAssociations(os, as, ss, gid, _option);
			
			RectangleObject _A_mod = aa.objects().stream().filter(box -> box.getName().equals(_A.getName())).findFirst().get();
			RectangleObject _B_mod = aa.objects().stream().filter(box -> box.getName().equals(_B.getName())).findFirst().get();
			
			for(LineAssociation a : aa.value())
			{
				if(a.getId().equals("A_B"))
				{
					// Turn number equals
					assertEquals(Integer.valueOf(0), a.getTurns());
					
					// link 'a' comes out of _A_mod's southern side
					assertTrue(_A_mod.getPosition().isInTheDirection(a.getRoute().get(1), Direction.south, false) &&
							_A_mod.getTopLeft().isInTheDirection(a.getRoute().get(1), Direction.east, false) &&
							_A_mod.getBottomRight().isInTheDirection(a.getRoute().get(1), Direction.west, false));
					
					// link 'a' connects to _B_mod's northern side
					assertTrue(_B_mod.getPosition().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.north, true) &&
							_B_mod.getTopLeft().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.east, false) &&
							_B_mod.getBottomRight().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.west, false));
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
		
		try {
			ArrangeAssociations aa = 
					new ArrangeAssociations(os, as, ss, gid, _option);
			
			RectangleObject _A_mod = aa.objects().stream().filter(box -> box.getName().equals(_A.getName())).findFirst().get();
			RectangleObject _B_mod = aa.objects().stream().filter(box -> box.getName().equals(_B.getName())).findFirst().get();
			
			for(LineAssociation a : aa.value())
			{
				if(a.getId().equals("A_B"))
				{
					// Turn number equals
					assertEquals(Integer.valueOf(2), a.getTurns());
					
					// link 'a' comes out of _A_mod's eastern side
					assertTrue(_A_mod.getPosition().isInTheDirection(a.getRoute().get(1), Direction.east, false) &&
							_A_mod.getTopLeft().isInTheDirection(a.getRoute().get(1), Direction.south, false) &&
							_A_mod.getBottomRight().isInTheDirection(a.getRoute().get(1), Direction.north, false));
					// link 'a' connects to _B_mod's eastern side
					assertTrue(_B_mod.getPosition().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.east, false) &&
							_B_mod.getTopLeft().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.south, false) &&
							_B_mod.getBottomRight().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.north, false));
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
		
		try {
			ArrangeAssociations aa = 
					new ArrangeAssociations(os, as, ss, gid, _option);
			
			RectangleObject _A_mod = aa.objects().stream().filter(box -> box.getName().equals(_A.getName())).findFirst().get();
			RectangleObject _B_mod = aa.objects().stream().filter(box -> box.getName().equals(_B.getName())).findFirst().get();
			RectangleObject _C_mod = aa.objects().stream().filter(box -> box.getName().equals(_C.getName())).findFirst().get();
			
			for(LineAssociation a : aa.value())
			{
				if(a.getId().equals("A_C"))
				{
					// Turn number equals
					assertEquals(Integer.valueOf(2), a.getTurns());
					
					// Link 'a' 's start is in the line of object 'A'
					// Link 'a' comes out of object 'A' 's right side
					// Link 'a' comes out of object 'A' 's left side
					boolean inline_with_A = _A_mod.getTopLeft().isInTheDirection(a.getRoute().get(1), Direction.south, false) &&
							_A_mod.getBottomRight().isInTheDirection(a.getRoute().get(1), Direction.north, false);
					boolean right_of_A = _A_mod.getPosition().isInTheDirection(a.getRoute().get(1), Direction.east, false);
					boolean left_of_A = _A_mod.getPosition().isInTheDirection(a.getRoute().get(1), Direction.west, true);
					
					// All of link 'a' 's points are right from object 'B'
					// All of link 'a' 's points are left from object 'B'
					boolean right_of_B = a.getRoute().stream().allMatch(route_point -> _B_mod.getPosition().isInTheDirection(route_point, Direction.east, false));
					boolean left_of_B = a.getRoute().stream().allMatch(route_point -> _B_mod.getPosition().isInTheDirection(route_point, Direction.west, true));
					
					// Link 'a' 's end is in the line of object 'C'
					// Link 'a' connects to object 'C' 's right side
					// Link 'a' connects to object 'C' 's left side
					boolean inline_with_C = _C_mod.getTopLeft().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.south, false) &&
							_C_mod.getBottomRight().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.north, false);
					boolean right_of_C = _C_mod.getPosition().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.east, false);
					boolean left_of_C = _C_mod.getPosition().isInTheDirection(a.getRoute().get(a.getRoute().size() - 2), Direction.west, true);
					
					assertTrue(inline_with_A && inline_with_C && ((right_of_A && right_of_B && right_of_C) || (left_of_A && left_of_B && left_of_C)));
					
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
