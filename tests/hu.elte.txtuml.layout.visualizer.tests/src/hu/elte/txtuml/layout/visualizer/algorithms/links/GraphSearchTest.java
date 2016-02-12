package hu.elte.txtuml.layout.visualizer.algorithms.links;

import static org.junit.Assert.*;
import hu.elte.txtuml.layout.visualizer.algorithms.links.GraphSearch;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Boundary;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Color;
import hu.elte.txtuml.layout.visualizer.algorithms.links.graphsearchhelpers.Node;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotFindAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.CannotStartAssociationRouteException;
import hu.elte.txtuml.layout.visualizer.exceptions.ConversionException;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.Point;
import hu.elte.txtuml.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class GraphSearchTest {

	private Boundary _bounds;
	private HashSet<Pair<Node, Double>> _startA;
	private HashSet<Pair<Node, Double>> _endB;
	private HashSet<Pair<Node, Double>> _endC;
	private HashMap<Point, Color> _colorsA;
	private HashMap<Point, Color> _colorsB;
	private HashMap<Point, Color> _colorsC;
	private ArrayList<Node> _A_B_straight;
	private ArrayList<Node> _A_B_obstacle1;
	private ArrayList<Node> _A_B_obstacle2;
	private ArrayList<Node> _A_C_one_turn1;
	private ArrayList<Node> _A_C_one_turn2;
	
	@Before
	public void setUp() throws Exception, InternalException {
		_bounds = new Boundary(50, -50, -50, 50);
		
		setupBoxes();
		setupColors();
		setupPaths();
	}
	
	private void setupBoxes()
	{
		_startA = new HashSet<Pair<Node, Double>>();
		_startA.add(new Pair<Node, Double>(new Node(new Point(0,1), new Point(0,2)), 1.0));
		_startA.add(new Pair<Node, Double>(new Node(new Point(1,0), new Point(2,0)), 1.0));
		_startA.add(new Pair<Node, Double>(new Node(new Point(0,-1), new Point(0,-2)), 1.0));
		_startA.add(new Pair<Node, Double>(new Node(new Point(-1,0), new Point(-2,0)), 1.0));
	
		_endB = new HashSet<Pair<Node, Double>>();
		_endB.add(new Pair<Node, Double>(new Node(new Point(0,-3), new Point(0,-4)), 1.0));
		_endB.add(new Pair<Node, Double>(new Node(new Point(2,-5), new Point(1,-5)), 1.0));
		_endB.add(new Pair<Node, Double>(new Node(new Point(0,-7), new Point(0,-6)), 1.0));
		_endB.add(new Pair<Node, Double>(new Node(new Point(-2,-5), new Point(-1,-5)), 1.0));
		
		_endC = new HashSet<Pair<Node, Double>>();
		_endC.add(new Pair<Node, Double>(new Node(new Point(5,-3), new Point(5,-4)), 1.0));
		_endC.add(new Pair<Node, Double>(new Node(new Point(7,-5), new Point(6,-5)), 1.0));
		_endC.add(new Pair<Node, Double>(new Node(new Point(5,-7), new Point(5,-6)), 1.0));
		_endC.add(new Pair<Node, Double>(new Node(new Point(3,-5), new Point(4,-5)), 1.0));
	}
	
	private void setupColors()
	{
		_colorsA = new HashMap<Point, Color>();
		_colorsA.put(new Point(-1,1), Color.Red);
		_colorsA.put(new Point(0,1), Color.Red);
		_colorsA.put(new Point(1,1), Color.Red);
		_colorsA.put(new Point(-1,0), Color.Red);
		_colorsA.put(new Point(0,0), Color.Red);
		_colorsA.put(new Point(1,0), Color.Red);
		_colorsA.put(new Point(-1,-1), Color.Red);
		_colorsA.put(new Point(0,-1), Color.Red);
		_colorsA.put(new Point(1,-1), Color.Red);
		
		_colorsB = new HashMap<Point, Color>();
		_colorsB.put(new Point(-1,-4), Color.Red);
		_colorsB.put(new Point(0,-4), Color.Red);
		_colorsB.put(new Point(1,-4), Color.Red);
		_colorsB.put(new Point(-1,-5), Color.Red);
		_colorsB.put(new Point(0,-5), Color.Red);
		_colorsB.put(new Point(1,-5), Color.Red);
		_colorsB.put(new Point(-1,-6), Color.Red);
		_colorsB.put(new Point(0,-6), Color.Red);
		_colorsB.put(new Point(1,-6), Color.Red);
		
		_colorsC = new HashMap<Point, Color>();
		_colorsC.put(new Point(4,-4), Color.Red);
		_colorsC.put(new Point(5,-4), Color.Red);
		_colorsC.put(new Point(6,-4), Color.Red);
		_colorsC.put(new Point(4,-5), Color.Red);
		_colorsC.put(new Point(5,-5), Color.Red);
		_colorsC.put(new Point(6,-5), Color.Red);
		_colorsC.put(new Point(4,-6), Color.Red);
		_colorsC.put(new Point(5,-6), Color.Red);
		_colorsC.put(new Point(6,-6), Color.Red);
	}

	private void setupPaths()
	{
		_A_B_straight = new ArrayList<Node>();
		_A_B_straight.add(new Node(new Point(0,-1), new Point(0,-2)));
		_A_B_straight.add(new Node(new Point(0,-2), new Point(0,-3)));
		_A_B_straight.add(new Node(new Point(0,-3), new Point(0,-4)));
		
		_A_B_obstacle1 = new ArrayList<Node>();
		_A_B_obstacle1.add(new Node(new Point(1, 0), new Point(2, 0)));
		_A_B_obstacle1.add(new Node(new Point(2, 0), new Point(2,-1)));
		_A_B_obstacle1.add(new Node(new Point(2,-1), new Point(2,-2)));
		_A_B_obstacle1.add(new Node(new Point(2,-2), new Point(2,-3)));
		_A_B_obstacle1.add(new Node(new Point(2,-3), new Point(2,-4)));
		_A_B_obstacle1.add(new Node(new Point(2,-4), new Point(2,-5)));
		_A_B_obstacle1.add(new Node(new Point(2,-5), new Point(1,-5)));
		
		_A_B_obstacle2 = new ArrayList<Node>();
		_A_B_obstacle2.add(new Node(new Point(-1, 0), new Point(-2, 0)));
		_A_B_obstacle2.add(new Node(new Point(-2, 0), new Point(-2,-1)));
		_A_B_obstacle2.add(new Node(new Point(-2,-1), new Point(-2,-2)));
		_A_B_obstacle2.add(new Node(new Point(-2,-2), new Point(-2,-3)));
		_A_B_obstacle2.add(new Node(new Point(-2,-3), new Point(-2,-4)));
		_A_B_obstacle2.add(new Node(new Point(-2,-4), new Point(-2,-5)));
		_A_B_obstacle2.add(new Node(new Point(-2,-5), new Point(-1,-5)));
		
		_A_C_one_turn1 = new ArrayList<Node>();
		_A_C_one_turn1.add(new Node(new Point(1, 0), new Point(2, 0)));
		_A_C_one_turn1.add(new Node(new Point(2, 0), new Point(3, 0)));
		_A_C_one_turn1.add(new Node(new Point(3, 0), new Point(4, 0)));
		_A_C_one_turn1.add(new Node(new Point(4, 0), new Point(5, 0)));
		_A_C_one_turn1.add(new Node(new Point(5, 0), new Point(5,-1)));
		_A_C_one_turn1.add(new Node(new Point(5,-1), new Point(5,-2)));
		_A_C_one_turn1.add(new Node(new Point(5,-2), new Point(5,-3)));
		_A_C_one_turn1.add(new Node(new Point(5,-3), new Point(5,-4)));
		
		_A_C_one_turn2 = new ArrayList<Node>();
		_A_C_one_turn2.add(new Node(new Point(0,-1), new Point(0,-2)));
		_A_C_one_turn2.add(new Node(new Point(0,-2), new Point(0,-3)));
		_A_C_one_turn2.add(new Node(new Point(0,-3), new Point(0,-4)));
		_A_C_one_turn2.add(new Node(new Point(0,-4), new Point(0,-5)));
		_A_C_one_turn2.add(new Node(new Point(0,-5), new Point(1,-5)));
		_A_C_one_turn2.add(new Node(new Point(1,-5), new Point(2,-5)));
		_A_C_one_turn2.add(new Node(new Point(2,-5), new Point(3,-5)));
		_A_C_one_turn2.add(new Node(new Point(3,-5), new Point(4,-5)));
	}
	
	@Test
	public void NoPathTest() {
		HashSet<Pair<Node, Double>> startset = new HashSet<Pair<Node, Double>>();
		startset.addAll(_startA);
		
		HashSet<Pair<Node, Double>> endset = new HashSet<Pair<Node, Double>>();
		endset.addAll(_endB);
		
		HashMap<Point, Color> colors = new HashMap<Point, Color>();
		colors.putAll(_colorsA);
		colors.putAll(_colorsB);
		colors.put(new Point(0, -2), Color.Red);
		colors.put(new Point(0, 2), Color.Red);
		colors.put(new Point(-2, 0), Color.Red);
		colors.put(new Point(2, 0), Color.Red);
		
		try {
			GraphSearch gs = new GraphSearch(startset, endset, colors, _bounds);
			gs.value();
			fail("Test failed!");
		} catch (CannotFindAssociationRouteException
				| CannotStartAssociationRouteException e) {
			assertTrue(true);
		} catch(ConversionException | InternalException e)
		{
			fail("Test failed: " + e.getMessage());
		}
	}
	
	@Test
	public void StraightLineTest() {
		HashSet<Pair<Node, Double>> startset = new HashSet<Pair<Node, Double>>();
		startset.addAll(_startA);
		
		HashSet<Pair<Node, Double>> endset = new HashSet<Pair<Node, Double>>();
		endset.addAll(_endB);
		
		HashMap<Point, Color> colors = new HashMap<Point, Color>();
		colors.putAll(_colorsA);
		colors.putAll(_colorsB);
		
		try {
			GraphSearch gs = new GraphSearch(startset, endset, colors, _bounds);
			ArrayList<Node> pp = gs.value();
			
			assertEquals(_A_B_straight.size(), pp.size());
			
			for(int i = 0; i < pp.size(); ++i)
			{
				assertEquals(_A_B_straight.get(i), pp.get(i));
			}
			
		} catch (CannotFindAssociationRouteException
				| CannotStartAssociationRouteException | ConversionException
				| InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}

	@Test
	public void OneTurnLineTest() {
		HashSet<Pair<Node, Double>> startset = new HashSet<Pair<Node, Double>>();
		startset.addAll(_startA);
		
		HashSet<Pair<Node, Double>> endset = new HashSet<Pair<Node, Double>>();
		endset.addAll(_endC);
		
		HashMap<Point, Color> colors = new HashMap<Point, Color>();
		colors.putAll(_colorsA);
		colors.putAll(_colorsC);
		
		try {
			GraphSearch gs = new GraphSearch(startset, endset, colors, _bounds);
			ArrayList<Node> pp = gs.value();
			
			assertEquals(_A_C_one_turn1.size(), pp.size());
			
			for(int i = 0; i < pp.size(); ++i)
			{
				assertTrue(_A_C_one_turn1.get(i).equals(pp.get(i)) 
						|| _A_C_one_turn2.get(i).equals(pp.get(i)));
			}
			
		} catch (CannotFindAssociationRouteException
				| CannotStartAssociationRouteException | ConversionException
				| InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
		
	}
	
	@Test
	public void OneTurnLineWBoxTest() {
		HashSet<Pair<Node, Double>> startset = new HashSet<Pair<Node, Double>>();
		startset.addAll(_startA);
		
		HashSet<Pair<Node, Double>> endset = new HashSet<Pair<Node, Double>>();
		endset.addAll(_endC);
		
		HashMap<Point, Color> colors = new HashMap<Point, Color>();
		colors.putAll(_colorsA);
		colors.putAll(_colorsC);
		colors.putAll(_colorsB);
		
		try {
			GraphSearch gs = new GraphSearch(startset, endset, colors, _bounds);
			ArrayList<Node> pp = gs.value();
			
			assertEquals(_A_C_one_turn1.size(), pp.size());
			
			for(int i = 0; i < pp.size(); ++i)
			{
				assertEquals(_A_C_one_turn1.get(i),pp.get(i));
			}
			
		} catch (CannotFindAssociationRouteException
				| CannotStartAssociationRouteException | ConversionException
				| InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
		
	}
	
	@Test
	public void StraightLineWObstacleTest() {
		HashSet<Pair<Node, Double>> startset = new HashSet<Pair<Node, Double>>();
		startset.addAll(_startA);
		
		HashSet<Pair<Node, Double>> endset = new HashSet<Pair<Node, Double>>();
		endset.addAll(_endB);
		
		HashMap<Point, Color> colors = new HashMap<Point, Color>();
		colors.putAll(_colorsA);
		colors.putAll(_colorsB);
		colors.put(new Point(0, -2), Color.Red);
		
		try {
			GraphSearch gs = new GraphSearch(startset, endset, colors, _bounds);
			ArrayList<Node> pp = gs.value();
			
			assertEquals(_A_B_obstacle1.size(), pp.size());
			
			for(int i = 0; i < pp.size(); ++i)
			{
				assertTrue(_A_B_obstacle1.get(i).equals(pp.get(i)) 
						|| _A_B_obstacle2.get(i).equals(pp.get(i)));
			}
			
		} catch (CannotFindAssociationRouteException
				| CannotStartAssociationRouteException | ConversionException
				| InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
		
	}
	
}
