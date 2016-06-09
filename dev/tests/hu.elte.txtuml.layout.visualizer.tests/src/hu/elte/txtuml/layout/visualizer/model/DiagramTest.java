package hu.elte.txtuml.layout.visualizer.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DiagramTest {

	private Diagram _a;
	private Diagram _b;

	@Before
	public void setUp() {
		Set<RectangleObject> a_os = new HashSet<RectangleObject>();
		RectangleObject a1 = new RectangleObject("A1", new Point(0, 0));
		a1.setPixelWidth(20);
		a1.setWidth(3);
		a1.setPixelHeight(6);
		a1.setHeight(3);
		a_os.add(a1);

		RectangleObject a2 = new RectangleObject("A2", new Point(0, -4));
		a2.setPixelWidth(20);
		a2.setWidth(3);
		a2.setPixelHeight(9);
		a2.setHeight(4);
		a_os.add(a2);

		Set<LineAssociation> a_as = new HashSet<LineAssociation>();
		LineAssociation a12 = new LineAssociation("1_2", a1, a2);
		List<Point> a12_route = new ArrayList<Point>();
		a12_route.add(new Point(0, 0));
		a12_route.add(new Point(1, -2));
		a12_route.add(new Point(1, -3));
		a12_route.add(new Point(1, -4));
		a12_route.add(new Point(0, -4));
		a12.setRoute(a12_route);
		a_as.add(a12);

		LineAssociation a11 = new LineAssociation("1_1", a1, a1);
		List<Point> a11_route = new ArrayList<Point>();
		a11_route.add(new Point(0, 0));
		a11_route.add(new Point(0, -1));
		a11_route.add(new Point(-1, -1));
		a11_route.add(new Point(-1, 0));
		a11_route.add(new Point(-1, 1));
		a11_route.add(new Point(0, 1));
		a11_route.add(new Point(1, 1));
		a11_route.add(new Point(1, 0));
		a11_route.add(new Point(0, 0));
		a11.setRoute(a11_route);
		a_as.add(a11);

		_a = new Diagram(DiagramType.Class, a_os, a_as);

		Set<RectangleObject> b_os = new HashSet<RectangleObject>();
		b_os.add(new RectangleObject("B1"));
		b_os.add(new RectangleObject("B2"));

		Set<LineAssociation> b_as = new HashSet<LineAssociation>();
		b_as.add(new LineAssociation("1_2", "B1", "B2"));

		_b = new Diagram(DiagramType.State, b_os, b_as);
	}

	@Test
	public void PixelGridRatioTest() {
		assertEquals(10, _a.getPixelGridHorizontal().intValue());
		assertEquals(3, _a.getPixelGridVertical().intValue());
	}

	@Test
	public void HasValidLayoutTest() {
		assertTrue(_a.hasValidLayout());
		assertFalse(_b.hasValidLayout());
	}

	@Test
	public void DimensionTest() {
		assertEquals(4, _a.getWidth().intValue());
		assertEquals(9, _a.getHeight().intValue());
	}

	@Test
	public void AreaTest() {
		Boundary a_bounds = _a.getArea();
		
		assertEquals(-1 - 1, a_bounds.get_left().intValue());
		assertEquals(1 + 1, a_bounds.get_top().intValue());
		assertEquals(2 + 1, a_bounds.get_right().intValue());
		assertEquals(-7 - 1, a_bounds.get_bottom().intValue());
	}

}
