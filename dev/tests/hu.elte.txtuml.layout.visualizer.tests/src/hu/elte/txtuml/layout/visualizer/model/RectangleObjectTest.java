package hu.elte.txtuml.layout.visualizer.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RectangleObjectTest {

	private RectangleObject _a;
	private RectangleObject _b;
	private RectangleObject _c;
	private RectangleObject _init;

	@Before
	public void setUp() {
		_a = new RectangleObject("A", new Point(0, 0));
		_a.setWidth(3);
		_a.setHeight(3);
		_b = new RectangleObject("B", new Point(0, 0));
		_b.setInner(new Diagram(DiagramType.Class));
		_c = new RectangleObject("C", new Point(0, -10));
		_c.setPixelWidth(12);
		_c.setPixelHeight(12);
		_c.setWidth(6);
		_c.setHeight(6);
		_init = new RectangleObject("Init", new Point(-1, 0));
		_init.setSpecial(SpecialBox.Initial);
	}

	@Test
	public void EqualsTest() {
		assertEquals(_a, new RectangleObject("A", new Point(0, 0)));
		assertNotEquals(_a, new RectangleObject("A", new Point(1, 0)));
		assertNotEquals(_a, _b);
	}

	@Test
	public void HasInnerTest() {
		assertFalse(_a.hasInner());
		assertTrue(_b.hasInner());
	}

	@Test
	public void PixelDimensionPresentTest() {
		assertFalse(_a.isPixelDimensionsPresent());
		assertTrue(_c.isPixelDimensionsPresent());
	}

	@Test
	public void IsSpecialTest() {
		assertFalse(_a.isSpecial());
		assertTrue(_init.isSpecial());
	}

	@Test
	public void IsCornerTest(){
		assertTrue(_a.isCornerPoint(new Point(0, 0)));
		assertTrue(_a.isCornerPoint(new Point(2, 0)));
		assertTrue(_a.isCornerPoint(new Point(2, -2)));
		assertTrue(_a.isCornerPoint(new Point(0, -2)));
		
		assertFalse(_a.isCornerPoint(new Point(2, 1)));
		assertFalse(_a.isCornerPoint(new Point(3, 3)));
		
	}
	
	@Test
	public void IsCornerTest2()
	{
		assertTrue(_c.isCornerPoint(new Point(0, -10)));
		assertTrue(_c.isCornerPoint(new Point(0, -15)));
		assertTrue(_c.isCornerPoint(new Point(5, -10)));
		assertTrue(_c.isCornerPoint(new Point(5, -15)));
		
		assertFalse(_c.isCornerPoint(new Point(1, -10)));
		assertFalse(_c.isCornerPoint(new Point(1, -11)));
	}
	
	
}
