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
	public void CenterPointTest1()
	{
		//Width/Height is odd
		assertEquals(4, _a.getCenterPoints().size());
		
		for(Point cp : _a.getCenterPoints()) {
			assertTrue(cp.equals(new Point(1, 0)) || cp.equals(new Point(0, -1)) ||
					 cp.equals(new Point(1, -2)) || cp.equals(new Point(2, -1)));
		}
	}
	
	@Test
	public void CenterPointTest2()
	{
		//Width/Height is equal
		assertEquals(4, _c.getCenterPoints().size());

		for(Point cp : _c.getCenterPoints()) {
			assertTrue(cp.equals(new Point(2, -10)) || cp.equals(new Point(0, -12)) ||
					cp.equals(new Point(2, -15)) || cp.equals(new Point(5, -12)));
		}
	}
	
}
