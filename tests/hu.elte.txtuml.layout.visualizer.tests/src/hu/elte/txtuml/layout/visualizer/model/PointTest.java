package hu.elte.txtuml.layout.visualizer.model;

import static org.junit.Assert.*;
import hu.elte.txtuml.layout.visualizer.model.Direction;
import hu.elte.txtuml.layout.visualizer.model.Point;

import org.junit.Before;
import org.junit.Test;

public class PointTest {

	private Point _a;
	private Point _b;
	private Point _c;
	private Point _d;
	
	@Before
	public void setUp() throws Exception {
		_a = new Point(0, 0);
		_b = new Point(1, 0);
		_c = new Point(-1, 0);
		_d = new Point(2, 0);
	}
	
	@Test
	public void EqualsTest() {
		assertEquals(_a, new Point(0, 0));
		assertNotEquals(_a, _b);
	}

	@Test
	public void AddPoiTest() {
		assertEquals(_b, Point.Add(_a, _b));
		assertNotEquals(_b, Point.Add(_b, _b));
	}
	
	@Test
	public void SubstractPoiTest() {
		assertEquals(_b, Point.Substract( _b, _a));
		assertEquals(_c, Point.Substract( _a, _b));
		assertNotEquals(_b, Point.Substract(_a, _b));
		assertNotEquals(_a, Point.Substract(_a, _b));
	}
	
	@Test
	public void MultiplyAmountTest() {
		assertEquals(_a, Point.Multiply(_a, 2));
		assertEquals(_b, Point.Multiply(_b, 1));
		assertEquals(_d, Point.Multiply(_b, 2));
	}
	
	@Test
	public void DivideAmountTest() {
		assertEquals(_a, Point.Divide(_a, 2));
		assertEquals(_b, Point.Divide(_d, 2));
		assertEquals(_a, Point.Divide(_b, 2));
	}
	
	@Test
	public void AddDirTest() {
		assertEquals(_a, Point.Add(_c, Direction.east));
		assertEquals(_b, Point.Add(_a, Direction.east));
		assertEquals(_d, Point.Add(_b, Direction.east));
		assertEquals(_c, Point.Add(_a, Direction.west));
	}
	
	@Test
	public void MultiplyDirTest() {
		assertEquals(_d, Point.Multiply(Direction.east, 2));
		assertEquals(_b, Point.Multiply(Direction.east, 1));
		assertEquals(_a, Point.Multiply(Direction.east, 0));
	}
	
	@Test
	public void InTheDirectionTest1() {
		assertTrue(Point.isInTheDirection(_c, _a, Direction.east, false));
		assertTrue(Point.isInTheDirection(_d, _b, Direction.west, false));
		assertFalse(Point.isInTheDirection(_a, _b, Direction.north, false));
	}
	
	@Test
	public void InTheDirectionTest2() {
		assertTrue(Point.isInTheDirection(_c, _a, Direction.east, true));
		assertTrue(Point.isInTheDirection(_d, _b, Direction.west, true));
		assertTrue(Point.isInTheDirection(_a, _b, Direction.north, true));
	}
	
	@Test
	public void DirectionOfTest() {
		assertEquals( Direction.west, Point.directionOf(_a, _b));
		assertEquals( Direction.east, Point.directionOf(_a, _c));
		assertEquals( null, Point.directionOf(_a, new Point(1,1)));
	}
	
	@Test
	public void DirectionOfAllTest() {
		assertEquals(1 , Point.directionOfAll(_a, _b).size());
		assertEquals(1 , Point.directionOfAll(_a, _c).size());
		assertEquals(2 , Point.directionOfAll(_a, new Point(1, 1)).size());
	}

}
