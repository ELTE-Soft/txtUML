package hu.elte.txtuml.layout.visualizer.tests.algorithms;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import hu.elte.txtuml.layout.visualizer.algorithms.DefaultStatements;
import hu.elte.txtuml.layout.visualizer.exceptions.InternalException;
import hu.elte.txtuml.layout.visualizer.model.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.statements.Statement;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

import org.junit.Before;
import org.junit.Test;

public class DefaultStatementTest {

	private RectangleObject _a;
	private RectangleObject _b;
	private RectangleObject _c;
	private RectangleObject _d;
	private RectangleObject _e;
	private RectangleObject _f;
	private RectangleObject _g;
	private RectangleObject _h;
	
	private LineAssociation _a_b;
	private LineAssociation _c_d;
	
	private Statement _a_n_b;
	private Statement _c_n_d;
	private Statement _e_n_f;
	private Statement _g_n_h;
	
	@Before
	public void setUp() throws Exception {
		_a = new RectangleObject("A");
		_b = new RectangleObject("B");
		_c = new RectangleObject("C");
		_d = new RectangleObject("D");
		_e = new RectangleObject("E");
		_f = new RectangleObject("F");
		_g = new RectangleObject("G");
		_h = new RectangleObject("H");
		
		_a_b = new LineAssociation("A_B", _a, _b);
		_c_d = new LineAssociation("C_D", _c, _d);
		
		_a_n_b = new Statement(StatementType.north, _a.getName(), _b.getName());
		_c_n_d = new Statement(StatementType.north, _c.getName(), _d.getName());
		_e_n_f = new Statement(StatementType.north, _e.getName(), _f.getName());
		_g_n_h = new Statement(StatementType.north, _g.getName(), _h.getName());
	}
	
	@Test
	public void NoNewTest() {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_a);
		
		HashSet<LineAssociation> as = new HashSet<LineAssociation>();
		ArrayList<Statement> ss = new ArrayList<Statement>();
		
		try {
			DefaultStatements ds = new DefaultStatements(DiagramType.Class, os, as, ss, 0);
		
			assertEquals(new Integer(0), ds.getGroupId());
			assertEquals(0, ds.value().size());
		
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}
	
	@Test
	public void OneGroupTest() {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_a);
		os.add(_b);
		
		HashSet<LineAssociation> as = new HashSet<LineAssociation>();
		as.add(_a_b);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		
		try {
			DefaultStatements ds = new DefaultStatements(DiagramType.Class, os, as, ss, 0);
		
			assertEquals(new Integer(0), ds.getGroupId());
			assertEquals(0, ds.value().size());
		
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}
	
	@Test
	public void TwoGroupsWLinkTest() {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_a);
		os.add(_b);
		os.add(_c);
		os.add(_d);
		
		HashSet<LineAssociation> as = new HashSet<LineAssociation>();
		as.add(_a_b);
		as.add(_c_d);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		
		try {
			DefaultStatements ds = new DefaultStatements(DiagramType.Class, os, as, ss, 0);
		
			assertEquals(new Integer(1), ds.getGroupId());
			assertEquals(4, ds.value().size());
			
			for(Statement s : ds.value())
			{
				assertTrue(s.getParameter(0).equals(_a.getName()) 
						|| s.getParameter(0).equals(_b.getName()));
				assertTrue(s.getParameter(1).equals(_c.getName()) 
						|| s.getParameter(1).equals(_d.getName()));
			}
		
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}
	
	@Test
	public void TwoGroupsWStatementsTest() {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_a);
		os.add(_b);
		os.add(_c);
		os.add(_d);
		
		HashSet<LineAssociation> as = new HashSet<LineAssociation>();
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		ss.add(_a_n_b);
		ss.add(_c_n_d);
		
		try {
			DefaultStatements ds = new DefaultStatements(DiagramType.Class, os, as, ss, 0);
		
			assertEquals(new Integer(1), ds.getGroupId());
			assertEquals(4, ds.value().size());
			
			for(Statement s : ds.value())
			{
				assertTrue(s.getParameter(0).equals(_a.getName()) 
						|| s.getParameter(0).equals(_b.getName()));
				assertTrue(s.getParameter(1).equals(_c.getName()) 
						|| s.getParameter(1).equals(_d.getName()));
			}
		
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}
	
	@Test
	public void TwoGroupsWMixedTest() {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_a);
		os.add(_b);
		os.add(_c);
		os.add(_d);
		
		HashSet<LineAssociation> as = new HashSet<LineAssociation>();
		as.add(_a_b);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		ss.add(_c_n_d);
		
		try {
			DefaultStatements ds = new DefaultStatements(DiagramType.Class, os, as, ss, 0);
		
			assertEquals(new Integer(1), ds.getGroupId());
			assertEquals(4, ds.value().size());
			
			for(Statement s : ds.value())
			{
				assertTrue(s.getParameter(0).equals(_a.getName()) 
						|| s.getParameter(0).equals(_b.getName()));
				assertTrue(s.getParameter(1).equals(_c.getName()) 
						|| s.getParameter(1).equals(_d.getName()));
			}
		
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}
	
	@Test
	public void FourGroupLayoutTest() {
		HashSet<RectangleObject> os = new HashSet<RectangleObject>();
		os.add(_a);
		os.add(_b);
		os.add(_c);
		os.add(_d);
		os.add(_e);
		os.add(_f);
		os.add(_g);
		os.add(_h);
		
		HashSet<LineAssociation> as = new HashSet<LineAssociation>();
		as.add(_a_b);
		as.add(_c_d);
		
		ArrayList<Statement> ss = new ArrayList<Statement>();
		ss.add(_e_n_f);
		ss.add(_g_n_h);
		
		try {
			DefaultStatements ds = new DefaultStatements(DiagramType.Class, os, as, ss, 0);
		
			assertEquals(new Integer(6), ds.getGroupId());
			assertEquals(24, ds.value().size());		
		} catch (InternalException e) {
			fail("Test failed: " + e.getMessage());
		}
	}

}
