package hu.elte.txtuml.export.papyrus.arrange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import hu.elte.txtuml.export.papyrus.arrange.LayoutTransformer;

public class LayoutTransformerTest {

	@Test
	public void testAllObjectAreInPartI() {
		LayoutTransformer layoutTransformer = new LayoutTransformer();
		
		Map<String, Rectangle> objects = new HashMap<String, Rectangle>();
		Map<String, List<Point>> connections = new HashMap<String, List<Point>>();
		
		objects.put("A", new Rectangle(1,1,1,1));
		objects.put("B", new Rectangle(3,3,1,1));
		connections.put("A_B", Arrays.asList(new Point(2,1), new Point(3,2)));
		
		layoutTransformer.doTranformations(objects, connections);
		
		Assert.assertEquals(0, objects.get("A").x());
		Assert.assertEquals(2, objects.get("A").y());
		Assert.assertEquals(2, objects.get("B").x());
		Assert.assertEquals(0, objects.get("B").y());
		
		Assert.assertEquals(1, connections.get("A_B").get(0).x());
		Assert.assertEquals(2, connections.get("A_B").get(0).y());
		Assert.assertEquals(2, connections.get("A_B").get(1).x());
		Assert.assertEquals(1, connections.get("A_B").get(1).y());
	}

	@Test
	public void testAllObjectAreInPartII() {
		LayoutTransformer layoutTransformer = new LayoutTransformer();
		
		Map<String, Rectangle> objects = new HashMap<String, Rectangle>();
		Map<String, List<Point>> connections = new HashMap<String, List<Point>>();
		
		objects.put("A", new Rectangle(-3,1,1,1));
		objects.put("B", new Rectangle(-1,3,1,1));
		connections.put("A_B", Arrays.asList(new Point(-2,1), new Point(-1,2)));
		
		layoutTransformer.doTranformations(objects, connections);
		
		Assert.assertEquals(0, objects.get("A").x());
		Assert.assertEquals(2, objects.get("A").y());
		Assert.assertEquals(2, objects.get("B").x());
		Assert.assertEquals(0, objects.get("B").y());
		
		Assert.assertEquals(1, connections.get("A_B").get(0).x());
		Assert.assertEquals(2, connections.get("A_B").get(0).y());
		Assert.assertEquals(2, connections.get("A_B").get(1).x());
		Assert.assertEquals(1, connections.get("A_B").get(1).y());
	}

	@Test
	public void testAllObjectAreInPartIII() {
		LayoutTransformer layoutTransformer = new LayoutTransformer();
		
		Map<String, Rectangle> objects = new HashMap<String, Rectangle>();
		Map<String, List<Point>> connections = new HashMap<String, List<Point>>();
		
		objects.put("A", new Rectangle(-3,-3,1,1));
		objects.put("B", new Rectangle(-1,-1,1,1));
		connections.put("A_B", Arrays.asList(new Point(-2,-3), new Point(-1,-2)));
		
		layoutTransformer.doTranformations(objects, connections);
		
		Assert.assertEquals(0, objects.get("A").x());
		Assert.assertEquals(2, objects.get("A").y());
		Assert.assertEquals(2, objects.get("B").x());
		Assert.assertEquals(0, objects.get("B").y());
		
		Assert.assertEquals(1, connections.get("A_B").get(0).x());
		Assert.assertEquals(2, connections.get("A_B").get(0).y());
		Assert.assertEquals(2, connections.get("A_B").get(1).x());
		Assert.assertEquals(1, connections.get("A_B").get(1).y());
	}
	
	@Test
	public void testAllObjectAreInPartIV() {
		LayoutTransformer layoutTransformer = new LayoutTransformer();
		
		Map<String, Rectangle> objects = new HashMap<String, Rectangle>();
		Map<String, List<Point>> connections = new HashMap<String, List<Point>>();
		
		objects.put("A", new Rectangle(1,-3,1,1));
		objects.put("B", new Rectangle(3,-1,1,1));
		connections.put("A_B", Arrays.asList(new Point(2,-3), new Point(3,-2)));
		
		layoutTransformer.doTranformations(objects, connections);
		
		Assert.assertEquals(0, objects.get("A").x());
		Assert.assertEquals(2, objects.get("A").y());
		Assert.assertEquals(2, objects.get("B").x());
		Assert.assertEquals(0, objects.get("B").y());
		
		Assert.assertEquals(1, connections.get("A_B").get(0).x());
		Assert.assertEquals(2, connections.get("A_B").get(0).y());
		Assert.assertEquals(2, connections.get("A_B").get(1).x());
		Assert.assertEquals(1, connections.get("A_B").get(1).y());
	}


	@Test
	public void testObjectsInAllParts() {
		LayoutTransformer layoutTransformer = new LayoutTransformer();
		
		Map<String, Rectangle> objects = new HashMap<String, Rectangle>();
		Map<String, List<Point>> connections = new HashMap<String, List<Point>>();
		
		objects.put("A", new Rectangle(-1,1,1,1));
		objects.put("B", new Rectangle(1,1,1,1));
		objects.put("C", new Rectangle(1,-1,1,1));
		objects.put("D", new Rectangle(-1,-1,1,1));
		
		connections.put("A_B", Arrays.asList(new Point(0,1), new Point(1,1)));
		connections.put("B_C", Arrays.asList(new Point(2,0), new Point(2,-1)));
		connections.put("C_D", Arrays.asList(new Point(1,-2), new Point(0,-2)));
		connections.put("D_A", Arrays.asList(new Point(-1,-1), new Point(-1,0)));
		
		layoutTransformer.doTranformations(objects, connections);
		
		Assert.assertEquals(0, objects.get("A").x());
		Assert.assertEquals(0, objects.get("A").y());
		Assert.assertEquals(2, objects.get("B").x());
		Assert.assertEquals(0, objects.get("B").y());
		Assert.assertEquals(2, objects.get("C").x());
		Assert.assertEquals(2, objects.get("C").y());
		Assert.assertEquals(0, objects.get("D").x());
		Assert.assertEquals(2, objects.get("D").y());
		
		Assert.assertEquals(1, connections.get("A_B").get(0).x());
		Assert.assertEquals(0, connections.get("A_B").get(0).y());
		Assert.assertEquals(2, connections.get("A_B").get(1).x());
		Assert.assertEquals(0, connections.get("A_B").get(1).y());
		
		Assert.assertEquals(3, connections.get("B_C").get(0).x());
		Assert.assertEquals(1, connections.get("B_C").get(0).y());
		Assert.assertEquals(3, connections.get("B_C").get(1).x());
		Assert.assertEquals(2, connections.get("B_C").get(1).y());
		
		Assert.assertEquals(2, connections.get("C_D").get(0).x());
		Assert.assertEquals(3, connections.get("C_D").get(0).y());
		Assert.assertEquals(1, connections.get("C_D").get(1).x());
		Assert.assertEquals(3, connections.get("C_D").get(1).y());
		
		Assert.assertEquals(0, connections.get("D_A").get(0).x());
		Assert.assertEquals(2, connections.get("D_A").get(0).y());
		Assert.assertEquals(0, connections.get("D_A").get(1).x());
		Assert.assertEquals(1, connections.get("D_A").get(1).y());
	}
	
	@Test
	public void testLinksAreOnEdges(){
		LayoutTransformer layoutTransformer = new LayoutTransformer();

		Map<String, Rectangle> objects = new HashMap<String, Rectangle>();
		Map<String, List<Point>> connections = new HashMap<String, List<Point>>();
		
		objects.put("A", new Rectangle(-3,3,2,2));
		objects.put("B", new Rectangle(1,3,2,2));
		objects.put("C", new Rectangle(1,-1,2,2));
		objects.put("D", new Rectangle(-3,-1,2,2));
		connections.put("A_B", Arrays.asList(new Point(-2,3), new Point(-2,4), new Point(2, 4), new Point(2, 3)));
		connections.put("B_C", Arrays.asList(new Point(3,2), new Point(4,2), new Point(4, -2), new Point(3, -2)));
		connections.put("C_D", Arrays.asList(new Point(2,-3), new Point(2,-4), new Point(-2, -4), new Point(-2, -3)));
		connections.put("D_A", Arrays.asList(new Point(-3,-2), new Point(-4,-2), new Point(-4, 2), new Point(-3, 2)));
		
		layoutTransformer.doTranformations(objects, connections);
		
		Assert.assertEquals(1, objects.get("A").x());
		Assert.assertEquals(1, objects.get("A").y());
		
		Assert.assertEquals(5, objects.get("B").x());
		Assert.assertEquals(1, objects.get("B").y());
		
		Assert.assertEquals(5, objects.get("C").x());
		Assert.assertEquals(5, objects.get("C").y());
		
		Assert.assertEquals(1, objects.get("D").x());
		Assert.assertEquals(5, objects.get("D").y());
		
		Assert.assertEquals(2,  connections.get("A_B").get(0).x());
		Assert.assertEquals(1, connections.get("A_B").get(0).y());
		Assert.assertEquals(2, connections.get("A_B").get(1).x());
		Assert.assertEquals(0, connections.get("A_B").get(1).y());
		Assert.assertEquals(6,  connections.get("A_B").get(2).x());
		Assert.assertEquals(0, connections.get("A_B").get(2).y());
		Assert.assertEquals(6,  connections.get("A_B").get(3).x());
		Assert.assertEquals(1, connections.get("A_B").get(3).y());
		
		Assert.assertEquals(7,  connections.get("B_C").get(0).x());
		Assert.assertEquals(2, connections.get("B_C").get(0).y());
		Assert.assertEquals(8, connections.get("B_C").get(1).x());
		Assert.assertEquals(2, connections.get("B_C").get(1).y());
		Assert.assertEquals(8,  connections.get("B_C").get(2).x());
		Assert.assertEquals(6, connections.get("B_C").get(2).y());
		Assert.assertEquals(7,  connections.get("B_C").get(3).x());
		Assert.assertEquals(6, connections.get("B_C").get(3).y());
		
		Assert.assertEquals(6,  connections.get("C_D").get(0).x());
		Assert.assertEquals(7, connections.get("C_D").get(0).y());
		Assert.assertEquals(6, connections.get("C_D").get(1).x());
		Assert.assertEquals(8, connections.get("C_D").get(1).y());
		Assert.assertEquals(2,  connections.get("C_D").get(2).x());
		Assert.assertEquals(8, connections.get("C_D").get(2).y());
		Assert.assertEquals(2,  connections.get("C_D").get(3).x());
		Assert.assertEquals(7, connections.get("C_D").get(3).y());
		
		Assert.assertEquals(1,  connections.get("D_A").get(0).x());
		Assert.assertEquals(6, connections.get("D_A").get(0).y());
		Assert.assertEquals(0, connections.get("D_A").get(1).x());
		Assert.assertEquals(6, connections.get("D_A").get(1).y());
		Assert.assertEquals(0,  connections.get("D_A").get(2).x());
		Assert.assertEquals(2, connections.get("D_A").get(2).y());
		Assert.assertEquals(1,  connections.get("D_A").get(3).x());
		Assert.assertEquals(2, connections.get("D_A").get(3).y());
	}
}
